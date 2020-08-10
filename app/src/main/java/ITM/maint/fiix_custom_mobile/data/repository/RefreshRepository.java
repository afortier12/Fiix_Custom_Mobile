package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.constants.StatusCodes;
import ITM.maint.fiix_custom_mobile.constants.WorkOrderTasks;
import ITM.maint.fiix_custom_mobile.constants.WorkOrders;
import ITM.maint.fiix_custom_mobile.data.api.ErrorUtils;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IWorkOrderDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.utils.Status;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefreshRepository extends BaseRepository {

    private static final String TAG = "WorkOrderRepository";
    protected static final String PREFERENCE_KEY = "MAIN_KEY";
    protected static final String SET_KEY = "TASK_LIST_SET";

    private MutableLiveData<WorkOrderTaskMessage> taskListChangedLiveData;
    private IWorkOrderService workOrderService;
    private CompositeDisposable disposables;
    private FindRequest workOrderTaskRequest;

    private IWorkOrderDao workOrderDao;
    private FiixDatabase fiixDatabase;

    private int userId;
    private List<WorkOrderTask> dbWorkOrderTaskList;
    private List<WorkOrder> dbWorkOrderList;

    private MutableLiveData<Status> status;

    public RefreshRepository(Application application, int userId) {
        super(application);

        this.userId = userId;

        taskListChangedLiveData = new MutableLiveData<WorkOrderTaskMessage>();
        status = new MutableLiveData<Status>();
        workOrderService = ServiceGenerator.createService(IWorkOrderService.class);

        dbWorkOrderTaskList = new ArrayList<WorkOrderTask>();
        dbWorkOrderList = new ArrayList<WorkOrder>();

        fiixDatabase = FiixDatabase.getDatabase(application);
        workOrderDao = fiixDatabase.workOrderDao();
        disposables = new CompositeDisposable();

        Disposable disposable = getDisposable();
        disposables.add(disposable);

        setupTaskAPICall();
    }

    private void getTaskListFromDB(){
        Single<List<WorkOrderTask>> single = workOrderDao.getAssignedWorkOrderTasks(userId);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<WorkOrderTask>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<WorkOrderTask> taskList) {
                if (taskList.isEmpty()) {
                    dbWorkOrderTaskList.clear();
                } else {
                    dbWorkOrderTaskList.clear();
                    dbWorkOrderTaskList.addAll(taskList);
                    getTasksFromFiix();
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                dbWorkOrderTaskList.clear();
                Log.d(TAG, "Error finding work order task from DB");
            }

        });
    }

    private void getWorkOrdersFromDB(){
        Single<List<WorkOrder>> single = workOrderDao.getWorkOrders();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<WorkOrder>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<WorkOrder> orderList) {
                if (orderList.isEmpty()) {
                    dbWorkOrderList.clear();
                } else {
                    dbWorkOrderList.clear();
                    dbWorkOrderList.addAll(orderList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                dbWorkOrderList.clear();
                Log.d(TAG, "Error finding work order task from DB");
            }

        });
    }

    private void setupTaskAPICall(){
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderTaskFields = new ArrayList<>(Arrays.asList(
                WorkOrderTasks.id.getField(),
                WorkOrderTasks.assignedToId.getField(),
                WorkOrderTasks.workOrderId.getField(),
                WorkOrderTasks.completedDate.getField()
        ));

        String fields = TextUtils.join(",", workOrderTaskFields);

        List list = Stream.of(userId).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "dtmDateCompleted is null and intAssignedToUserID = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        workOrderTaskRequest = new FindRequest("FindRequest", clientVersion, "WorkOrderTask", fields, filters);

    }

    private void getTasksFromFiix(){

        Observable<List<WorkOrderTask>> observable = workOrderService.getWorkOrderTaskList(workOrderTaskRequest);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<WorkOrderTask>>() {
                    @Override
                    public void onNext(List<WorkOrderTask> workOrderTasks) {
                        if (workOrderTasks != null) {
                            if (workOrderTasks.isEmpty()) {
                                Log.d(TAG, "response is empty");
                            } else {
                                boolean differences = checkTaskListDifferences(workOrderTasks);
                                List<Integer> workOrderIdList = new ArrayList<Integer>();
                                for (WorkOrderTask task: workOrderTasks){
                                    if(!workOrderIdList.contains(task.getWorkOrderId()))
                                        workOrderIdList.add(task.getWorkOrderId());
                                }
                                setupOrderAPICall(workOrderIdList);
                                updateWorkOrderTasks(workOrderTasks);
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


    public void setupOrderAPICall(List<Integer> workOrders) {
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderFields = new ArrayList<>(Arrays.asList(
                WorkOrders.id.getField(),
                WorkOrders.code.getField(),
                WorkOrders.priorityId.getField(),
                WorkOrders.priority.getField(),
                WorkOrders.assets.getField(),
                WorkOrders.description.getField(),
                WorkOrders.statusId.getField(),
                WorkOrders.workOrderStatus.getField(),
                WorkOrders.maintenanceTypeId.getField(),
                WorkOrders.maintenanceType.getField(),
                WorkOrders.requestedByUser.getField(),
                WorkOrders.guestEmail.getField(),
                WorkOrders.guestName.getField(),
                WorkOrders.guestPhone.getField(),
                WorkOrders.estCompletionDate.getField(),
                WorkOrders.adminNotes.getField(),
                WorkOrders.assetIds.getField()
        ));

        String fields = TextUtils.join(",", workOrderFields);
        List<String> placeHolders = new ArrayList<>();
        List<Integer> parameters = new ArrayList<>();
        for (Integer id : workOrders) {
            parameters.add(id);
            placeHolders.add("?");
        }
        String placeHolderList = TextUtils.join(",", placeHolders);

        FindRequest.Filter filter = new FindRequest.Filter(
                "dtmDateCompleted is null and id in (" + placeHolderList + ")",
                parameters
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "WorkOrder", fields, filters);
        getWorkOrdersFromFiix(workOrderRequest);
    }

    private void getWorkOrdersFromFiix(FindRequest workOrderRequest) {
        workOrderService.getWorkOrderList(workOrderRequest)
                .enqueue(new Callback<List<WorkOrder>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrder>> call, Response<List<WorkOrder>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null)
                                if (!response.body().isEmpty())
                                    updateWorkOrders(response.body());
                                else
                                    Log.d(TAG, "response is empty");
                            else
                                Log.d(TAG, "response is empty");
                        } else {
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages())
                                Log.d(TAG, String.valueOf(msg));
                        }

                    }

                    @Override
                    public void onFailure(Call<List<WorkOrder>> call, Throwable t) {
                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        } else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });
    }

    private boolean checkTaskListDifferences(List<WorkOrderTask> workOrderTasks){
        //compare last work order task list with new
        WorkOrderTaskMessage msg = new WorkOrderTaskMessage();
        for (WorkOrderTask newTask : workOrderTasks) {
            boolean taskFound = false;
            boolean orderFound = false;
            for (WorkOrderTask oldTask: dbWorkOrderTaskList){
                if (oldTask.getId() == newTask.getId()) {
                    taskFound = true;
                    break;
                }
                if (oldTask.getWorkOrderId() == newTask.getWorkOrderId()){
                    orderFound = true;
                }
            }
            if (!orderFound){
                msg.addOrder(newTask.getWorkOrderId());
            } else if (!taskFound){
                msg.addTask(newTask);
            }
        }

        if (msg.getTaskList().size() > 0 || msg.getOrderList().size() > 0) {
            dbWorkOrderTaskList = new ArrayList<WorkOrderTask>(workOrderTasks);
            taskListChangedLiveData.postValue(msg);
            return true;
        }

        return false;
    }


    private void updateWorkOrders(List<WorkOrder> workOrders) {

        boolean updateDBFlag = false;
        List<WorkOrder> newWorkOrders = new ArrayList<>();
        for (WorkOrder apiWorkOrder: workOrders) {
            boolean found = false;
            for (WorkOrder dbWorkOrder : dbWorkOrderList) {
                if (dbWorkOrder.getId() == (apiWorkOrder.getId())) {
                    dbWorkOrder.setPriorityId(apiWorkOrder.getPriorityId());
                    dbWorkOrder.setMaintenanceTypeId(apiWorkOrder.getMaintenanceTypeId());
                    dbWorkOrder.setStatusId(apiWorkOrder.getStatusId());
                    dbWorkOrder.setDateCompleted(apiWorkOrder.getDateCompleted());
                    found = true;
                    updateDBFlag = true;
                }
            }
            if (!found)
                newWorkOrders.add(apiWorkOrder);
        }

        if (newWorkOrders.size() > 0)
            insertWorkOrders(newWorkOrders);

        if (updateDBFlag) {
            Completable completable = fiixDatabase.workOrderDao().updateWorkOrders(dbWorkOrderList);
            Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
            disposableCompletableObserver = new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Log.d(TAG, "work order added to DB");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "Error adding work order to DB");
                }
            };
            completable.subscribeOn(scheduler)
                    .subscribe(disposableCompletableObserver);
            compositeDisposable.add(disposableCompletableObserver);
        }
    }

    private void insertWorkOrders(List<WorkOrder> workOrders){
        Completable completable = fiixDatabase.workOrderDao().insertWorkOrders(workOrders);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "work order added to DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error adding work order to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }


    private void updateWorkOrderTasks(List<WorkOrderTask> workOrderTasks) {

        boolean updateDBFlag = false;
        List<WorkOrderTask> newWorkOrderTasks = new ArrayList<>();
        for (WorkOrderTask apiTask: workOrderTasks) {
            boolean found = false;
            for (WorkOrderTask dbWorkOrderTask : dbWorkOrderTaskList) {
                if (dbWorkOrderTask.getId() == (apiTask.getId())) {
                    dbWorkOrderTask.setEstimatedHours(apiTask.getEstimatedHours());
                    dbWorkOrderTask.setCompletedDate(apiTask.getCompletedDate());
                    found = true;
                    updateDBFlag = true;
                }
            }
            if (!found)
                newWorkOrderTasks.add(apiTask);
        }

        if (newWorkOrderTasks.size() > 0)
            insertWorkOrderTasks(newWorkOrderTasks);

        if (updateDBFlag) {
            Completable completable = fiixDatabase.workOrderDao().updateTasks(dbWorkOrderTaskList);
            Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
            disposableCompletableObserver = new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Log.d(TAG, "work order added to DB");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "Error adding work order to DB");
                }
            };
            completable.subscribeOn(scheduler)
                    .subscribe(disposableCompletableObserver);
            compositeDisposable.add(disposableCompletableObserver);
        }
    }

    private void insertWorkOrderTasks(List<WorkOrderTask> workOrderTasks){
        Completable completable = fiixDatabase.workOrderDao().insertTasks(workOrderTasks);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "work order added to DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error adding work order to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }


    private void updateTaskList(Long aLong) {
        getTaskListFromDB();
        getWorkOrdersFromDB();
    }

    private Disposable getDisposable(){
        return Observable.interval(1,2, TimeUnit.MINUTES)
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

    public MutableLiveData<WorkOrderTaskMessage> getTaskListChangedLiveData() {
        return taskListChangedLiveData;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    public static class WorkOrderTaskMessage {

        private List<WorkOrderTask> taskList;
        private List<Integer> orderList;

        public WorkOrderTaskMessage() {
            taskList = new ArrayList<>();
            orderList = new ArrayList<>();
        }

        public void addTask(WorkOrderTask task){
            taskList.add(task);
        }

        public void addOrder(int orderNumber){
            orderList.add(orderNumber);
        }

        public List<WorkOrderTask> getTaskList() {
            return taskList;
        }

        public List<Integer> getOrderList() {
            return orderList;
        }
    }
}
