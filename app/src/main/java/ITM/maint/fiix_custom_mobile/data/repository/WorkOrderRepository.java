package ITM.maint.fiix_custom_mobile.data.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.constants.Priorities;
import ITM.maint.fiix_custom_mobile.constants.WorkOrderTasks;
import ITM.maint.fiix_custom_mobile.constants.WorkOrders;
import ITM.maint.fiix_custom_mobile.data.api.ErrorUtils;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.ILookupTablesDao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IWorkOrderDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrderRepository extends BaseRepository implements IWorkOrderRepository{

    private static final String TAG ="WorkOrderRepository";
    private IWorkOrderService workOrderService;
    private MutableLiveData<List<WorkOrder>> workOrderResponseMutableLiveData;
    private MutableLiveData<List<WorkOrderTask>> workOrderTaskResponseMutableLiveData;
    private MutableLiveData<String> status;

    private IWorkOrderDao workOrderDao;
    private ILookupTablesDao lookupTablesDao;
    private FiixDatabase fiixDatabase;
    private MutableLiveData<List<WorkOrderJoinPriority>> workOrderDBMutableLiveData;
    private ArrayList<Integer> workOrderIdList;
    private CompositeDisposable compositeDisposable;

    private String username;
    private int userId;


    public WorkOrderRepository(Application application) {
        super(application);

        workOrderResponseMutableLiveData = new MutableLiveData<List<WorkOrder>>();
        workOrderTaskResponseMutableLiveData = new MutableLiveData<List<WorkOrderTask>>();
        status = new MutableLiveData<String>();
        workOrderService = ServiceGenerator.createService(IWorkOrderService.class);

        workOrderIdList = new ArrayList<>();

        fiixDatabase = FiixDatabase.getDatabase(application);
        workOrderDao = fiixDatabase.workOrderDao();
        lookupTablesDao = fiixDatabase.lookupTablesDao();
        compositeDisposable = new CompositeDisposable();
        workOrderDBMutableLiveData = new MutableLiveData<List<WorkOrderJoinPriority>>();
    }

    @Override
    public void getWorkOrdersForUser(String username, int userId) {

        this.username = username;
        this.userId = userId;

        getPrioritiesFromDB();


    }

    private void getWorkOrdersFromDB(){
        //get from database if available
        Single<List<WorkOrder>> single = fiixDatabase.workOrderDao().getWorkOrdersforUser(username);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<WorkOrder>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<WorkOrder> workOrderList) {
                if (workOrderList.isEmpty()){
                    //no work orders in database -> request from Fiix
                    ; getTasksFromFiix(username, userId, 0);
                } else {
                    getWorkOrdersWithPriorities();
                    //getPrioritiesFromFiix();
                    //workOrderResponseMutableLiveData.postValue(workOrderList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                getTasksFromFiix(username, userId, 0);
                Log.d(TAG, "Error finding work order from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getWorkOrderTasks(String username, int userId, int workOrderId) {

    }

    public void getTasksFromFiix(String username, int userId, int workOrderId){
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderTaskFields = new ArrayList<>(Arrays.asList(
                WorkOrderTasks.id.getField(),
                WorkOrderTasks.assignedToId.getField(),
                WorkOrderTasks.workOrderId.getField()
        ));

        String fields = TextUtils.join(",",workOrderTaskFields);

        List list = Stream.of(userId).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "dtmDateCompleted is null and intAssignedToUserID = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "WorkOrderTask", fields, filters);
        requestTasksFromFiix(workOrderRequest);
    }

    private void requestTasksFromFiix(FindRequest workOrderRequest){
        workOrderService.getWorkOrderTasks(workOrderRequest)
                .enqueue(new Callback<List<WorkOrderTask>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrderTask>> call, Response<List<WorkOrderTask>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().isEmpty()){
                                    workOrderResponseMutableLiveData.postValue(null);

                                } else {
                                    workOrderIdList.clear();
                                    for (WorkOrderTask workOrderTask : response.body()) {
                                        workOrderIdList.add(workOrderTask.getWorkOrderId());
                                    }
                                    getWorkOrders(workOrderIdList);
                                }
                            } else {
                                workOrderResponseMutableLiveData.postValue(null);
                                Log.d(TAG, "response is empty");
                            }
                        } else {
                            workOrderResponseMutableLiveData.postValue(null);
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages()) {
                                status.postValue(msg);
                                Log.d(TAG, String.valueOf(msg));
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<WorkOrderTask>> call, Throwable t) {
                        workOrderTaskResponseMutableLiveData.postValue(null);

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        } else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });

    }

    @Override
    public void getWorkOrders(List<Integer> workOrders) {
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderFields = new ArrayList<>(Arrays.asList(
                WorkOrders.id.getField(),
                WorkOrders.code.getField(),
                WorkOrders.priorityId.getField(),
                WorkOrders.priority.getField(),
                WorkOrders.maintenanceType.getField(),
                WorkOrders.assets.getField(),
                WorkOrders.description.getField(),
                WorkOrders.problem.getField()
        ));

        String fields = TextUtils.join(",",workOrderFields);
        List<String> placeHolders = new ArrayList<>();
        List<Integer> parameters = new ArrayList<>();
        for (Integer id: workOrders){
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
        requestWorkOrdersFromFiix(workOrderRequest);
    }

    private void requestWorkOrdersFromFiix(FindRequest workOrderRequest){
        workOrderService.getWorkOrderList(workOrderRequest)
                .enqueue(new Callback<List<WorkOrder>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrder>> call, Response<List<WorkOrder>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                addWorkOrders(response.body());
                                //workOrderResponseMutableLiveData.postValue(response.body());
                            } else {
                                workOrderResponseMutableLiveData.postValue(null);
                                Log.d(TAG, "response is empty");
                            }
                        } else {
                            workOrderResponseMutableLiveData.postValue(null);
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages())
                                Log.d(TAG, String.valueOf(msg));
                        }

                    }

                    @Override
                    public void onFailure(Call<List<WorkOrder>> call, Throwable t) {
                        workOrderResponseMutableLiveData.postValue(null);

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        } else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });
    }

    @Override
    public void addWorkOrders(List<WorkOrder> workOrderList) {

        for (WorkOrder order: workOrderList){
            order.setUsername(username);
        }

        Completable completable = fiixDatabase.workOrderDao().insertWorkOrders(workOrderList);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
               // String msg = Resources.getSystem().getString(R.string.work_orders_added);
                //status.postValue(msg);
               // Log.d(TAG, msg);
                getWorkOrdersWithPriorities();
                Log.d(TAG, "work order added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = Resources.getSystem().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding work order to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void getPrioritiesFromDB() {
        Single<List<Priority>> single = fiixDatabase.workOrderDao().getPriorities();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<Priority>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<Priority> priorityList) {
                if (priorityList.isEmpty()){
                    //no work orders in database -> request from Fiix
                    getPrioritiesFromFiix();
                } else {
                    getWorkOrdersFromDB();
                    //workOrderResponseMutableLiveData.postValue(workOrderList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                getTasksFromFiix(username, userId, 0);
                Log.d(TAG, "Error finding work order from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    public void getPrioritiesFromFiix(){
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> priorityFields = new ArrayList<>(Arrays.asList(
                Priorities.id.getField(),
                Priorities.order.getField(),
                Priorities.name.getField()
        ));

        String fields = TextUtils.join(",",priorityFields);

        FindRequest priorityRequest = new FindRequest("FindRequest", clientVersion, "Priority", fields, null);
        requestPriorityFromFiix(priorityRequest);
    }

    private void requestPriorityFromFiix(FindRequest priorityRequest){
        workOrderService.getPriorityList(priorityRequest)
                .enqueue(new Callback<List<Priority>>() {

                    @Override
                    public void onResponse(Call<List<Priority>> call, Response<List<Priority>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().isEmpty()){
                                    Log.d(TAG, "Priority request returned empty list of priorities");
                                } else {
                                    addPriorities(response.body());
                                }
                            } else {
                                Log.d(TAG, "priority response is empty");
                            }
                        } else {
                            Log.d(TAG, "priority response error");
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages()) {
                                status.postValue(msg);
                                Log.d(TAG, String.valueOf(msg));
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Priority>> call, Throwable t) {

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        } else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });

    }

    public void addPriorities(List<Priority> priorities) {

        Completable completable = fiixDatabase.lookupTablesDao().insert(priorities);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                // String msg = Resources.getSystem().getString(R.string.work_orders_added);
                //status.postValue(msg);
                // Log.d(TAG, msg);
                getWorkOrdersFromDB();
                Log.d(TAG, "priorities added to DB");
            }

            @Override
            public void onError(Throwable e) {
                //String msg = Resources.getSystem().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding priorities to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void getWorkOrdersWithPriorities() {
        Single<List<WorkOrderJoinPriority>> single = fiixDatabase.workOrderDao().getWorkOrdersforUserWithPriorities(username);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<WorkOrderJoinPriority>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<WorkOrderJoinPriority> workOrderPriorityList) {
                if (workOrderPriorityList.isEmpty()){
                    Log.d(TAG, "no work orders with priorities");
                } else {
                    workOrderDBMutableLiveData.postValue(workOrderPriorityList);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error finding work order with priority from DB");
            }

            //@Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void getWorkOrderDetails(FindRequest partRequest) {

    }

    @Override
    public void deleteWorkOrder(int workOrderId) {

    }


    public void dispose(){
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public IWorkOrderService getWorkOrderService() {
        return workOrderService;
    }

    public MutableLiveData<List<WorkOrderTask>> getWorkOrderTaskResponseMutableLiveData() {
        return workOrderTaskResponseMutableLiveData;
    }

    public MutableLiveData<List<WorkOrder>> getWorkOrderResponseMutableLiveData() {
        return workOrderResponseMutableLiveData;
    }

    public MutableLiveData<List<WorkOrderJoinPriority>> getWorkOrderDBMutableLiveData() {
        return workOrderDBMutableLiveData;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }


}
