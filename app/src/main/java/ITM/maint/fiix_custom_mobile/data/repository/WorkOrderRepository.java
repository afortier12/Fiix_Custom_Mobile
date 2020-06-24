package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.constants.WorkOrderTasks;
import ITM.maint.fiix_custom_mobile.constants.WorkOrders;
import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.IWorkOrder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkOrderRepository extends BaseRepository implements IWorkOrderRepository{

    private static final String TAG ="WorkOrderRepository";
    private IWorkOrderService workOrderService;
    private MutableLiveData<List<WorkOrder>> workOrderResponseMutableLiveData;
    private MutableLiveData<List<WorkOrderTask>> workOrderTaskResponseMutableLiveDate;

    private ITM.maint.fiix_custom_mobile.data.model.dao.IWorkOrderDao IWorkOrderDao;
    private FiixDatabase fiixDatabase;
    private MutableLiveData<WorkOrder> workOrderDBMutableLiveData;


    public WorkOrderRepository(Application application) {
        super(application);
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
        for (Integer id: workOrders){
            placeHolders.add("?");
        }
        String placeHolderList = TextUtils.join(",", placeHolders);

        List list = Stream.of(workOrders).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "id in (" + placeHolderList + ")",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, filters);
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
                "intAssignedToUserID = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest workOrderRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, filters);
        requestTasksFromFiix(workOrderRequest);
    }

    private void requestTasksFromFiix(FindRequest workOrderRequest){
        workOrderService.getWorkOrderTasks(workOrderRequest)
                .enqueue(new Callback<List<WorkOrderTask>>() {

                    @Override
                    public void onResponse(Call<List<WorkOrderTask>> call, Response<List<WorkOrderTask>> response) {
                        if (response.body() != null) {
                            workOrderTaskResponseMutableLiveDate.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<WorkOrderTask>> call, Throwable t) {
                        workOrderTaskResponseMutableLiveDate.postValue(null);

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

    public MutableLiveData<List<WorkOrderTask>> getWorkOrderTaskResponseMutableLiveDate() {
        return workOrderTaskResponseMutableLiveDate;
    }

    public MutableLiveData<List<WorkOrder>> getWorkOrderResponseMutableLiveData() {
        return workOrderResponseMutableLiveData;
    }

    public MutableLiveData<WorkOrder> getWorkOrderDBMutableLiveData() {
        return workOrderDBMutableLiveData;
    }


}
