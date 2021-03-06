package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.constants.Priorities;
import ITM.maint.fiix_custom_mobile.constants.StatusCodes;
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
import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.CategoryJoinSource;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.SourceJoinProblemCause;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

import ITM.maint.fiix_custom_mobile.utils.Status;
import ITM.maint.fiix_custom_mobile.utils.Utils;
import io.reactivex.Completable;
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

public class WorkOrderRepository extends BaseRepository implements IWorkOrderRepository.IWorkOrderList, IWorkOrderRepository.IWorkOrderDetail,
        IWorkOrderRepository.IWorkOrderRCA, IWorkOrderRepository.IWorkOrderTask {

    private static int FRESH_TIMEOUT_IN_MINUTES = 3;

    private static final String TAG = "WorkOrderRepository";
    private IWorkOrderService workOrderService;
    private MutableLiveData<List<WorkOrder>> workOrderResponseMutableLiveData;
    private MutableLiveData<List<WorkOrderTask>> workOrderTaskResponseMutableLiveData;
    private MutableLiveData<Double> estimatedTimeResponseMutableLiveData;
    private MutableLiveData<List<MaintenanceType>> maintenanceTypeMutableLiveData;
    private MutableLiveData<List<WorkOrderStatus>> workOrderStatusMutableLiveData;
    private MutableLiveData<Status> status;


    private MutableLiveData<List<String>> failureCodeNestingMutableLiveData;
    private MutableLiveData<List<RCACategorySource.CategoryJoinSource>> sourceMutableLiveData;
    private MutableLiveData<List<SourceJoinProblemCause>> problemCauseMutableLiveData;
    private MutableLiveData<List<Problem>> problemMutableLiveData;
    private MutableLiveData<List<Cause>> causeMutableLiveData;
    private MutableLiveData<List<Action>> actionMutableLiveData;
    private MutableLiveData<Problem> problemIdMutableLiveData;
    private MutableLiveData<Cause> causeIdMutableLiveData;
    private MutableLiveData<Action> actionIdMutableLiveData;
    private MutableLiveData<WorkOrderTask> taskMutableLiveData;


    private IWorkOrderDao workOrderDao;
    private ILookupTablesDao lookupTablesDao;
    private FiixDatabase fiixDatabase;
    private MutableLiveData<List<WorkOrderJoinPriority>> workOrderDBMutableLiveData;
    private ArrayList<Integer> workOrderIdList;
    private CompositeDisposable compositeDisposable;

    private String username;
    private int userId;
    private int taskWorkOrderId;

    private String taskDescription;
    private Double taskEstTime;

    public WorkOrderRepository(Application application) {
        super(application);

        workOrderResponseMutableLiveData = new MutableLiveData<List<WorkOrder>>();
        workOrderTaskResponseMutableLiveData = new MutableLiveData<List<WorkOrderTask>>();
        estimatedTimeResponseMutableLiveData = new MutableLiveData<Double>();
        maintenanceTypeMutableLiveData = new MutableLiveData<List<MaintenanceType>>();
        workOrderStatusMutableLiveData = new MutableLiveData<List<WorkOrderStatus>>();
        status = new MutableLiveData<Status>();
        workOrderService = ServiceGenerator.createService(IWorkOrderService.class);

        failureCodeNestingMutableLiveData = new MutableLiveData<List<String>>();
        sourceMutableLiveData = new MutableLiveData<List<RCACategorySource.CategoryJoinSource>>();
        problemCauseMutableLiveData = new MutableLiveData<List<SourceJoinProblemCause>>();
        actionMutableLiveData = new MutableLiveData<List<Action>>();
        problemIdMutableLiveData = new MutableLiveData<Problem>();
        causeIdMutableLiveData = new MutableLiveData<Cause>();
        actionIdMutableLiveData = new MutableLiveData<Action>();

        taskMutableLiveData = new MutableLiveData<WorkOrderTask>();

        workOrderIdList = new ArrayList<>();

        fiixDatabase = FiixDatabase.getDatabase(application);
        workOrderDao = fiixDatabase.workOrderDao();
        lookupTablesDao = fiixDatabase.lookupTablesDao();
        compositeDisposable = new CompositeDisposable();
        workOrderDBMutableLiveData = new MutableLiveData<List<WorkOrderJoinPriority>>();
    }




    public void getEstTimefromDB(int workOrderId, int userId){
        //get from database if available

        taskWorkOrderId = workOrderId;
        this.userId = userId;

        Single<Double> single = fiixDatabase.workOrderDao().getWorkOrderEstimatedTime(workOrderId);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<Double>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(Double estTime) {
                if (estTime == null) {
                    //no tasks in database -> request from Fiix
                    setupGetTasksForOrderAPICall(userId, taskWorkOrderId, true);
                } else {
                    estimatedTimeResponseMutableLiveData.postValue(estTime);
                }
            }



            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetTasksForOrderAPICall(userId, taskWorkOrderId, true);
                Log.d(TAG, "Error finding work order task from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void getWorkOrdersForUser(String username, int userId) {

        this.username = username;
        this.userId = userId;

        getPrioritiesFromDB();

    }

    private void getWorkOrdersFromDB() {
        //get from database if available
        Single<List<WorkOrder>> single = fiixDatabase.workOrderDao().getWorkOrders();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<WorkOrder>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<WorkOrder> workOrderList) {
                if (workOrderList.isEmpty()) {
                    //no work orders in database -> request from Fiix
                    setupGetTaskAPICall(username, userId, 0);
                } else {
                    getWorkOrdersWithPrioritiesFromDb();
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetTaskAPICall(username, userId, 0);
                Log.d(TAG, "Error finding work order from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void getWorkOrderTasks(String username, int userId, int workOrderId) {
        this.username = username;
        this.userId = userId;
        this.taskWorkOrderId = workOrderId;

        getWorkOrderTasksfromDB(userId, workOrderId);
    }

    private void getWorkOrderTasksfromDB(int userId, int workOrderId) {
        //get from database if available
        Single<List<WorkOrderTask>> single = fiixDatabase.workOrderDao().getWorkOrderTasks(userId, workOrderId);
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
                    //no tasks in database -> request from Fiix
                    setupGetTasksForOrderAPICall(userId, taskWorkOrderId, false);
                } else {
                   workOrderTaskResponseMutableLiveData.postValue(taskList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                Log.d(TAG, "Error finding work order task from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    public void setupGetTaskAPICall(String username, int userId, int workOrderId) {
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderTaskFields = new ArrayList<>(Arrays.asList(
                WorkOrderTasks.id.getField(),
                WorkOrderTasks.assignedToId.getField(),
                WorkOrderTasks.workOrderId.getField()
        ));

        String fields = TextUtils.join(",", workOrderTaskFields);

        List list = Stream.of(userId).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "dtmDateCompleted is null and intAssignedToUserID = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "WorkOrderTask", fields, filters);
        getAllTasksFromFiix(workOrderRequest);
    }

    private void getAllTasksFromFiix(FindRequest workOrderRequest) {
        workOrderService.getWorkOrderTasks(workOrderRequest)
                .enqueue(new Callback<List<WorkOrderTask>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrderTask>> call, Response<List<WorkOrderTask>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().isEmpty()) {
                                    workOrderResponseMutableLiveData.postValue(null);

                                } else {
                                    workOrderIdList.clear();
                                    for (WorkOrderTask workOrderTask : response.body()) {
                                        workOrderIdList.add(workOrderTask.getWorkOrderId());
                                    }
                                    setupGetWorkOrdersAPICall(workOrderIdList);
                                }
                            } else {
                                workOrderResponseMutableLiveData.postValue(null);
                                Log.d(TAG, "response is empty");
                            }
                        } else {
                            workOrderResponseMutableLiveData.postValue(null);
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages()) {
                                Status newStatus = new Status(StatusCodes.FiixError, "requestAllTasksFromFiix", msg);
                                status.postValue(newStatus);
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

    public void setupGetTasksForOrderAPICall(int userId, int workOrderId, Boolean estFlag) {
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderTaskFields = new ArrayList<>(Arrays.asList(
                WorkOrderTasks.id.getField(),
                WorkOrderTasks.description.getField(),
                WorkOrderTasks.estimatedHours.getField(),
                WorkOrderTasks.timeSpentHours.getField(),
                WorkOrderTasks.completedDate.getField(),
                WorkOrderTasks.workOrderId.getField(),
                WorkOrderTasks.assignedToId.getField()
        ));

        String fields = TextUtils.join(",", workOrderTaskFields);

        List<Integer> list = Stream.of(userId).collect(Collectors.toList());
        list.add(workOrderId);

        FindRequest.Filter filter = new FindRequest.Filter(
                    "intAssignedToUserID = ? and intWorkOrderID = ?",
                    list
            );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "WorkOrderTask", fields, filters);
        if (!estFlag)
            getTasksForWorkOrderFromFiix(workOrderRequest);
        else
            getWorkOrderEstimatedTimeFromFiix(workOrderRequest);
    }

    private void getTasksForWorkOrderFromFiix(FindRequest workOrderRequest) {
        workOrderService.getWorkOrderTasks(workOrderRequest)
                .enqueue(new Callback<List<WorkOrderTask>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrderTask>> call, Response<List<WorkOrderTask>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().isEmpty()) {
                                    workOrderTaskResponseMutableLiveData.postValue(null);

                                } else {
                                    addWorkOrderTasks(response.body());
                                    workOrderTaskResponseMutableLiveData.postValue(response.body());
                                }
                            } else {
                                workOrderTaskResponseMutableLiveData.postValue(null);
                                Log.d(TAG, "response is empty");
                            }
                        } else {
                            workOrderTaskResponseMutableLiveData.postValue(null);
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages()) {
                                Status newStatus = new Status(StatusCodes.FiixError, "requestTasksForWorkOrderFromFiix", msg);
                                status.postValue(newStatus);
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

    private void getWorkOrderEstimatedTimeFromFiix(FindRequest workOrderRequest) {
        workOrderService.getWorkOrderTasks(workOrderRequest)
                .enqueue(new Callback<List<WorkOrderTask>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrderTask>> call, Response<List<WorkOrderTask>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().isEmpty()) {
                                    estimatedTimeResponseMutableLiveData.postValue(null);

                                } else {
                                    Double estTime = 0.0;
                                    for (WorkOrderTask task :  response.body()){
                                        estTime = estTime + task.getEstimatedHours();
                                    }
                                    estimatedTimeResponseMutableLiveData.postValue(estTime);
                                }
                            } else {
                                estimatedTimeResponseMutableLiveData.postValue(null);
                                Log.d(TAG, "response is empty");
                            }
                        } else {
                            estimatedTimeResponseMutableLiveData.postValue(null);
                            APIError error = ErrorUtils.parseError(response);
                            for (String msg : error.getMessages()) {
                                Status newStatus = new Status(StatusCodes.FiixError, "requestTasksForWorkOrderFromFiix", msg);
                                status.postValue(newStatus);
                                Log.d(TAG, String.valueOf(msg));
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

    private void setupGetWorkOrdersAPICall(List<Integer> workOrders) {
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
                            if (response.body() != null) {
                                addWorkOrders(response.body());
                                //workOrderResponseMutableLiveData.postValue(response.body());
                            } else {
                                estimatedTimeResponseMutableLiveData.postValue(null);
                                Log.d(TAG, "response is empty");
                            }
                        } else {
                            estimatedTimeResponseMutableLiveData.postValue(null);
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
    public void addWorkOrderTasks(List<WorkOrderTask> taskList) {

        Completable completable = fiixDatabase.workOrderDao().insertTasks(taskList);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.work_order_task_added);
                Status newStatus = new Status(StatusCodes.addComplete, "WorkOrderTask", msg);
                status.postValue(newStatus);
                Log.d(TAG, "work order added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding work order to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void addWorkOrders(List<WorkOrder> workOrderList) {

        for (WorkOrder order : workOrderList) {
            order.setUsername(username);
        }

        Completable completable = fiixDatabase.workOrderDao().insertWorkOrders(workOrderList);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.work_orders_added);
                Status newStatus = new Status(StatusCodes.addComplete, "WorkOrder", msg);
                status.postValue(newStatus);
                getWorkOrdersWithPrioritiesFromDb();
                Log.d(TAG, "work order added to DB");
            }

            @Override
            public void onError(Throwable e) {
                workOrderDBMutableLiveData.postValue(null);
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Status newStatus = new Status(StatusCodes.addComplete, "WorkOrder", msg);
                status.postValue(newStatus);
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
                if (priorityList.isEmpty()) {
                    //no work orders in database -> request from Fiix
                    setupGetPrioritiesAPICall();
                } else {
                    getWorkOrdersFromDB();
                    //workOrderResponseMutableLiveData.postValue(workOrderList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetTaskAPICall(username, userId, 0);
                Log.d(TAG, "Error finding work order from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    public void setupGetPrioritiesAPICall() {
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> priorityFields = new ArrayList<>(Arrays.asList(
                Priorities.id.getField(),
                Priorities.order.getField(),
                Priorities.name.getField()
        ));

        String fields = TextUtils.join(",", priorityFields);

        FindRequest priorityRequest = new FindRequest("FindRequest", clientVersion, "Priority", fields, null);
        getPrioritiesFromFiix(priorityRequest);
    }

    private void getPrioritiesFromFiix(FindRequest priorityRequest) {
        workOrderService.getPriorityList(priorityRequest)
                .enqueue(new Callback<List<Priority>>() {

                    @Override
                    public void onResponse(Call<List<Priority>> call, Response<List<Priority>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().isEmpty()) {
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
                                Status newStatus = new Status(StatusCodes.addComplete, "requestPriorityFromFiix", msg);
                                status.postValue(newStatus);
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

        Completable completable = fiixDatabase.lookupTablesDao().insertPriorities(priorities);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.work_orders_added);
                Status newStatus = new Status(StatusCodes.addComplete, "Priority", msg);
                status.postValue(newStatus);
                Log.d(TAG, msg);
                getWorkOrdersFromDB();
                Log.d(TAG, "priorities added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding priorities to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void getWorkOrdersWithPrioritiesFromDb() {
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
                if (workOrderPriorityList.isEmpty()) {
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
    public void getMaintenanceTypesFromDb() {
        Single<List<MaintenanceType>> single = fiixDatabase.lookupTablesDao().getMaintenanceType();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<MaintenanceType>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<MaintenanceType> typeList) {
                if (typeList.isEmpty()) {
                    //no maintenance types in database -> request from Fiix
                    Log.d(TAG, "No maintenance types in DB");

                } else {
                    maintenanceTypeMutableLiveData.postValue(typeList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                Log.d(TAG, "Error finding maintenance types from DB");
            }

        });
    }

    @Override
    public void getWorkOrderStatusesFromDb() {
        Single<List<WorkOrderStatus>> single = fiixDatabase.lookupTablesDao().getWorkOrderStatus();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<WorkOrderStatus>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<WorkOrderStatus> typeList) {
                if (typeList.isEmpty()) {
                    //no maintenance types in database -> request from Fiix
                    Log.d(TAG, "No work order statuses in DB");

                } else {
                    workOrderStatusMutableLiveData.postValue(typeList);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                Log.d(TAG, "Error finding work order statuses from DB");
            }
        });
    }




    @Override
    public void updateWorkOrder(WorkOrder workOrder) {

    }

    @Override
    public void deleteWorkOrder(int workOrderId) {

    }

    @Override
    public void updateWorkOrders(List<WorkOrder> workOrders) {
        Completable completable = fiixDatabase.workOrderDao().updateWorkOrders(workOrders);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "work orders updated in DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error updating work orders in DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void updateTask(WorkOrderTask task) {
        Completable completable = fiixDatabase.workOrderDao().updateTask(task);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "task updated in DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error updating task in DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void addTask(WorkOrderTask task) {

        Completable completable = fiixDatabase.workOrderDao().insertTask(task);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.task_added);
                Status newStatus = new Status(StatusCodes.addComplete, "Task", msg);
                status.postValue(newStatus);
                Log.d(TAG, msg);
                Log.d(TAG, "tasks added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding tasks to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    @Override
    public void deleteTask(WorkOrderTask task) {

        Completable completable = fiixDatabase.workOrderDao().deleteTask(task);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.task_deleted);
                Status newStatus = new Status(StatusCodes.deleteComplete, "Task", msg);
                status.postValue(newStatus);
                Log.d(TAG, msg);
                getWorkOrdersFromDB();
                Log.d(TAG, "tasks deleted to DB");
            }

            @Override
            public void onError(Throwable e) {
                //String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error deleting tasks to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);

    }

    @Override
    public void updateTasks(List<WorkOrderTask> taskList) {

        Completable completable = fiixDatabase.workOrderDao().updateTasks(taskList);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "work order tasks added to DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error adding work order tasks to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);

    }

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    public void dispose() {
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

    public MutableLiveData<Double> getEstimatedTimeResponseMutableLiveData() {
        return estimatedTimeResponseMutableLiveData;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }

    public MutableLiveData<List<MaintenanceType>> getMaintenanceTypeMutableLiveData() {
        return maintenanceTypeMutableLiveData;
    }

    public MutableLiveData<List<WorkOrderStatus>> getWorkOrderStatusMutableLiveData() {
        return workOrderStatusMutableLiveData;
    }

    @Override
    public void getRCACategories() {

        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        Single single = fiixDatabase.rcaDao().getCategories();
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<String>>() {

            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<String> failureCodeNestings) {
                failureCodeNestingMutableLiveData.postValue(failureCodeNestings);
                Log.d(TAG, "RCA nesting added to DB");
            }

            @Override
            public void onError(Throwable e) {
                failureCodeNestingMutableLiveData.postValue(null);
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "Error adding RCA nesting to DB");
            }
        });

    }

    @Override
    public void getSources(String categoryName) {
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        Single single = fiixDatabase.rcaDao().getSourcesForCategory(categoryName);
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<CategoryJoinSource>>() {

            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<RCACategorySource.CategoryJoinSource> sources) {
                sourceMutableLiveData.postValue(sources);
                Log.d(TAG, "RCA nesting added to DB");
            }

            @Override
            public void onError(Throwable e) {
                sourceMutableLiveData.postValue(null);
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "Error adding RCA nesting to DB");
            }
        });
    }

    @Override
    public void getSourceProblemsCauses(String sourceName) {
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        Single single = fiixDatabase.rcaDao().getProblemsCausesForSource(sourceName);
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<SourceJoinProblemCause>>() {

            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<SourceJoinProblemCause> problemsCauses) {
                problemCauseMutableLiveData.postValue(problemsCauses);
                Log.d(TAG, "RCA nesting added to DB");
            }

            @Override
            public void onError(Throwable e) {
                sourceMutableLiveData.postValue(null);
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "Error adding RCA nesting to DB");
            }
        });
    }

    @Override
    public void getActions() {
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        Single single = fiixDatabase.rcaDao().getActions();
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<Action>>() {

            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<Action> actions) {
                actionMutableLiveData.postValue(actions);
                Log.d(TAG, "RCA nesting added to DB");
            }

            @Override
            public void onError(Throwable e) {
                actionMutableLiveData.postValue(null);
                Log.d(TAG, e.getMessage());
                Log.d(TAG, "Error adding RCA nesting to DB");
            }
        });
    }

    public MutableLiveData<List<String>> getFailureCodeNestingMutableLiveData() {
        return failureCodeNestingMutableLiveData;
    }

    public MutableLiveData<List<CategoryJoinSource>> getSourceMutableLiveData() {
        return sourceMutableLiveData;
    }

    public MutableLiveData<List<SourceJoinProblemCause>> getProblemCauseMutableLiveData() {
        return problemCauseMutableLiveData;
    }


    public MutableLiveData<List<Action>> getActionMutableLiveData() {
        return actionMutableLiveData;
    }

    public MutableLiveData<Action> getActionIdMutableLiveData() {
        return actionIdMutableLiveData;
    }

    public MutableLiveData<Problem> getProblemMutableLiveData() {
        return problemIdMutableLiveData;
    }

    public MutableLiveData<Cause> getCauseMutableLiveData() {
        return causeIdMutableLiveData;
    }

    @Override
    public void addTaskToDatabase(String description, String estTime, int userId, int workOrderId) {

        taskDescription = description;

        WorkOrderTask task = new WorkOrderTask();
        task.setDescription(taskDescription);
        task.setAssignedToId(userId);
        task.setWorkOrderId(workOrderId);
        task.setUserCreated(1);

        String[] estTimeList = String.valueOf(estTime).split(":");
        String estTimeValue;
        if (estTimeList.length == 2 ) {
            estTimeValue = estTimeList[0] + "." + estTimeList[1];
        } else {
            estTimeValue = "0.0";
        }

        taskEstTime = (Double.parseDouble(estTimeValue));
        task.setEstimatedHours(taskEstTime);

        Completable completable = workOrderDao.insertTask (task);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.work_order_task_added);
                Status newStatus = new Status(StatusCodes.addComplete, "Task", msg);
                status.postValue(newStatus);

                WorkOrderTask task = new WorkOrderTask();
                task.setDescription(taskDescription);
                task.setEstimatedHours(taskEstTime);
                taskMutableLiveData.postValue(task);
                Log.d(TAG, msg);
                Log.d(TAG, "task added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_task_add_error);
                Status newStatus = new Status(StatusCodes.addError, "Task", msg);
                status.postValue(newStatus);
                taskMutableLiveData.postValue(null);
                Log.d(TAG, "Error adding task to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);

    }

    public MutableLiveData<WorkOrderTask> getTaskMutableLiveData() {
        return taskMutableLiveData;
    }
}
