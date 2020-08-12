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
import android.widget.ImageButton;
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

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }



    class TasksHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextInputEditText txtEstTime;
        private SwitchMaterial completeSwitch;
        private TextView description;
        private Activity activity;
        private ImageButton btnDeleteTask;

        public TasksHolder(@NonNull View itemView, Activity activity) {
            super(itemView);

            this.activity = activity;
            txtEstTime = itemView.findViewById(R.id.task_estimate_time);
            completeSwitch = itemView.findViewById(R.id.task_complete);
            btnDeleteTask = itemView.findViewById(R.id.task_delete);
            description = itemView.findViewById(R.id.task_description);


            txtEstTime.setKeyListener(null);
            txtEstTime.setShowSoftInputOnFocus(false);

        }

        @Override
        public void onClick(View v) {

        }
    }





}
