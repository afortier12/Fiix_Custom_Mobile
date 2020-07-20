package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderDetailViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderDetails {

    private WorkOrderRepository workOrderRepository;
    private IWorkOrderService workOrderService;
    private LiveData<List<WorkOrderTask>> workOrderTaskResponseLiveData;
    private LiveData<Double> estTimeResponseLiveData;
    private LiveData<String> responseStatus;


    public WorkOrderDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        workOrderService = workOrderRepository.getWorkOrderService();
        workOrderTaskResponseLiveData = workOrderRepository.getWorkOrderTaskResponseMutableLiveData();
        estTimeResponseLiveData = workOrderRepository.getEstimatedTimeResponseMutableLiveData();
        //responseStatus = workOrderRepository.getStatus();

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

    @Override
    public void getWorkOrderEstTime(int workOrderId, int userId) {
        workOrderRepository.getEstTimefromDB(workOrderId, userId);
    }
}
