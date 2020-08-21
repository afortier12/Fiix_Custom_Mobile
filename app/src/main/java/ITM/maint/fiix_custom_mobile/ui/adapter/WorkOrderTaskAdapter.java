package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderTaskUpdateDialog;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderTaskAdapter extends RecyclerView.Adapter<WorkOrderTaskAdapter.TasksHolder> {

    private static final String TAG = "WorkOrderTaskAdapter";
    private ArrayList<WorkOrderTask> taskList;
    private AlertDialog.Builder builder;

    public interface OnDeleteTaskListener {
        void sendDelete(WorkOrderTask task);
        void sendClose(WorkOrderTask task);
    }

    public OnDeleteTaskListener onDeleteListener;

    public WorkOrderTaskAdapter(ArrayList<WorkOrderTask> taskList, OnDeleteTaskListener onDeleteListener) {
        this.taskList = taskList;
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public TasksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        builder = new AlertDialog.Builder(parent.getContext());


        return new WorkOrderTaskAdapter.TasksHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksHolder holder, int position) {
        WorkOrderTask task = (WorkOrderTask) taskList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        String description = task.getDescription();
        Double estimated = task.getEstimatedHours();

        holder.description.setText(description);

        if (task.getCompletedDate() == null) {
            holder.completeSwitch.setChecked(false);
        } else {
            holder.completeSwitch.setChecked(true);
        }

        holder.completeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.completeSwitch.isChecked()) {
                    Date date = new Date();
                    String timestamp = Utils.convertDateTimeToUNIXEpochms(date);
                    task.setCompletedDate(timestamp);
                    onDeleteListener.sendClose(task);
                } else {
                    task.setCompletedDate(null);
                }
            }
        });

        if (task.getUserCreated() > 0){
            holder.btnDeleteTask.setVisibility(View.VISIBLE);
        } else {
            holder.btnDeleteTask.setVisibility(View.INVISIBLE);
        }

        holder.btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage(R.string.delete_task_dialog_message) .setTitle(R.string.delete_task_dialog_title)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                onDeleteListener.sendDelete(task);
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        String[] estTimeList = String.valueOf(estimated).split("\\.");
        String estTime;
        if (estTimeList.length == 2) {
            estTime = estTimeList[0] + ":" + estTimeList[1];
        } else {
            estTime = "00:00";
        }


        try {
            Date time = format.parse(estTime);
            holder.txtEstTime.setText(format.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtEstTime.setKeyListener(null);
        holder.txtEstTime.setShowSoftInputOnFocus(false);

    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksHolder extends RecyclerView.ViewHolder {

        private TextInputEditText txtEstTime;
        private SwitchMaterial completeSwitch;
        private TextView description;
        private ImageButton btnDeleteTask;

        public TasksHolder(@NonNull View itemView) {
            super(itemView);

            txtEstTime = itemView.findViewById(R.id.task_estimate_time);
            completeSwitch = itemView.findViewById(R.id.task_complete);
            btnDeleteTask = itemView.findViewById(R.id.task_delete);
            description = itemView.findViewById(R.id.task_description);

        }


    }

}
