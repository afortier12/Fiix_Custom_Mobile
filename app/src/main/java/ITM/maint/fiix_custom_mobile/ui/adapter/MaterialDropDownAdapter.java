package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.List;

public class MaterialDropDownAdapter<String> extends ArrayAdapter<String> {

    private List<String> values;

    public MaterialDropDownAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        values = objects;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            filterResults.values = values;
            filterResults.count = values.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };


    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }
}
