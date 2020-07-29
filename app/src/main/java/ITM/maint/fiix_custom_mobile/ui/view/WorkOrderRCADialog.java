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
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.repository.AssetRepository;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;
import ITM.maint.fiix_custom_mobile.ui.adapter.RCAAutoCompleteTextView;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderRCAViewModel;

public class WorkOrderRCADialog extends DialogFragment  {

    public static final String TAG = "WorkOrderRCADialog";
    private WorkOrderRepository workOrderRepository;
    private WorkOrderRCAViewModel viewModel;
    private WorkOrder workOrder;

    private List<String> categoryList;
    private List<String> sourceList;
    private List<String> problemCauseList;
    private List<String> problemList;
    private List<String> causeList;

    private ArrayAdapter<String> categoryAdapter;

    private TextInputLayout layoutCategory;
    private RCAAutoCompleteTextView txtCategory;
    private TextInputLayout layoutSource;
    private AutoCompleteTextView txtSource;
    private TextInputLayout layoutProblem;
    private AutoCompleteTextView txtProblem;
    private TextInputLayout layoutCause;
    private AutoCompleteTextView txtCause;
    private TextInputLayout layoutAction;
    private AutoCompleteTextView txtAction;

    public WorkOrderRCADialog(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }


    public interface OnRCAListener {
        void sendRCA(String category, String source, String problem, String cause, String action);
    }

    public OnRCAListener onRCAListener;

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

        View view = inflater.inflate(R.layout.fragment_work_order_rca, container, false);

        viewModel = new ViewModelProvider(this).get(WorkOrderRCAViewModel.class);
        viewModel.init();

        viewModel.getFailureCodeNestingLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> failureCodeNestings) {
                categoryAdapter.clear();
                int position = 0;
                for (String category: failureCodeNestings) {
                    categoryAdapter.add(category);

                    /*get asset type and set selection to current
                    work order asset type

                        categoryAdapter.getPosition(workOrder.get)
                        position++;

                     */
                }
                categoryAdapter.notifyDataSetChanged();
                txtCategory.showDropDown();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutCategory = (TextInputLayout) view.findViewById(R.id.detail_problem_category_layout);
        txtCategory = (RCAAutoCompleteTextView) view.findViewById(R.id.detail_problem_category_dropdown);
        txtCategory.setShowAlways(true);
        categoryAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.rca_list_item, categoryList);
        txtCategory.setAdapter(categoryAdapter);
        txtCategory.setThreshold(1);
        txtCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category =  (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Category selected = " + category);
            }
        });

        layoutSource = (TextInputLayout) view.findViewById(R.id.detail_problem_source_layout);
        txtSource = (AutoCompleteTextView) view.findViewById(R.id.detail_problem_source_dropdown);

        viewModel.getCategoryList();
        viewModel.getSourceList();

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
