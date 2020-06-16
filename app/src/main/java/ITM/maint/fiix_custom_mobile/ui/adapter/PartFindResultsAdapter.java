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

    public PartFindResultsAdapter(ArrayList<Part> parts) {
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

        String name = part.getName();
        String model = part.getModel();
        String make = part.getMake();
        String partNumber = part.getUnspcCode();

        holder.nameText.setText(name);
        holder.modelText.setText(model);
        holder.manufacturerText.setText(make);
        holder.partnumberText.setText(partNumber);

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
        private TextView manufacturerText;
        private TextView partnumberText;
        private ImageView image;


        public PartFindResultsHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.part_item_name);
            modelText = itemView.findViewById(R.id.part_item_model);
            manufacturerText = itemView.findViewById(R.id.part_item_manufacturer);
            partnumberText = itemView.findViewById(R.id.part_item_partnumber);
            image = itemView.findViewById(R.id.part_item_smallThumbnail);
        }
    }
}
