package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;


public abstract class ChipListAdapter<T extends ISharedAdapter> extends RecyclerView.Adapter<ChipListAdapter.ChipListViewHolder> {

    protected final Context context;
    protected final ArrayList<T> chipList = new ArrayList<>();

    private ISharedAdapter.OnItemClickListener listener;

    public ChipListAdapter(Context context, ISharedAdapter.OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ChipListAdapter.ChipListViewHolder holder, int position) {
        holder.bind(chipList.get(position), listener);
    }

    @Override
    public int getItemCount() {

        return chipList.size();

    }

    public void addData(List<T> newData) {

        for (T data : newData) {
            chipList.add(data);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        chipList.clear();
    }




    public static ChipListAdapter create(Context context, int adapterType, ISharedAdapter.OnItemClickListener listener) {

        switch (adapterType) {
            case 0:
                return new TypeAdapter(context, listener);
            case 1:
                return new StatusAdapter(context, listener);
            default:
                throw new IllegalArgumentException("Invalid adapter type: " + adapterType);

        }
    }


    protected abstract class ChipListViewHolder extends RecyclerView.ViewHolder {

        protected final Chip chip;


        protected ChipListViewHolder(View viewHolder) {
            super(viewHolder);

            chip = (Chip) viewHolder.findViewById(R.id.chip_list_item);
        }

        protected abstract void bind(T data, ISharedAdapter.OnItemClickListener listener);

    }
}
