package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

public class WorkOrderTaskAdapter extends RecyclerView.Adapter<WorkOrderTaskAdapter.TasksHolder> {

    private ArrayList<WorkOrderTask> taskList;

    public WorkOrderTaskAdapter(ArrayList<WorkOrderTask> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TasksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);

        return new WorkOrderTaskAdapter.TasksHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TasksHolder holder, int position) {
        WorkOrderTask task = (WorkOrderTask) taskList.get(position);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TasksHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView modelText;
        private TextView manufacturerText;
        private TextView partnumberText;
        private ImageView image;


        public TasksHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.part_item_name);
            modelText = itemView.findViewById(R.id.part_item_model);
            manufacturerText = itemView.findViewById(R.id.part_item_make);
            partnumberText = itemView.findViewById(R.id.part_item_unspc);
            image = itemView.findViewById(R.id.part_item_smallThumbnail);
        }
    }
}
