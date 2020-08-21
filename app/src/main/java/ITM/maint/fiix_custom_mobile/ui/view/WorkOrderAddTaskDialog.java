package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

public class WorkOrderAddTaskDialog extends DialogFragment {

    public static final String TAG = "WorkOrderAddTaskDialog";
    private static final String INIT_TIME = "00:00";
    private WorkOrderTaskViewModel viewModel;
    private WorkOrder workOrder;
    private List<WorkOrderTask> workOrderTaskList;
    private int userId;


    private TextInputLayout layoutDescription;
    private TextInputEditText txtDescription;
    private NumberPicker actualHourPicker;
    private NumberPicker actualMinutePicker;
    private TextView txtActualLabel;

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

        LinearLayout actualLayout = view.findViewById(R.id.detail_add_actual_layout);
        actualLayout.bringToFront();
        txtActualLabel = view.findViewById(R.id.task_update_actual_label);

        actualHourPicker = view.findViewById(R.id.task_add_hour_picker);
        actualHourPicker.setMinValue(0);
        actualHourPicker.setMaxValue(23);

        actualHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtActualLabel.setTextColor(Color.BLACK);
                actualHourPicker.setTextColor(Color.BLACK);
                actualMinutePicker.setTextColor(Color.BLACK);
            }
        });


        actualHourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        actualHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtActualLabel.setTextColor(Color.BLACK);
            }
        });

        actualMinutePicker = view.findViewById(R.id.task_add_minute_picker);
        actualMinutePicker.setMinValue(0);
        actualMinutePicker.setMaxValue(59);

        actualMinutePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        actualMinutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                txtActualLabel.setTextColor(Color.BLACK);

            }
        });

        save = (MaterialButton) view.findViewById(R.id.task_add_save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                String description = txtDescription.getText().toString();
                Boolean ok = Boolean.TRUE;
                if(description.length() == 0) {
                    Snackbar.make(getView(), "No description entered!", Snackbar.LENGTH_LONG).show();
                    layoutDescription.setError("Note is required or press cancel");
                    ok = Boolean.FALSE;
                }

                String estTime = String.format("%02d", actualHourPicker.getValue()) + ":" + String.format("%02d",actualMinutePicker.getValue());

                if ((estTime.equalsIgnoreCase(INIT_TIME) || (actualHourPicker.getValue()) > 23) || (actualHourPicker.getValue() >59)){
                    Snackbar.make(getView(), "Time estimate not valid!", Snackbar.LENGTH_LONG).show();
                    txtActualLabel.setTextColor(Color.RED);
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

}
