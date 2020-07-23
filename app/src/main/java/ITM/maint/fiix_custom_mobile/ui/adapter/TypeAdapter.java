package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;

public class TypeAdapter extends ChipListAdapter<MaintenanceType> {

    public TypeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public TypeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_item, parent, false);

        return new TypeAdapterViewHolder(itemView);
    }


    protected class TypeAdapterViewHolder extends ChipListAdapter.ChipListViewHolder {

        private Chip chip;

        protected TypeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_list_item);
        }

        @Override
        protected void bind(ISharedAdapter data) {
            /* MaintenanceType type = (MaintenanceType) maintenanceTypeList.get(position);
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
            }*/
        }


    }
}
