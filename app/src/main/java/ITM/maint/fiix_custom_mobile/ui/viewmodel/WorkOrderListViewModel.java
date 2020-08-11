package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;
import ITM.maint.fiix_custom_mobile.data.repository.AssetRepository;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;
import ITM.maint.fiix_custom_mobile.utils.Status;

public class WorkOrderListViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderList {

    private WorkOrderRepository workOrderRepository;
    private AssetRepository assetRepository;

    private LiveData<List<WorkOrder>> workOrderResponseLiveData;
    private LiveData<List<WorkOrderJoinPriority>> workOrderDBLiveData;
    private LiveData<List<Asset.AssetDepartmentPlant>> deptPlantResponseLiveData;
    private LiveData<List<Asset>> assetResponseLiveData;
    private LiveData<Status> responseStatus;

    public WorkOrderListViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        assetRepository = new AssetRepository(this.getApplication());
        workOrderResponseLiveData = workOrderRepository.getWorkOrderResponseMutableLiveData();;
        workOrderDBLiveData = workOrderRepository.getWorkOrderDBMutableLiveData();
        assetResponseLiveData = assetRepository.getAssetMutableLiveData();
        deptPlantResponseLiveData = assetRepository.getDeptPlantMutableLiveData();
        responseStatus = workOrderRepository.getStatus();
    }

    @Override
    public void getWorkOrders(String username, int userId) {
        workOrderRepository.getWorkOrdersForUser(username, userId);
    }

    @Override
    public void findWorkOrderTasks(String username, int userId, int workOrderId) {
        //workOrderRepository.getWorkOrderTasks(username, userId, workOrderId);
    }

    @Override
    public void getWorkOrderDetails(FindRequest.Filter filter) {

    }

    @Override
    public void changeWorkOrderStatus(ChangeRequest changeRequest) {

    }

    @Override
    public void updateTask(ChangeRequest changeRequest) {

    }

    @Override
    public void getDepartmentsPlants(List<Integer> assetIds) {
        assetRepository.getAssetDepartmentPlant(assetIds);
    }


    public LiveData<List<WorkOrder>> getWorkOrderResponseLiveData() {
        return workOrderResponseLiveData;
    }


    public LiveData<List<WorkOrderJoinPriority>> getWorkOrderDBLiveData() {
        return workOrderDBLiveData;
    }

    public LiveData<Status> getResponseStatus() {
        return responseStatus;
    }

    public LiveData<List<Asset.AssetDepartmentPlant>> getDeptPlantResponseLiveData() {
        return deptPlantResponseLiveData;
    }

    public LiveData<List<Asset>> getAssetResponseLiveData() {
        return assetResponseLiveData;
    }

    public void dispose(){
        workOrderRepository.dispose();
    }
}