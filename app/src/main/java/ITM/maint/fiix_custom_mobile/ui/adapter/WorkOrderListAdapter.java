package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderListAdapter extends RecyclerView.Adapter<WorkOrderListAdapter.WorkOrderResultsHolder> {

    private ArrayList<WorkOrder> workOrderList;
    private View itemView;
    private OnItemClickListener listener;

    public WorkOrderListAdapter(ArrayList<WorkOrder> workOrderList, OnItemClickListener listener) {
        this.workOrderList = workOrderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkOrderListAdapter.WorkOrderResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.work_order_item, parent, false);

        return new WorkOrderListAdapter.WorkOrderResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkOrderListAdapter.WorkOrderResultsHolder holder, int position) {
        WorkOrder workOrder = (WorkOrder) workOrderList.get(position);

        int order = workOrder.getPriorityOrder();
        String priority = workOrder.getExtraFields().getPriorityName();
        String code = workOrder.getCode();
        String plant = workOrder.getPlant();
        String department = workOrder.getDepartment();
        String asset = workOrder.getAssets();
        String description = workOrder.getDescription();
        //String problemCode = workOrder.getExtraFields().getProblem();

        Drawable img;
        img = Utils.getPriorityIcon(order, itemView.getContext());
        holder.priorityIcon.setImageDrawable(img);
        holder.priorityText.setText(priority);
        holder.codeText.setText(code);
        holder.plantText.setText(plant);
        holder.departmentText.setText(department);
        holder.assetText.setText(asset);
        holder.descriptionText.setText(description);
        //holder.problemCodeText.setText(problemCode);

        holder.bind(workOrder, listener);

    }

    @Override
    public int getItemCount() {
        return workOrderList.size();
    }

    public void setResults(List<WorkOrder> results) {
        this.workOrderList.addAll(results);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(WorkOrder workOrder);
    }


    class WorkOrderResultsHolder extends RecyclerView.ViewHolder{
        private TextView priorityText;
        private TextView codeText;
        private TextView plantText;
        private TextView departmentText;
        private TextView assetText;
        private TextView descriptionText;
        //private TextView problemCodeText;
        private ImageView priorityIcon;

        public WorkOrderResultsHolder(@NonNull View itemView) {
            super(itemView);

            priorityText = itemView.findViewById(R.id.work_order_priority);
            codeText = itemView.findViewById(R.id.work_order_code);
            plantText = itemView.findViewById(R.id.work_order_plant);
            departmentText = itemView.findViewById(R.id.work_order_department);
            assetText = itemView.findViewById(R.id.work_order_asset);
            descriptionText = itemView.findViewById(R.id.work_order_description);
            //problemCodeText = itemView.findViewById(R.id.work_order_problem_code);
            priorityIcon = itemView.findViewById(R.id.work_order_priority_icon);

        }

        public void bind(WorkOrder workOrder, OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(workOrder);
                }
            });
        }
    }
}
