package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderViewModel extends AndroidViewModel implements IWorkOrder{

    private WorkOrderRepository workOrderRepository;
    private IWorkOrderService workOrderService;
    private LiveData<List<WorkOrder>> workOrderResponseLiveData;
    private LiveData<WorkOrder> workOrderDBLiveData;
    private LiveData<String> responseStatus;

    public WorkOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        workOrderService = workOrderRepository.getWorkOrderService();
        workOrderResponseLiveData = workOrderRepository.getWorkOrderResponseMutableLiveData();;
        workOrderDBLiveData = workOrderRepository.getWorkOrderDBMutableLiveData();
        responseStatus = workOrderRepository.getStatus();
    }

    @Override
    public void findWorkOrderTasks(String username, int userId, int workOrderId) {
        workOrderRepository.getWorkOrderTasks(username, userId, workOrderId);
    }

    @Override
    public void findWorkOrders(List<Integer> ids) {
        workOrderRepository.getWorkOrders(ids);
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


    public LiveData<List<WorkOrder>> getWorkOrderResponseLiveData() {
        return workOrderResponseLiveData;
    }


    public LiveData<WorkOrder> getWorkOrderDBLiveData() {
        return workOrderDBLiveData;
    }

    public LiveData<String> getResponseStatus() {
        return responseStatus;
    }

    public void dispose(){
        workOrderRepository.dispose();
    }
}