package ITM.maint.fiix_custom_mobile.ui.adapter;

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
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public class PartFindResultsAdapter extends RecyclerView.Adapter<PartFindResultsAdapter.PartFindResultsHolder> {

    private ArrayList<Object> parts;

    public PartFindResultsAdapter(ArrayList<Object> parts) {
        this.parts = parts;
    }

    @NonNull
    @Override
    public PartFindResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.part_item, parent, false);

        return new PartFindResultsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PartFindResultsHolder holder, int position) {
        Part part = (Part) parts.get(position);

        holder.nameText.setText(String.valueOf(part.getName()));
        holder.modelText.setText(String.valueOf(part.getModel()));
        holder.makeText.setText(String.valueOf(part.getMake()));
        holder.makeText.setText(String.valueOf(part.getUnspcCode()));

        if (part.getThumbnail() != 0) {
            //String imageUrl = volume.getVolumeInfo().getImageLinks().getSmallThumbnail()
            //        .replace("http://", "https://");

            //Glide.with(holder.itemView)
            //        .load(imageUrl)
            //        .into(holder.smallThumbnailImageView);
        }
    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    public void setResults(List<Part> results) {
        this.parts.addAll(results);
        notifyDataSetChanged();
    }

    class PartFindResultsHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView modelText;
        private TextView makeText;
        private TextView unspcText;
        private ImageView image;


        public PartFindResultsHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.part_item_name);
            modelText = itemView.findViewById(R.id.part_item_model);
            makeText = itemView.findViewById(R.id.part_item_make);
            unspcText = itemView.findViewById(R.id.part_item_unspc);
            image = itemView.findViewById(R.id.part_item_smallThumbnail);
        }
    }
}
