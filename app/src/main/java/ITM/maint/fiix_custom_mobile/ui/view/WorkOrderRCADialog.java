package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting.FailureCodeNestingJoinSource;
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;
import ITM.maint.fiix_custom_mobile.ui.adapter.RCAAutoCompleteTextView;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderRCAViewModel;

public class WorkOrderRCADialog extends DialogFragment  {

    public static final String TAG = "WorkOrderRCADialog";
    private WorkOrderRepository workOrderRepository;
    private WorkOrderRCAViewModel viewModel;
    private WorkOrder workOrder;
    private String category;
    private String source;

    private List<String> categoryList;
    private List<String> sourceList;
    private List<String> problemCauseList;
    private List<String> problemList;
    private List<String> causeList;
    private List<Asset> assetList;

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> sourceAdapter;

    private TextInputLayout layoutCategory;
    private AutoCompleteTextView txtCategory;
    private TextInputLayout layoutSource;
    private AutoCompleteTextView txtSource;
    private TextInputLayout layoutProblem;
    private AutoCompleteTextView txtProblem;
    private TextInputLayout layoutCause;
    private AutoCompleteTextView txtCause;
    private TextInputLayout layoutAction;
    private AutoCompleteTextView txtAction;


    public interface OnRCAListener {
        void sendRCA(String category, String source, String problem, String cause, String action);
    }

    public OnRCAListener onRCAListener;

    public WorkOrderRCADialog(WorkOrder workOrder, String category, String source) {
        this.workOrder = workOrder;
        categoryList = new ArrayList<>();
        assetList = new ArrayList<>();
        this.category = category;
        this.source = source;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height =ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        categoryList = new ArrayList<>();
        sourceList = new ArrayList<>();

        View view = inflater.inflate(R.layout.dialog_work_order_rca, container, false);

        viewModel = new ViewModelProvider(this).get(WorkOrderRCAViewModel.class);
        viewModel.init();

        liveDataListeners();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutCategory = (TextInputLayout) view.findViewById(R.id.detail_problem_category_layout);
        txtCategory = (AutoCompleteTextView) view.findViewById(R.id.detail_problem_category_dropdown);
        categoryAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.rca_list_item, categoryList);
        txtCategory.setAdapter(categoryAdapter);
        txtCategory.setThreshold(1);
        txtCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Category selected = " + category);
            }
        });

        layoutSource = (TextInputLayout) view.findViewById(R.id.detail_problem_source_layout);
        txtSource = (AutoCompleteTextView) view.findViewById(R.id.detail_problem_source_dropdown);
        sourceAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.rca_list_item, sourceList);
        txtSource.setAdapter(sourceAdapter);
        txtSource.setThreshold(1);
        txtSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Source selected = " + category);
            }
        });

        viewModel.getCategoryList();

    }

    private void liveDataListeners(){

        viewModel.getAssetLiveData().observe(getViewLifecycleOwner(), new Observer<List<Asset>>() {
            @Override
            public void onChanged(List<Asset> assets) {
                assetList.clear();
                for (Asset asset:assets){
                    assetList.add(asset);
                }
                if (assetList.size() > 0) {
                    try {
                        viewModel.getAssetCategory(Integer.parseInt(assetList.get(0).getCategoryId()));
                    } catch (Exception e){
                        Log.d(TAG, "Error getting asset category " + e.getMessage());
                    }
                }
            }
        });

        viewModel.getAssetCategoryLiveData().observe(getViewLifecycleOwner(), new Observer<List<AssetCategory>>() {
            @Override
            public void onChanged(List<AssetCategory> assetCategoryList) {
                for (AssetCategory assetCategory: assetCategoryList) {
                    int position = categoryAdapter.getPosition(assetCategory.getName());
                    if (position >= 0) {
                        category = assetCategory.getName();
                        txtCategory.setText(category);
                        txtCategory.dismissDropDown();
                        break;
                    }
                }
                if (source.length() == 0 && category.length() > 0)
                    viewModel.getSourceList(category);
            }
        });

        viewModel.getFailureCodeNestingLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> failureCodeNestings) {
                categoryAdapter.clear();
                int position = 0;
                for (String category: failureCodeNestings) {
                    categoryAdapter.add(category);

                }
                categoryAdapter.notifyDataSetChanged();
                txtCategory.showDropDown();
                if (category.length() == 0)
                    viewModel.getAssets(workOrder);
            }
        });

        viewModel.getSourceLiveData().observe(getViewLifecycleOwner(), new Observer<List<FailureCodeNestingJoinSource>>() {
            @Override
            public void onChanged(List<FailureCodeNestingJoinSource> nestedSources) {
                sourceAdapter.clear();
                for (FailureCodeNestingJoinSource nestedSource: nestedSources) {
                    for(Source source: nestedSource.getSourceList())
                        sourceAdapter.add(source.getName());
                }
                sourceAdapter.notifyDataSetChanged();
                if (source.length() == 0){
                    int position = sourceAdapter.getPosition(category);
                    if (position >= 0) {
                        source = category;
                        txtSource.setText(source);
                        txtSource.dismissDropDown();
                    }
                } else {
                    txtSource.showDropDown();
                }
                if (workOrder.getProblemID() == 0){
                    //viewModel.getProblemList(source);
                }

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onRCAListener = (OnRCAListener) getTargetFragment();
            Log.d(TAG, "onAttach: " + onRCAListener);
        } catch (ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }


}
