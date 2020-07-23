package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;


public abstract class ChipListAdapter<T extends ISharedAdapter> extends RecyclerView.Adapter<ChipListAdapter.ChipListViewHolder> {

    protected final Context context;
    protected final ArrayList<T> chipList = new ArrayList<>();

    public ChipListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ChipListAdapter.ChipListViewHolder holder, int position) {
        holder.bind(chipList.get(position));
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


    public static ChipListAdapter create(Context context, int adapterType) {
        switch (adapterType) {
            case 0:
                return new TypeAdapter(context);
            case 1:
                return new StatusAdapter(context);
            default:
                throw new IllegalArgumentException("Invalid adapter type: " + adapterType);

        }
    }


    protected abstract class ChipListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected final Chip chip;


        protected ChipListViewHolder(View viewHolder) {
            super(viewHolder);

            chip = (Chip) viewHolder.findViewById(R.id.chip_list_item);
            viewHolder.setOnClickListener(this);
        }

        protected abstract void bind(T data);


        @Override
        public void onClick(View v) {


        }
    }
}
