package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.SourceJoinProblemCause;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;
import ITM.maint.fiix_custom_mobile.ui.adapter.MaterialDropDownAdapter;
import ITM.maint.fiix_custom_mobile.ui.adapter.RCAAutoCompleteTextView;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderRCAViewModel;

public class WorkOrderRCADialog extends DialogFragment  {

    public static final String TAG = "WorkOrderRCADialog";
    private WorkOrderRepository workOrderRepository;
    private WorkOrderRCAViewModel viewModel;
    private WorkOrder workOrder;
    private String category;
    private String source;
    private int categoryLastPosition;
    private int sourceLastPosition;

    private List<String> categoryList;
    private List<String> sourceList;
    private List<String> actionList;
    private List<String> problemList;
    private List<String> causeList;
    private List<Asset> assetList;

    private MaterialDropDownAdapter<String> categoryAdapter;
    private MaterialDropDownAdapter<String> sourceAdapter;
    private MaterialDropDownAdapter<String> problemAdapter;
    private MaterialDropDownAdapter<String> causeAdapter;
    private MaterialDropDownAdapter<String> actionAdapter;

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
    private MaterialButton update;
    private MaterialButton cancel;

    private HashMap<Integer, Integer> problemHashMap;
    private HashMap<Integer, Integer> causeHashMap;
    private HashMap<Integer, Integer> actionHashmap;

    private int selectedProblemId;
    private int selectedCauseId;
    private int selectedActionId;


    public interface OnRCAListener {
        void sendRCA(String category, String source, int problem, int cause, int action);
    }

    public OnRCAListener onRCAListener;

    public WorkOrderRCADialog(WorkOrder workOrder, String category, String source) {
        this.workOrder = workOrder;
        categoryList = new ArrayList<>();
        sourceList = new ArrayList<>();
        assetList = new ArrayList<>();
        problemList = new ArrayList<>();
        causeList = new ArrayList<>();
        actionList = new ArrayList<>();
        this.category = category;
        this.source = source;
        categoryLastPosition = -1;
        sourceLastPosition = -1;

        problemHashMap = new HashMap<Integer, Integer>();
        causeHashMap = new HashMap<Integer, Integer> ();
        actionHashmap = new HashMap<Integer, Integer> ();
        selectedProblemId = workOrder.getProblemID();
        selectedCauseId = workOrder.getCauseID();
        selectedActionId = workOrder.getActionID();
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
        txtCategory = (RCAAutoCompleteTextView) view.findViewById(R.id.detail_problem_category_dropdown);
        txtCategory.setText(category,false);
        categoryAdapter = new MaterialDropDownAdapter<String>(this.getContext(), R.layout.rca_list_item, categoryList);
        txtCategory.setAdapter(categoryAdapter);
        txtCategory.setThreshold(1);
        txtCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutCategory.setErrorEnabled(false);
                if (categoryLastPosition != position){
                    if (position >= 0) {
                        category = categoryList.get(position);
                        txtCategory.dismissDropDown();
                        txtSource.clearListSelection();
                        sourceAdapter.clear();
                        source = "";
                        txtSource.setText("", false);
                        txtProblem.setText("", false);
                        txtCause.setText("", false);
                        viewModel.getSourceList(category);
                    }
                }
                categoryLastPosition = position;
                String category = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "Category selected = " + category);
            }
        });

        layoutSource = (TextInputLayout) view.findViewById(R.id.detail_problem_source_layout);
        txtSource = (AutoCompleteTextView) view.findViewById(R.id.detail_problem_source_dropdown);
        txtCategory.setText(category,false);
        sourceAdapter = new MaterialDropDownAdapter<String>(this.getContext(), R.layout.rca_list_item, sourceList);
        txtSource.setAdapter(sourceAdapter);
        txtSource.setThreshold(1);
        txtSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutSource.setErrorEnabled(false);
                if (sourceLastPosition != position){
                    problemAdapter.clear();
                    causeAdapter.clear();
                    if (position >= 0) {
                        source = sourceList.get(position);
                        txtSource.dismissDropDown();
                        txtProblem.setText("", false);
                        txtCause.setText("", false);
                    }
                }
                sourceLastPosition = position;
                String category = (String) parent.getItemAtPosition(position);
                viewModel.getProblemsCausesList(source);
                Log.d(TAG, "Source selected = " + category);
            }
        });

        layoutProblem = (TextInputLayout) view.findViewById(R.id.detail_problem_layout);
        txtProblem = (AutoCompleteTextView) view.findViewById(R.id.detail_problem_dropdown);
        problemAdapter = new MaterialDropDownAdapter<String>(this.getContext(), R.layout.rca_list_item, problemList);
        txtProblem.setAdapter(problemAdapter);
        txtProblem.setThreshold(1);
        txtProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutProblem.setErrorEnabled(false);
                selectedProblemId = problemHashMap.get(position);
            }
        });

        layoutCause = (TextInputLayout) view.findViewById(R.id.detail_cause_layout);
        txtCause = (AutoCompleteTextView) view.findViewById(R.id.detail_cause_dropdown);
        causeAdapter = new MaterialDropDownAdapter<String>(this.getContext(), R.layout.rca_list_item, causeList);
        txtCause.setAdapter(causeAdapter);
        txtCause.setThreshold(1);
        txtCause.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutCause.setErrorEnabled(false);
                selectedCauseId = causeHashMap.get(position);
            }
        });

        layoutAction = (TextInputLayout) view.findViewById(R.id.detail_action_layout);
        txtAction = (AutoCompleteTextView) view.findViewById(R.id.detail_action_dropdown);
        actionAdapter = new MaterialDropDownAdapter<String>(this.getContext(), R.layout.rca_list_item, actionList);
        txtAction.setAdapter(actionAdapter);
        txtAction.setThreshold(1);
        txtAction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                layoutAction.setErrorEnabled(false);
                selectedActionId = actionHashmap.get(position);
            }
        });

        update = (MaterialButton) view.findViewById(R.id.rca_update_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = txtCategory.getEditableText().toString();
                String source = txtSource.getEditableText().toString();
                String problem = txtProblem.getEditableText().toString();
                String cause = txtCause.getEditableText().toString();
                String action = txtAction.getEditableText().toString();

                if(category.length() == 0) {
                    Snackbar.make(getView(), "Missing category selection!", Snackbar.LENGTH_LONG).show();
                    layoutCategory.setError("Category is required");
                }
                if(source.length() == 0){
                    Snackbar.make(getView(), "Missing source selection!", Snackbar.LENGTH_LONG).show();
                    layoutSource.setError("Source is required");
                }
                if(problem.length() == 0){
                    Snackbar.make(getView(), "Missing problem selection!", Snackbar.LENGTH_LONG).show();
                    layoutProblem.setError("Problem is required");
                }
                if(cause.length() == 0){
                    Snackbar.make(getView(), "Missing cause selection!", Snackbar.LENGTH_LONG).show();
                    layoutCause.setError("Cause is required");
                }
                if(action.length() == 0){
                    Snackbar.make(getView(), "Missing action selection!", Snackbar.LENGTH_LONG).show();
                    layoutAction.setError("Action is required");
                }

                onRCAListener.sendRCA(category, source, selectedProblemId, selectedCauseId, selectedActionId);
                dismissFragment();
            }
        });

        cancel = (MaterialButton) view.findViewById(R.id.rca_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissFragment();
            }
        });

        viewModel.getCategoryList();
        viewModel.getActionList();
    }


    private void dismissFragment(){
        this.dismiss();
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
                        viewModel.getAssetCategory(assetList.get(0).getCategoryId());
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
                        txtCategory.setText(category, false);
                        txtCategory.dismissDropDown();
                        break;
                    }
                }
                if (source.length() == 0 && category.length() > 0)
                    viewModel.getSourceList(category);
            }
        });

        viewModel.getCategorySourceLiveData().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> failureCodeNestings) {
                categoryAdapter.clear();
                int position = 0;
                for (String category: failureCodeNestings) {
                    categoryAdapter.add(category);

                }
                categoryAdapter.notifyDataSetChanged();
                //txtCategory.showDropDown();
                if (category.length() == 0) {
                    viewModel.getAssets(workOrder);
                } else {
                    viewModel.getSourceList(category);
                }
            }
        });

        viewModel.getSourceLiveData().observe(getViewLifecycleOwner(), new Observer<List<RCACategorySource.CategoryJoinSource>>() {
            @Override
            public void onChanged(List<RCACategorySource.CategoryJoinSource> nestedSources) {
                sourceAdapter.clear();
                for (RCACategorySource.CategoryJoinSource nestedSource : nestedSources) {
                    for (Source source : nestedSource.getSourceList())
                        sourceAdapter.add(source.getName());
                }
                sourceAdapter.notifyDataSetChanged();

                int position = sourceAdapter.getPosition(category);
                if (position >= 0 && source.length() == 0) {
                    source = category;
                    txtSource.setText(source, false);
                    txtSource.dismissDropDown();
                } else if (source.length() > 0) {
                    txtSource.setText(source, false);
                    txtSource.dismissDropDown();
                } else {
                    txtSource.clearListSelection();
                    txtSource.showDropDown();
                }
                viewModel.getProblemsCausesList(source);

            }
        });

        viewModel.getProblemCauseLiveData().observe(getViewLifecycleOwner(), new Observer<List<SourceJoinProblemCause>>() {
            @Override
            public void onChanged(List<SourceJoinProblemCause> sourceProblems) {
                int problemPosition = 0;
                problemAdapter.clear();

                int causePosition = 0;
                causeAdapter.clear();
                for (SourceJoinProblemCause sourceProblem : sourceProblems) {
                    for (Problem problem : sourceProblem.getProblemList())
                        if (problem.getId() != 0) {
                            problemAdapter.add(problem.getDescription());
                            problemHashMap.put(problemPosition, problem.getId());
                            if (problem.getId() == selectedProblemId){
                                txtProblem.setText(problem.getDescription());
                                txtProblem.dismissDropDown();
                            }
                            problemPosition++;

                        }
                    for (Cause cause : sourceProblem.getCauseList())
                        if (cause.getId() != 0) {
                            causeAdapter.add(cause.getDescription());
                            causeHashMap.put(causePosition, cause.getId());
                            if (cause.getId() == selectedCauseId) {
                                txtCause.setText(cause.getDescription());
                                txtCause.dismissDropDown();
                            }
                            causePosition++;
                        }
                }

                problemAdapter.notifyDataSetChanged();
                causeAdapter.notifyDataSetChanged();

                txtProblem.clearListSelection();
                txtCause.clearListSelection();

            }
        });

        viewModel.getActionLiveData().observe(getViewLifecycleOwner(), new Observer<List<Action>>() {
            @Override
            public void onChanged(List<Action> actions) {
                int actionPosition = 0;
                actionAdapter.clear();
                for (Action action: actions) {
                    actionAdapter.add(action.getDescription());
                    actionHashmap.put(actionPosition, action.getId());
                    if (action.getId() == selectedActionId) {
                        txtAction.setText(action.getDescription());
                        txtAction.dismissDropDown();
                    }
                    actionPosition++;
                }
                actionAdapter.notifyDataSetChanged();
                txtAction.clearListSelection();
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
