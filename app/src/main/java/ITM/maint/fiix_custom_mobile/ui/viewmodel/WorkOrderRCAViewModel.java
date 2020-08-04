package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.CategoryJoinSource;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.SourceJoinProblemCause;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.repository.AssetRepository;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderRCAViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderRCA {

    public static final String TAG = "WorkOrderRCAViewModel";
    private AssetRepository assetRepository;
    private WorkOrderRepository workOrderRepository;
    private LiveData<List<String>> categorySourceLiveData;
    private LiveData<List<CategoryJoinSource>> sourceLiveData;
    private LiveData<List<SourceJoinProblemCause>> problemCauseLiveData;
    private LiveData<List<Action>> actionLiveData;
    private LiveData<List<Asset>> assetLiveData;
    private LiveData<List<AssetCategory>> assetCategoryLiveData;
    private LiveData<String> responseStatus;

    public WorkOrderRCAViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){

        assetRepository = new AssetRepository(this.getApplication());
        workOrderRepository = new WorkOrderRepository(this.getApplication());
        categorySourceLiveData = workOrderRepository.getFailureCodeNestingMutableLiveData();
        sourceLiveData = workOrderRepository.getSourceMutableLiveData();
        problemCauseLiveData = workOrderRepository.getProblemCauseMutableLiveData();
        actionLiveData = workOrderRepository.getActionMutableLiveData();
        assetLiveData = assetRepository.getAssetMutableLiveData();
        assetCategoryLiveData = assetRepository.getAssetCategoryListMutableLiveData();

    }

    @Override
    public void getAssets(WorkOrder workOrder) {

        List<Integer> assetIds = new ArrayList<>();
        String assetIdString = workOrder.getAssetIds().trim();
        try {
            if (assetIdString.contains(" ")) {
                assetIds = Arrays.stream(workOrder.getAssetIds().split(" "))
                        .map(s -> Integer.parseInt(s))
                        .collect(Collectors.toList());
            } else {
                assetIds.add(Integer.parseInt(assetIdString));
            }
            assetRepository.findAsset(assetIds);
        } catch (Exception e) {
            Log.d(TAG, "Error parsing asset ids: " + e.getMessage());
        }

    }

    @Override
    public void getAssetCategory(int assetCategoryId) {
        assetRepository.findAssetCategory(assetCategoryId);
    }

    @Override
    public void getCategoryList() {
        workOrderRepository.getRCACategories();
    }

    @Override
    public void getSourceList(String categoryName) {
        workOrderRepository.getSources(categoryName);
    }

    @Override
    public void getProblemsCausesList(String sourceName) {
        workOrderRepository.getSourceProblemsCauses(sourceName);
    }

    @Override
    public void getActionList() {
        workOrderRepository.getActions();
    }

    public LiveData<List<String>> getCategorySourceLiveData() {
        return categorySourceLiveData;
    }

    public LiveData<List<CategoryJoinSource>> getSourceLiveData() {
        return sourceLiveData;
    }

    public LiveData<List<SourceJoinProblemCause>> getProblemCauseLiveData() {
        return problemCauseLiveData;
    }

    public LiveData<List<Action>> getActionLiveData() {
        return actionLiveData;
    }

    public LiveData<String> getResponseStatus() {
        return responseStatus;
    }

    public LiveData<List<Asset>> getAssetLiveData() {
        return assetLiveData;
    }

    public LiveData<List<AssetCategory>> getAssetCategoryLiveData() {
        return assetCategoryLiveData;
    }
}
