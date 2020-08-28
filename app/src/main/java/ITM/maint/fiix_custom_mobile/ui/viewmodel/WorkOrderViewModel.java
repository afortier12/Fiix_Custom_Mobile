package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;
import ITM.maint.fiix_custom_mobile.utils.Status;

public class WorkOrderViewModel extends AndroidViewModel implements IWorkOrder {

    private WorkOrderRepository workOrderRepository;
    private LiveData<WorkOrderTask> taskAddLiveData;
    private LiveData<Status> responseStatus;


    public WorkOrderViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        taskAddLiveData = workOrderRepository.getTaskMutableLiveData();
        responseStatus = workOrderRepository.getStatus();

    }

    @Override
    public void addTask(WorkOrderTask task) {
        workOrderRepository.addTask(task);
    }

    public LiveData<WorkOrderTask> getTaskAddLiveData() {
        return taskAddLiveData;
    }

    public LiveData<Status> getResponseStatus() {
        return responseStatus;
    }
}
