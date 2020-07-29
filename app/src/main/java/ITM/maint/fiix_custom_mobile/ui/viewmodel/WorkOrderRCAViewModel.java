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
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.AssetRepository;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderRCAViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderRCA {

    private AssetRepository assetRepository;
    private WorkOrderRepository workOrderRepository;
    private LiveData<List<String>> failureCodeNestingLiveData;
    private LiveData<List<String>> sourceLiveData;
    private LiveData<List<String>>problemLiveData;
    private LiveData<List<String>> causeLiveData;
    private LiveData<List<String>> actionLiveData;
    private LiveData<String> responseStatus;

    public WorkOrderRCAViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){

        assetRepository = new AssetRepository(this.getApplication());
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        failureCodeNestingLiveData = workOrderRepository.getFailureCodeNestingMutableLiveData();
        sourceLiveData = workOrderRepository.getSourceMutableLiveData();
        problemLiveData = workOrderRepository.getProblemMutableLiveData();
        causeLiveData = workOrderRepository.getCauseMutableLiveData();
        actionLiveData = workOrderRepository.getActionMutableLiveData();

    }

    @Override
    public void getCategoryList() {
        workOrderRepository.getCategories();
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

    public LiveData<List<String>> getFailureCodeNestingLiveData() {
        return failureCodeNestingLiveData;
    }

    public LiveData<List<String>> getSourceLiveData() {
        return sourceLiveData;
    }

    public LiveData<List<String>> getProblemLiveData() {
        return problemLiveData;
    }

    public LiveData<List<String>> getCauseLiveData() {
        return causeLiveData;
    }

    public LiveData<List<String>> getActionLiveData() {
        return actionLiveData;
    }

    public LiveData<String> getResponseStatus() {
        return responseStatus;
    }
}
