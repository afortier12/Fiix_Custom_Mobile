package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderDetailViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderDetails {

    private WorkOrderRepository workOrderRepository;
    private LiveData<List<WorkOrderTask>> workOrderTaskResponseLiveData;
    private LiveData<Double> estTimeResponseLiveData;
    private LiveData<List<MaintenanceType>> maintenanceTypeLiveData;
    private LiveData<List<WorkOrderStatus>> workOrderStatusLiveData;
    private LiveData<String> responseStatus;


    public WorkOrderDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        workOrderTaskResponseLiveData = workOrderRepository.getWorkOrderTaskResponseMutableLiveData();
        estTimeResponseLiveData = workOrderRepository.getEstimatedTimeResponseMutableLiveData();
        maintenanceTypeLiveData = workOrderRepository.getMaintenanceTypeMutableLiveData();
        workOrderStatusLiveData = workOrderRepository.getWorkOrderStatusMutableLiveData();
        //responseStatus = workOrderRepository.getStatus();

    }

    @Override
    public void getWorkOrderEstTime(int workOrderId, int userId) {
        workOrderRepository.getEstTimefromDB(workOrderId, userId);
    }

    @Override
    public void getMaintenanceTypeDetails() {
        workOrderRepository.getMaintenanceTypesFromDb();
    }

    @Override
    public void getWorkOrderStatusDetails() {
        workOrderRepository.getWorkOrderStatusesFromDb();
    }


    public LiveData<List<WorkOrderTask>> getWorkOrderTaskResponseLiveData() {
        return workOrderTaskResponseLiveData;
    }

    public LiveData<Double> getEstTimeResponseLiveData() {
        return estTimeResponseLiveData;
    }

    public LiveData<String> getResponseStatus() {
        return responseStatus;
    }

    public LiveData<List<MaintenanceType>> getMaintenanceTypeLiveData() {
        return maintenanceTypeLiveData;
    }

    public LiveData<List<WorkOrderStatus>> getWorkOrderStatusLiveData() {
        return workOrderStatusLiveData;
    }
}
