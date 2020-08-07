package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.StatusCodes;
import ITM.maint.fiix_custom_mobile.constants.WorkOrderTasks;
import ITM.maint.fiix_custom_mobile.data.api.ErrorUtils;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.utils.Status;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefreshRepository extends BaseRepository {

    private static final String TAG = "WorkOrderRepository";
    protected static final String PREFERENCE_KEY = "MAIN_KEY";
    protected static final String SET_KEY = "TASK_LIST_SET";

    private MutableLiveData<Integer> taskListChangedLiveData;
    private MutableLiveData<Integer> workOrderChangedLiveData;
    private IWorkOrderService workOrderService;
    private CompositeDisposable disposables;
    private FindRequest workOrderRequest;

    private int userId;
    private ArrayList<Integer> taskList;
    private ArrayList<Integer> workOrderList;
    private MutableLiveData<Status> status;

    public RefreshRepository(Application application, int userId, ArrayList<Integer> taskList) {
        super(application);

        this.userId = userId;
        this.taskList = new ArrayList<Integer>(taskList);

        taskListChangedLiveData = new MutableLiveData<Integer>();
        workOrderChangedLiveData = new MutableLiveData<Integer>();
        status = new MutableLiveData<Status>();

        disposables = new CompositeDisposable();

        Disposable disposable = getDisposable();
        disposables.add(disposable);

        setupAPICall();
    }

    private void setupAPICall(){
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderTaskFields = new ArrayList<>(Arrays.asList(
                WorkOrderTasks.id.getField(),
                WorkOrderTasks.assignedToId.getField()
        ));

        String fields = TextUtils.join(",", workOrderTaskFields);

        List list = Stream.of(userId).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "dtmDateCompleted is null and intAssignedToUserID = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        workOrderRequest = new FindRequest("FindRequest", clientVersion, "WorkOrderTask", fields, filters);

    }


    private void updateTaskList(Long aLong){


        Observable<List<WorkOrderTask>> observable = workOrderService.getWorkOrderTaskList(workOrderRequest);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<WorkOrderTask>>() {
                    @Override
                    public void onNext(List<WorkOrderTask> workOrderTasks) {
                        if (workOrderTasks != null) {
                            if (workOrderTasks.isEmpty()) {
                                Log.d(TAG, "response is empty");
                            } else {
                                //compare last work order task list with new
                                ArrayList<Integer> newTaskList = new ArrayList<>();
                                ArrayList<Integer> newWorkOrderList = new ArrayList<>();
                                for (WorkOrderTask task : workOrderTasks) {
                                    newTaskList.add(task.getId());
                                    newWorkOrderList.add(task.getWorkOrderId());
                                }

                                if (newTaskList.size() > taskList.size()) {
                                    taskListChangedLiveData.postValue(newTaskList.size() - taskList.size());
                                } else if (newWorkOrderList.size() < taskList.size()) {
                                    SharedPreferences sharedPreferences = application.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    Set<String> set = new HashSet<String>();
                                    set.addAll(newTaskList.stream()
                                            .map(String::valueOf)
                                            .collect(Collectors.toList()));
                                    editor.putStringSet(SET_KEY, set);
                                    editor.apply();

                                } else {
                                    for (Integer value : newTaskList) {
                                        if (!taskList.contains(value)) {
                                            taskListChangedLiveData.postValue(0);
                                            break;
                                        }
                                    }
                                }

                                if (newWorkOrderList.size() > workOrderList.size()) {
                                    workOrderChangedLiveData.postValue(newWorkOrderList.size() - workOrderList.size());
                                } else if (newWorkOrderList.size() < workOrderList.size()) {
                                    SharedPreferences sharedPreferences = application.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    Set<String> set = new HashSet<String>();
                                    set.addAll(newWorkOrderList.stream()
                                            .map(String::valueOf)
                                            .collect(Collectors.toList()));
                                    editor.putStringSet(SET_KEY, set);
                                    editor.apply();

                                } else {
                                    for (Integer value : newWorkOrderList) {
                                        if (!workOrderList.contains(value)) {
                                            workOrderChangedLiveData.postValue(0);
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Error in refreshing task list");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private Disposable getDisposable(){
        return Observable.interval(1,5, TimeUnit.MINUTES)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::updateTaskList, this::handleError);
    }

    private void handleError(Throwable t){
        if (t instanceof IOException) {
            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
            // logging probably not necessary
        } else {
            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
        }
    }

    public Boolean checkIfDisposed(){
        return disposables.isDisposed();
    }

    public void createDisposable(){
        Disposable disposable = getDisposable();
        disposables.add(disposable);
    }

    public void dispose(){
        disposables.clear();
    }

    public MutableLiveData<Integer> getTaskListChangedLiveData() {
        return taskListChangedLiveData;
    }

    public MutableLiveData<Integer> getWorkOrderChangedLiveData() {
        return workOrderChangedLiveData;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }
}
