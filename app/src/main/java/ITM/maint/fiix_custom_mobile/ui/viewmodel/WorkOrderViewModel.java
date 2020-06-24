package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.PartRepository;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderViewModel extends AndroidViewModel implements IWorkOrder{

    private WorkOrderRepository workOrderRepository;
    private IWorkOrderService workOrderService;
    private LiveData<List<WorkOrder>> workOrderResponseLiveData;
    private LiveData<List<WorkOrderTask>> workOrderTaskResponseLiveData;
    private LiveData<WorkOrder> workOrderDBLiveData;

    public WorkOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        workOrderService = workOrderRepository.getWorkOrderService();
        workOrderResponseLiveData = workOrderRepository.getWorkOrderResponseMutableLiveData();
        workOrderTaskResponseLiveData = workOrderRepository.getWorkOrderTaskResponseMutableLiveDate();
        workOrderDBLiveData = workOrderRepository.getWorkOrderDBMutableLiveData();
    }

    @Override
    public void findWorkOrderTasks(String username, int userId, int workOrderId) {
        workOrderRepository.getWorkOrderTasks(username, userId, workOrderId);
    }

    @Override
    public void findWorkOrders(List<Integer> ids) {
        workOrderRepository.getWorkOrders();
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

    public LiveData<List<WorkOrderTask>> getWorkOrderTaskResponseLiveData() {
        return workOrderTaskResponseLiveData;
    }

    public LiveData<WorkOrder> getWorkOrderDBLiveData() {
        return workOrderDBLiveData;
    }

    public void dispose(){
        workOrderRepository.dispose();
    }
}