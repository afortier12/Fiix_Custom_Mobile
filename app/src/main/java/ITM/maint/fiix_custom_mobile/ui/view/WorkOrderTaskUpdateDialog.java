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
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderTaskAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderRCAViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderTaskViewModel;

public class WorkOrderTaskUpdateDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "WorkOrderTaskUpdateDialog";

    private ImageButton btnIncHour;
    private ImageButton btnDecHour;
    private ImageButton btnIncMinute;
    private ImageButton btnDecMinute;
    private TextInputEditText txtActualTime;
    private TextInputLayout layoutActual;
    private WorkOrderTaskViewModel viewModel;

    private WorkOrderTask task;

    public interface OnUpdateListener {
        void sendUpdate(String note, String timeActual);
    }

    public WorkOrderNoteDialog.OnNoteListener onNoteListener;

    public WorkOrderTaskUpdateDialog(WorkOrderTask task) {
        this.task = task;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_update_task, container, false);

        viewModel = new ViewModelProvider(this).get(WorkOrderTaskViewModel.class);
        viewModel.init();

        txtActualTime = view.findViewById(R.id.task_actual_time);
        txtActualTime.addTextChangedListener(new ValidationTextWatcher(txtActualTime));

        btnIncHour = view.findViewById(R.id.incrActualHour);
        btnIncHour.setOnClickListener(this);
        btnDecHour = view.findViewById(R.id.decActualHour);
        btnDecHour.setOnClickListener(this);
        btnIncMinute = view.findViewById(R.id.incrActualMin);
        btnIncMinute.setOnClickListener(this);
        btnDecMinute = view.findViewById(R.id.decActualMinute);
        btnDecMinute.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Double timeSpent = task.getTimeSpentHours();

        String[] actTimeList = String.valueOf(timeSpent).split("\\.");
        String actTime;
        if (actTimeList.length == 2) {
            actTime = actTimeList[0] + ":" + actTimeList[1];
        } else {
            actTime = "00:00";
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date time = format.parse(actTime);
            txtActualTime.setText(format.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    private void dismissFragment(){
        this.dismiss();
    }

    @Override
    public void onClick(View v) {

        String[] actTimeList = String.valueOf(txtActualTime.getText()).split(":");
        int hour = 0;
        int minute = 0;
        String newTime = "00:00";
        switch (v.getId()) {
            case R.id.incrActualHour:
                if (actTimeList.length == 2) {
                    hour = Integer.parseInt(actTimeList[0]);
                    hour++;
                    if (hour > 23)
                        hour = 0;
                }
                newTime = String.format("%02d", hour);
                newTime  = newTime + ":" + actTimeList[1];
                txtActualTime.setText(newTime);
                break;
            case R.id.decActualHour:
                if (actTimeList.length == 2) {
                    hour = Integer.parseInt(actTimeList[0]);
                    hour--;
                    if (hour < 0)
                        hour = 23;
                }
                newTime = String.format("%02d", hour);
                newTime  = newTime + ":" + actTimeList[1];
                txtActualTime.setText(newTime);
                break;
            case R.id.incrActualMin:
                if (actTimeList.length == 2) {
                    minute = Integer.parseInt(actTimeList[1]);
                    minute++;
                    if (minute > 59)
                        minute = 0;
                }
                newTime = String.format("%02d", minute);
                newTime  = actTimeList[0] + ":" + newTime;
                txtActualTime.setText(newTime);
                break;
            case R.id.decActualMinute:
                if (actTimeList.length == 2) {
                    minute = Integer.parseInt(actTimeList[1]);
                    minute--;
                    if (minute < 0)
                        minute = 59;
                }
                newTime = String.format("%02d", minute);
                newTime  = actTimeList[0] + ":" + newTime;
                txtActualTime.setText(newTime);
                break;
            default:
                break;
        }
    }

    private boolean validateActual() {
        if (txtActualTime.getText().toString().trim().isEmpty()) {
            layoutActual.setError("Time is required");
            requestFocus(layoutActual);
            return false;
        } else if (!(txtActualTime.getText().toString().indexOf(":") == 2) || !(txtActualTime.getText().toString().length() == 5)){
            layoutActual.setError("Time must be in format HH:mm");
            requestFocus(layoutActual);
            return false;
        } else {
            layoutActual.setErrorEnabled(false);
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

        private ValidationTextWatcher(View view) {
            this.view = view;
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

            if(s.length() > 1)
                s.setFilters(new InputFilter[]{ new NumberFilter(12, 59, 0)});
            else
                s.setFilters(new InputFilter[]{});

            if(s.length() == 2  && !(s.toString().contains(":")) && !(before.contains(":"))) {
                s.replace (0,s.length(), s + ":");
            } else if (s.length() > 2 && !(s.toString().contains(":")))
                s.insert(2,":");


            switch (view.getId()) {
                case R.id.task_actual_time:
                    validateActual();
                    break;
            }
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

                if (dstart==0 && dest.length()==5)
                    return null;
                if (source.length()==0 && dend == 3)
                    return "";

                if (source.toString().equalsIgnoreCase(" ")||
                        source.toString().equalsIgnoreCase(",")||
                        source.toString().equalsIgnoreCase("-"))       // ||
                    // ((start == 0) && (end == 0) && (dend > dstart)))  //backspace
                    return "";

                try{
                    StringBuilder sb = new StringBuilder(dest);
                    sb.insert(dstart, source);

                    String newText = sb.toString();
                    if (newText.length() <= 2){
                        if (isInRange(Integer.parseInt(newText), maxHour))
                            return null;
                    } else if (newText.length() == 3) {
                        if (source.toString().equalsIgnoreCase(":")) {
                            return null;
                        } else if (dend == 3){
                            return ":";
                        }
                    } else if(newText.length() < 6){
                        String[] time = String.valueOf(newText).split("\\:");
                        if (time.length == 2)
                            if (isInRange(Integer.parseInt(time[1]), maxMinute))
                                return null;
                    } else {
                        return "";
                    }
                }catch(NumberFormatException nfe){
                    return "";
                }
                return "";
            }

            private boolean isInRange(Integer number, int maxValue){
                if ((number.compareTo(maxValue) <= 0) && (number.compareTo((minValue)) >= 0))
                    return true;
                else {
                    Log.d(TAG, "Number entered " + String.valueOf(number) + "is out of range");
                    layoutActual.setError("Invalid entry");
                    requestFocus(layoutActual);
                    return false;
                }
            }
        }
    }
}

