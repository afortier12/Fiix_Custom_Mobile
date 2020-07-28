package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderRCAViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderRCA {

    private WorkOrderRepository workOrderRepository;
    private LiveData<List<FailureCodeNesting>> failureCodeNestingLiveData;
    private LiveData<List<FailureCodeNesting.Source>> sourceLiveData;
    private LiveData<List<Problem>>problemLiveData;
    private LiveData<List<Cause>> causeLiveData;
    private LiveData<List<Action>> actionLiveData;
    private LiveData<String> responseStatus;

    public WorkOrderRCAViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        failureCodeNestingLiveData = workOrderRepository.getFailureCodeNestingMutableLiveData();
        sourceLiveData = workOrderRepository.getSourceMutableLiveData();
        problemLiveData = workOrderRepository.getProblemMutableLiveData();
        causeLiveData = workOrderRepository.getCauseMutableLiveData();
        actionLiveData = workOrderRepository.getActionMutableLiveData();

    }

    @Override
    public void getCategoryList() {

    }

    @Override
    public void getSourceList(int categoryId) {
        workOrderRepository.getSources(categoryId);
    }

    @Override
    public void getProblemList(int sourceId) {
        workOrderRepository.getProblems(sourceId);
    }

    @Override
    public void getCauseList(int sourceId) {
        workOrderRepository.getCauses(sourceId);
    }

    @Override
    public void getActionList() {
        workOrderRepository.getActions();
    }

    public LiveData<List<FailureCodeNesting>> getFailureCodeNestingLiveData() {
        return failureCodeNestingLiveData;
    }

    public LiveData<List<FailureCodeNesting.Source>> getSourceLiveData() {
        return sourceLiveData;
    }

    public LiveData<List<Problem>> getProblemLiveData() {
        return problemLiveData;
    }

    public LiveData<List<Cause>> getCauseLiveData() {
        return causeLiveData;
    }

    public LiveData<List<Action>> getActionLiveData() {
        return actionLiveData;
    }

    public LiveData<String> getResponseStatus() {
        return responseStatus;
    }
}
