package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class TypeAdapter extends ChipListAdapter<MaintenanceType> {

    private View itemView;

    public TypeAdapter(Context context, ISharedAdapter.OnItemClickListener listener) {
        super(context, listener);
    }

    @NonNull
    @Override
    public TypeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        itemView = LayoutInflater.from(parent.getContext())
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
        protected void bind(ISharedAdapter data, ISharedAdapter.OnItemClickListener listener) {
            MaintenanceType type = (MaintenanceType) data;
            chip.setText(type.getStrName());
            chip.setCheckable(true);
            String name = type.getStrName().replaceAll(" ", "_");
            try {
                int chipColorId = itemView.getResources().getIdentifier(name, "color", itemView.getContext().getPackageName());
                String hexColor = itemView.getResources().getString(chipColorId).replaceAll("#ff", "");
                if (Utils.isColorDark(Integer.parseInt(hexColor, 16)))
                    chip.setTextColor(Color.WHITE);
                else
                    chip.setTextColor(Color.BLACK);
                    chip.setChipBackgroundColorResource(chipColorId);
            } catch (Exception e) {
                chip.setChipBackgroundColorResource(R.color.type_NA);
                chip.setTextColor(Color.WHITE);
            }

            chip.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onTypeItemClick(type);
                }
            });
        }

    }
}
