package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderTaskViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderTasks{

    private WorkOrderRepository workOrderRepository;
    private IWorkOrderService workOrderService;
    private LiveData<List<WorkOrderTask>> workOrderTaskResponseLiveData;
    private LiveData<String> responseStatus;


    public WorkOrderTaskViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        workOrderService = workOrderRepository.getWorkOrderService();
        workOrderTaskResponseLiveData = workOrderRepository.getWorkOrderTaskResponseMutableLiveData();
        //responseStatus = workOrderRepository.getStatus();

    }


    @Override
    public void getWorkOrderTasks(String username, int userId, int workOrderId) {
        workOrderRepository.getWorkOrderTasks(username, userId, workOrderId);
    }

    public LiveData<List<WorkOrderTask>> getWorkOrderTaskResponseLiveData() {
        return workOrderTaskResponseLiveData;
    }

    public LiveData<String> getResponseStatus() {
        return responseStatus;
    }

    public void dispose(){

    }
}
