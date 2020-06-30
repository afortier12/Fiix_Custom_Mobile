package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.WorkOrderTasks;
import ITM.maint.fiix_custom_mobile.constants.WorkOrders;
import ITM.maint.fiix_custom_mobile.data.api.ErrorUtils;
import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IUserDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrderRepository extends BaseRepository implements IWorkOrderRepository{

    private static final String TAG ="WorkOrderRepository";
    private IWorkOrderService workOrderService;
    private MutableLiveData<List<WorkOrder>> workOrderResponseMutableLiveData;
    private MutableLiveData<List<WorkOrderTask>> workOrderTaskResponseMutableLiveData;
    private MutableLiveData<String> status;

    private ITM.maint.fiix_custom_mobile.data.model.dao.IWorkOrderDao IWorkOrderDao;
    private FiixDatabase fiixDatabase;
    private MutableLiveData<WorkOrder> workOrderDBMutableLiveData;
    private ArrayList<Integer> workOrderIdList;


    public WorkOrderRepository(Application application) {
        super(application);

        workOrderResponseMutableLiveData = new MutableLiveData<List<WorkOrder>>();
        workOrderTaskResponseMutableLiveData = new MutableLiveData<List<WorkOrderTask>>();
        status = new MutableLiveData<String>();
        workOrderService = ServiceGenerator.createService(IWorkOrderService.class);

        workOrderIdList = new ArrayList<>();

        //fiixDatabase = FiixDatabase.getDatabase(application);
        //IWorkOrderDao workOrderDao= fiixDatabase
    }

    @Override
    public void getWorkOrderTasks(String username, int userId, int workOrderId) {
        //get from database if available

        //else
        getTasksFromFiix(username, userId, workOrderId);
    }

    @Override
    public void getWorkOrders(List<Integer> workOrders) {
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> workOrderFields = new ArrayList<>(Arrays.asList(
                WorkOrders.code.getField(),
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
        String parameterList = TextUtils.join(",",parameters);

        FindRequest.Filter filter = new FindRequest.Filter(
                "dtmDateCompleted is null and id in (" + placeHolderList + ")",
                Collections.singletonList(parameterList)
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "WorkOrder", fields, filters);
        requestWorkOrdersFromFiix(workOrderRequest);


    }

    @Override
    public void getWorkOrderDetails(FindRequest partRequest) {

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

    private void requestWorkOrdersFromFiix(FindRequest workOrderRequest){
        workOrderService.getWorkOrderList(workOrderRequest)
                .enqueue(new Callback<List<WorkOrder>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrder>> call, Response<List<WorkOrder>> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                workOrderResponseMutableLiveData.postValue(response.body());
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

    public MutableLiveData<WorkOrder> getWorkOrderDBMutableLiveData() {
        return workOrderDBMutableLiveData;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }
}
