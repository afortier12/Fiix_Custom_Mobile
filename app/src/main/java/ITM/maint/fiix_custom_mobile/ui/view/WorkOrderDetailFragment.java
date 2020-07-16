package ITM.maint.fiix_custom_mobile.ui.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;

import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderDetailViewModel;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderDetailFragment extends Fragment {

    public static final String TAG = "WorkOrderDetailFragment";

    private WorkOrderDetailViewModel viewModel;
    private String username;
    private int userId;
    private WorkOrder workOrder;

    public WorkOrderDetailFragment(String username, int userId, WorkOrder workOrder) {
        this.username = username;
        this.userId = userId;
        this.workOrder = workOrder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(WorkOrderDetailViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order_detail, container, false);

        LinearLayout assetLayout = root.findViewById(R.id.detail_asset_layout);
        assetLayout.bringToFront();
        LinearLayout typeLayout = root.findViewById(R.id.detail_type_layout);
        typeLayout.bringToFront();
        LinearLayout statusLayout = root.findViewById(R.id.detail_status_layout);
        statusLayout.bringToFront();
        LinearLayout descriptionLayout = root.findViewById(R.id.detail_description_layout);
        descriptionLayout.bringToFront();
        LinearLayout notesLayout = root.findViewById(R.id.detail_note_layout);
        notesLayout.bringToFront();
        LinearLayout scheduleLayout = root.findViewById(R.id.detail_scheduled_layout);
        scheduleLayout.bringToFront();
        LinearLayout estLayout = root.findViewById(R.id.detail_estimated_layout);
        estLayout.bringToFront();

        viewModel.getWorkOrderTaskResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrderTask>>() {
            @Override
            public void onChanged(List<WorkOrderTask> workOrderTasks) {
                double estTime = 0.0;
                for(WorkOrderTask task:workOrderTasks){
                    estTime += task.getEstimatedHours();
                }
                int estHour = (int) estTime;
                String strEstHour = String.valueOf(estHour);
                if (strEstHour.length() < 2)
                    Utils.padLeftZeros(strEstHour, 2-strEstHour.length());
                int estMinute = (int) (estTime-estHour);
                String strEstMinute = String.valueOf(estMinute);
                if (strEstMinute.length() < 2)
                    Utils.padRightZeros(strEstMinute, 2-strEstMinute.length());
               workOrder.setEstTime(strEstHour+":"+strEstMinute);

            }
        });

        viewModel.getEstTimeResponseLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double estTime) {

                String strEstHour = String.valueOf(estTime.intValue());
                if (strEstHour.length() < 2)
                    Utils.padLeftZeros(strEstHour, 2-strEstHour.length());
                int estMinute = (int) (estTime-estTime.intValue());
                String strEstMinute = String.valueOf(estMinute);
                if (strEstMinute.length() < 2)
                    Utils.padRightZeros(strEstMinute, 2-strEstMinute.length());
                workOrder.setEstTime(strEstHour+":"+strEstMinute);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (workOrder != null) {

            TextView priorityDescription = view.findViewById(R.id.work_order_detail_priority_text);
            ImageView priorityIcon = view.findViewById(R.id.work_order_detail_priority_icon);
            TextView asset = view.findViewById(R.id.detail_asset);
            Chip type = view.findViewById(R.id.detail_type);
            Chip status = view.findViewById(R.id.detail_status);
            TextView description = view.findViewById(R.id.detail_description);
            TextView requestedByName = view.findViewById(R.id.detail_requestedBy_Name);
            TextView requestedByEmail = view.findViewById(R.id.detail_requestedBy_email);
            TextView scheduledDate = view.findViewById(R.id.detail_scheduled_date);
            TextView estTime = view.findViewById(R.id.detail_estimated_time);
            TextView notes = view.findViewById(R.id.detail_note);


            Drawable img;
            if (workOrder.getPriorityOrder() < 7) {
                img = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_high_priority, null);
                priorityIcon.setImageDrawable(img);
            } else if (workOrder.getPriorityOrder() < 9) {
                img = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_medium_priority, null);
                priorityIcon.setImageDrawable(img);
            } else {
                img = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_calendar, null);
                priorityIcon.setImageDrawable(img);
            }

            priorityDescription.setText(workOrder.getExtraFields().getPriorityName());
            asset.setText(workOrder.getAssets());
            type.setText(workOrder.getExtraFields().getMaintenanceType());
            status.setText(workOrder.getExtraFields().getWorkOrderStatus());
            description.setText(workOrder.getDescription());
            requestedByEmail.setText(workOrder.getGuestEmail());
            requestedByName.setText(workOrder.getGuestName());
            scheduledDate.setText(workOrder.getEstCompletionDate());
            estTime.setText(workOrder.getEstTime());
            notes.setText(workOrder.getCompletionNotes());

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
