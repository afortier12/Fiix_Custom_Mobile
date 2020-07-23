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

public class StatusAdapter extends ChipListAdapter<WorkOrderStatus> {

    public StatusAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public StatusAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_item, parent, false);

        return new StatusAdapterViewHolder(itemView);
    }



    protected class StatusAdapterViewHolder extends ChipListAdapter.ChipListViewHolder{

        private Chip chip;

        public StatusAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_list_item);
        }

        @Override
        protected void bind(ISharedAdapter data) {
           /*WorkOrderStatus status = (WorkOrderStatus) workOrderStatusList.get(position);
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
            }*/
        }

    }
}
