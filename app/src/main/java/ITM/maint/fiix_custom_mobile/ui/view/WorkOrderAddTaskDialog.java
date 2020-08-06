package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderTaskViewModel;
import ITM.maint.fiix_custom_mobile.utils.Status;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderAddTaskDialog extends DialogFragment {

    public static final String TAG = "WorkOrderAddTaskDialog";
    private static final String INIT_TIME = "00:00";
    private WorkOrderTaskViewModel viewModel;
    private WorkOrder workOrder;
    private List<WorkOrderTask> workOrderTaskList;
    private int userId;


    private TextInputLayout layoutDescription;
    private TextInputEditText txtDescription;
    private TextInputLayout layoutEstTime;
    private TextInputEditText txtEstTime;

    private MaterialButton save;
    private MaterialButton cancel;

    public OnTaskAddListener onTaskAddListener;

    public interface OnTaskAddListener {
        void sendNewTask(WorkOrderTask newTask);
    }

    public WorkOrderAddTaskDialog(WorkOrder workOrder, int userId) {
        this.workOrder = workOrder;
        this.userId = userId;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onTaskAddListener = (OnTaskAddListener) getTargetFragment();
            Log.d(TAG, "onAttach: " + onTaskAddListener);
        } catch (ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException" + e.getMessage());
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_work_order_task_add, container, false);

        viewModel = new ViewModelProvider(this).get(WorkOrderTaskViewModel.class);
        viewModel.init();

        viewModel.getResponseStatus().observe(getViewLifecycleOwner(), new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                Snackbar.make(getView(), status.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        viewModel.getTaskAddLiveData().observe(getViewLifecycleOwner(), new Observer<WorkOrderTask>() {
            @Override
            public void onChanged(WorkOrderTask workOrderTask) {
                if (workOrderTask != null){
                    onTaskAddListener.sendNewTask(workOrderTask);
                }
                dismissFragment();
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutDescription = (TextInputLayout) view.findViewById(R.id.task_add_description_layout);
        ViewGroup.LayoutParams layoutParams = layoutDescription.getLayoutParams();
        layoutParams.height = getActivity().getWindow().getDecorView().getHeight()/4;
        layoutDescription.setLayoutParams(layoutParams);
        txtDescription = (TextInputEditText) view.findViewById(R.id.task_add_description);

        txtEstTime = view.findViewById(R.id.task_add_estimated);
        layoutEstTime = (TextInputLayout) view.findViewById(R.id.task_add_estimated_layout);
        txtEstTime.addTextChangedListener(new ValidationTextWatcher(txtEstTime, layoutEstTime, txtEstTime));

        txtEstTime.setText(INIT_TIME);

        save = (MaterialButton) view.findViewById(R.id.task_add_save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = txtDescription.getText().toString();
                Boolean ok = Boolean.TRUE;
                if(description.length() == 0) {
                    Snackbar.make(getView(), "No description entered!", Snackbar.LENGTH_LONG).show();
                    layoutDescription.setError("Note is required or press cancel");
                    ok = Boolean.FALSE;
                }

                String estTime = txtEstTime.getText().toString();

                if(!validateEstimate() || estTime.equalsIgnoreCase(INIT_TIME)){
                    Snackbar.make(getView(), "Time estimate not valid!", Snackbar.LENGTH_LONG).show();
                    layoutEstTime.setError("Please enter a valid time estimate");
                    ok = Boolean.FALSE;
                }

                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                //Utils.hideKeyboard(getActivity());
                if (ok) {
                    viewModel.addTaskToDB(description, estTime, userId, workOrder.getId());
                }
            }
        });

        cancel = (MaterialButton) view.findViewById(R.id.task_add_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Utils.hideKeyboard(getActivity());
                getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dismissFragment();
            }
        });

    }



    private void dismissFragment(){
        this.dismiss();
    }


    private boolean validateEstimate() {
        String estTime = txtEstTime.getText().toString();
        int estLength = estTime.length();
        int estPos = estTime.indexOf(":");
        if (estTime.trim().isEmpty()) {
            layoutEstTime.setError("Time is required");
            requestFocus(layoutEstTime);
            return false;
        } else if (!(estTime.indexOf(":") == 2) || !(estTime.length() == 5)){
            layoutEstTime.setError("Time must be in format HH:mm");
            requestFocus(layoutEstTime);
            return false;
        } else {
            layoutEstTime.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class ValidationTextWatcher implements TextWatcher {

        private View view;
        private String before;
        private TextInputEditText txtEdit;
        private TextInputLayout layoutEdit;

        private ValidationTextWatcher(View view, TextInputLayout layoutEdit, TextInputEditText txtEdit) {
            this.view = view;
            this.txtEdit = txtEdit;
            this.layoutEdit =layoutEdit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            before = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.length() > 1)
                s.setFilters(new InputFilter[]{new ValidationTextWatcher.NumberFilter(12, 59, 0)});
            else
                s.setFilters(new InputFilter[]{});

            if (s.length() == 2 && !(s.toString().contains(":")) && !(before.contains(":")))
                txtEdit.setText(s + ":");
            else if (s.length() > 2 && !(s.toString().contains(":")))
                s.insert(2, ":");

            validateEstimate();
        }


        class NumberFilter implements InputFilter {

            private int maxHour;
            private int maxMinute;
            private int minValue;

            public NumberFilter(int maxHour, int maxMinute, int minValue) {
                this.maxHour = maxHour;
                this.maxMinute = maxMinute;
                this.minValue = minValue;
            }


            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (dstart == 0 && dest.length() == 5)
                    return null;
                if (source.length() == 0 && dend == 3)
                    return "";

                if (source.toString().equalsIgnoreCase(" ") ||
                        source.toString().equalsIgnoreCase(",") ||
                        source.toString().equalsIgnoreCase("-"))       // ||
                    // ((start == 0) && (end == 0) && (dend > dstart)))  //backspace
                    return "";

                try {
                    StringBuilder sb = new StringBuilder(dest);
                    sb.insert(dstart, source);

                    String newText = sb.toString();
                    if (newText.length() <= 2) {
                        if (isInRange(Integer.parseInt(newText), maxHour))
                            return null;
                    } else if (newText.length() == 3) {
                        if (source.toString().equalsIgnoreCase(":")) {
                            return null;
                        } else if (dend == 3) {
                            return ":";
                        }
                    } else if (newText.length() < 6) {
                        String[] time = String.valueOf(newText).split("\\:");
                        if (time.length == 2)
                            if (isInRange(Integer.parseInt(time[1]), maxMinute))
                                return null;
                    } else {
                        return "";
                    }
                } catch (NumberFormatException nfe) {
                    return "";
                }
                return "";
            }

            private boolean isInRange(Integer number, int maxValue) {
                if ((number.compareTo(maxValue) <= 0) && (number.compareTo((minValue)) >= 0))
                    return true;
                else {
                    Log.d(TAG, "Number entered " + String.valueOf(number) + "is out of range");
                    layoutEdit.setError("Invalid entry");
                    requestFocus(layoutEdit);
                    return false;
                }
            }
        }
    }
}
