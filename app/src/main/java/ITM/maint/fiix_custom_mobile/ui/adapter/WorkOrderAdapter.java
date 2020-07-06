package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderResultsHolder> {

    private ArrayList<WorkOrder> workOrderList;
    private View itemView;

    public WorkOrderAdapter(ArrayList<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }

    @NonNull
    @Override
    public WorkOrderAdapter.WorkOrderResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_order_item, parent, false);

        return new WorkOrderAdapter.WorkOrderResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOrderAdapter.WorkOrderResultsHolder holder, int position) {
        WorkOrder workOrder = (WorkOrder) workOrderList.get(position);

        int order = workOrder.getPriorityOrder();
        String priority = workOrder.getExtraFields().getPriorityName();
        String code = workOrder.getCode();
        String type = workOrder.getExtraFields().getMaintenanceType();
        String asset = workOrder.getAssets();
        String description = workOrder.getDescription();
        String problemCode = workOrder.getExtraFields().getProblem();

        Drawable img;
        if (order < 3){
            img = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_error_24px, null);
            holder.priorityText.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        } else if (order < 5) {
            img = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_warning_24px, null);
            holder.priorityText.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        } else {
            img = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.ic_schedule_24px, null);
            holder.priorityText.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        }

        holder.priorityText.setText(priority);
        holder.codeText.setText(code);
        holder.typeText.setText(type);
        holder.assetText.setText(asset);
        holder.descriptionText.setText(description);
        holder.problemCodeText.setText(problemCode);


    }

    @Override
    public int getItemCount() {
        return workOrderList.size();
    }

    public void setResults(List<WorkOrder> results) {
        this.workOrderList.addAll(results);
        notifyDataSetChanged();
    }


    class WorkOrderResultsHolder extends RecyclerView.ViewHolder{
        private TextView priorityText;
        private TextView codeText;
        private TextView typeText;
        private TextView assetText;
        private TextView descriptionText;
        private TextView problemCodeText;

        public WorkOrderResultsHolder(@NonNull View itemView) {
            super(itemView);

            priorityText = itemView.findViewById(R.id.work_order_priority);
            codeText = itemView.findViewById(R.id.work_order_code);
            typeText = itemView.findViewById(R.id.work_order_type);
            assetText = itemView.findViewById(R.id.work_order_asset);
            descriptionText = itemView.findViewById(R.id.work_order_description);
            problemCodeText = itemView.findViewById(R.id.work_order_problem_code);

        }
    }
}
