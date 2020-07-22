package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public abstract class ChipListAdapter<T extends ISharedAdapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> chipList;
    private View itemView;
    private OnItemClickListener typeListener;
    private OnStatusItemClickListener statusListener;

    protected final Context context;

    public ChipListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_item, parent, false);
        if (viewType == ITEM_TYPE_MAINTENANCE_TYPE)
            return new MaintenanceTypeResultsHolder(itemView);
        else
            return new WorkOrderStatusResultsHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position)==ITEM_TYPE_MAINTENANCE_TYPE) {
            MaintenanceType type = (MaintenanceType) maintenanceTypeList.get(position);
            ((MaintenanceTypeResultsHolder) holder).chip.setText(type.getStrName());
            ((MaintenanceTypeResultsHolder) holder).chip.setCheckable(true);
            String name = type.getStrName().replaceAll(" ", "_");
            try {
                int chipColorId = itemView.getResources().getIdentifier(name, "color", itemView.getContext().getPackageName());
                String hexColor = itemView.getResources().getString(chipColorId).replaceAll("#ff", "");
                if (Utils.isColorDark(Integer.parseInt(hexColor, 16)))
                    ((MaintenanceTypeResultsHolder) holder).chip.setTextColor(Color.WHITE);
                else
                    ((MaintenanceTypeResultsHolder) holder).chip.setTextColor(Color.BLACK);
                ((MaintenanceTypeResultsHolder) holder).chip.setChipBackgroundColorResource(chipColorId);
                ((MaintenanceTypeResultsHolder) holder).bind(type, typeListener);
            } catch (Exception e) {
                ((MaintenanceTypeResultsHolder) holder).chip.setChipBackgroundColorResource(R.color.type_NA);
                ((MaintenanceTypeResultsHolder) holder).chip.setTextColor(Color.WHITE);
            }
        } else {
            WorkOrderStatus status = (WorkOrderStatus) workOrderStatusList.get(position);
            ((WorkOrderStatusResultsHolder) holder).chip.setText(status.getStrName());
            ((WorkOrderStatusResultsHolder) holder).chip.setCheckable(true);
            try {
                String resourceColor = "status_"+ String.valueOf(status.getIntControlID());
                int chipColor = itemView.getResources().getIdentifier(resourceColor, "color", itemView.getContext().getPackageName());
                ((WorkOrderStatusResultsHolder) holder).chip.setChipBackgroundColorResource(chipColor);
                String hexColor = itemView.getResources().getString(chipColor).replaceAll("#ff", "");
                if (Utils.isColorDark(Integer.parseInt(hexColor, 16)))
                    ((WorkOrderStatusResultsHolder) holder).chip.setTextColor(Color.WHITE);
                else
                    ((WorkOrderStatusResultsHolder) holder).chip.setTextColor(Color.BLACK);
                ((WorkOrderStatusResultsHolder) holder).bind(status, statusListener);
            } catch (Exception e) {
                ((WorkOrderStatusResultsHolder) holder).chip.setChipBackgroundColorResource(R.color.status_NA);
                ((WorkOrderStatusResultsHolder) holder).chip.setTextColor(Color.WHITE);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return itemViewType;
    }

    @Override
    public int getItemCount() {
        return maintenanceTypeList.size();
    }

    public void setItemViewType(int itemViewType){
        this.itemViewType = itemViewType;
    }


    public void setResults(List<MaintenanceType> results) {
        this.maintenanceTypeList.addAll(results);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(MaintenanceType type);
    }

    public interface OnStatusItemClickListener {
        void onItemClick(WorkOrderStatus status);
    }


    protected abstract class ChipListAdapterHolder extends RecyclerView.ViewHolder{
        protected ChipListAdapterHolder(View itemView) {
            super(itemView);
            // this can have some or all views that are shared between search results
        }

        protected abstract void bind(T data, OnItemClickListener listener);
    }

}
