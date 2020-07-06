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
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.WorkOrderResultsHolder> {

    private ArrayList<WorkOrderJoinPriority> workOrderPriorityList;
    private View itemView;

    public WorkOrderAdapter(ArrayList<WorkOrderJoinPriority> workOrderPriorityList) {
        this.workOrderPriorityList = workOrderPriorityList;
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
        WorkOrderJoinPriority workOrderPriority = (WorkOrderJoinPriority) workOrderPriorityList.get(position);

        int order = workOrderPriority.getPriority().getOrder();
        List<WorkOrder> workOrderList = workOrderPriority.getWorkOrderList();

        for (WorkOrder workOrder: workOrderList) {
            String priority = String.valueOf(order);
            String code = workOrder.getCode();
            String type = workOrder.getExtraFields().getMaintenanceType();
            String asset = workOrder.getAssets();
            String description = workOrder.getDescription();
            String problemCode = workOrder.getExtraFields().getProblem();

            if (order < 3){
                setTextViewDrawableColor(holder.priorityText, Color.RED);
            } else if (order < 5) {
                setTextViewDrawableColor(holder.priorityText, Color.YELLOW);
            } else {
                setTextViewDrawableColor(holder.priorityText, Color.BLUE);
            }

            holder.priorityText.setText(priority);
            holder.codeText.setText(code);
            holder.typeText.setText(type);
            holder.assetText.setText(asset);
            holder.descriptionText.setText(description);
            holder.problemCodeText.setText(problemCode);
        }

    }

    @Override
    public int getItemCount() {
        return workOrderPriorityList.size();
    }

    public void setResults(List<WorkOrderJoinPriority> results) {
        this.workOrderPriorityList.addAll(results);
        notifyDataSetChanged();
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
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
