package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.chip.Chip;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;

public class TypeAdapter extends ChipListAdapter<MaintenanceType> {

    public TypeAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ChipListAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chip_item, parent, false);

        return new ChipListAdapterHolder(itemView);
    }

    protected class AdapterViewHolder extends ChipListAdapterHolder{

        private Chip chip;

        protected AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_list_item);
        }


        @Override
        protected void bind(MaintenanceType type, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    listener.onItemClick(type);
                }
            });
        }


    }
}
