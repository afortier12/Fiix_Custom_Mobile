package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

public class WorkOrderTaskAdapter extends RecyclerView.Adapter<WorkOrderTaskAdapter.TasksHolder> {

    private static final String TAG = "WorkOrderTaskAdapter";
    private ArrayList<WorkOrderTask> taskList;
    private Activity activity;

    public WorkOrderTaskAdapter(ArrayList<WorkOrderTask> taskList, Activity activity) {
        this.taskList = taskList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TasksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new WorkOrderTaskAdapter.TasksHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksHolder holder, int position) {
        WorkOrderTask task = (WorkOrderTask) taskList.get(position);

        String description = task.getDescription();
        Double estimated = task.getEstimatedHours();
        Double timeSpent = task.getTimeSpentHours();

        holder.description.setText(description);

        if (task.getCompletedDate() == null){
            holder.completeSwitch.setChecked(false);
        } else {
            holder.completeSwitch.setChecked(true);
        }

        String[] estTimeList = String.valueOf(estimated).split("\\.");
        String estTime;
        if (estTimeList.length == 2 ) {
            estTime = estTimeList[0] + ":" + estTimeList[1];
        } else {
            estTime = "00:00";
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date time = format.parse(estTime);
            holder.txtEstTime.setText(format.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String[] actTimeList = String.valueOf(timeSpent).split("\\.");
        String actTime;
        if (actTimeList.length == 2) {
            actTime = actTimeList[0] + ":" + actTimeList[1];
        } else {
            actTime = "00:00";
        }

        try {
            Date time = format.parse(actTime);
            holder.txtActualTime.setText(format.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }



    class TasksHolder extends RecyclerView.ViewHolder{
        private TextInputEditText txtActualTime;
        private TextInputEditText txtEstTime;
        private SwitchMaterial completeSwitch;
        private TextView description;
        private TextInputLayout layoutActual;
        private Activity activity;


        public TasksHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            this.activity = activity;
            txtActualTime = itemView.findViewById(R.id.task_actual_time);
            txtEstTime = itemView.findViewById(R.id.task_estimate_time);
            completeSwitch = itemView.findViewById(R.id.task_complete);
            description = itemView.findViewById(R.id.task_description);

            layoutActual = (TextInputLayout) itemView.findViewById(R.id.task_actual);
            txtEstTime.setKeyListener(null);
            txtEstTime.setShowSoftInputOnFocus(false);

            txtActualTime.addTextChangedListener(new ValidationTextWatcher(txtActualTime, this));
            txtEstTime.setShowSoftInputOnFocus(false);

        }

        private boolean validateActual() {
            if (txtActualTime.getText().toString().trim().isEmpty()) {
                layoutActual.setError("Time is required");
                requestFocus(layoutActual);
                return false;
            } else if (txtActualTime.getText().toString().indexOf(":") == 3){
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
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

        private class ValidationTextWatcher implements TextWatcher {

            private View view;
            private String before;
            private TasksHolder tasksHolder;

            private ValidationTextWatcher(View view, TasksHolder tasksHolder) {
                this.view = view;
                this.tasksHolder =tasksHolder;
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

               if(s.length() == 2  && !(s.toString().contains(":")) && !(before.contains(":")))
                    tasksHolder.txtActualTime.setText(s + ":");
               else if (s.length() > 2 && !(s.toString().contains(":")))
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


}
