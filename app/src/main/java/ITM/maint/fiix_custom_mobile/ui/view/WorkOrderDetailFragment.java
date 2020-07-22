package ITM.maint.fiix_custom_mobile.ui.view;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.ui.adapter.ChipListAdapter;
import ITM.maint.fiix_custom_mobile.ui.adapter.ISharedAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderDetailViewModel;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderDetailFragment extends Fragment {

    public static final String TAG = "WorkOrderDetailFragment";

    private WorkOrderDetailViewModel viewModel;
    private String username;
    private int userId;
    private WorkOrder workOrder;

    private List<MaintenanceType> maintenanceTypeList;
    private List<WorkOrderStatus> workOrderStatusList;

    private List<T extends ISharedAdapter> adapterDisplayList;
    private List<WorkOrderStatus> workOrderStatusDisplayList;

    private RecyclerView recyclerView;
    private ChipListAdapter adapter;

    public WorkOrderDetailFragment(String username, int userId, WorkOrder workOrder) {
        this.username = username;
        this.userId = userId;
        this.workOrder = workOrder;

        maintenanceTypeList = new ArrayList<>();
        workOrderStatusList = new ArrayList<>();

        adapterDisplayList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(WorkOrderDetailViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order_detail, container, false);

        adapter = new ChipListAdapter(maintenanceTypeDisplayList, workOrderStatusDisplayList, new OnMaintenanceSelectedListener(root), new OnStatusSelectedListener(root));

        recyclerView = root.findViewById(R.id.maintenance_type_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

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
        LinearLayout requestedLayout = root.findViewById(R.id.detail_requestedBy_layout);
        requestedLayout.bringToFront();

        viewModel.getWorkOrderTaskResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrderTask>>() {
            @Override
            public void onChanged(List<WorkOrderTask> workOrderTasks) {
                if (workOrderTasks != null) {
                    double estTime = 0.0;
                    for (WorkOrderTask task : workOrderTasks) {
                        estTime += task.getEstimatedHours();
                    }
                    int estHour = (int) estTime;
                    String strEstHour = String.format("%02d",estHour);
                    int estMinute = (int) (estTime - estHour);
                    String strEstMinute =  String.format("%02d",estMinute);
                    workOrder.setEstTime(strEstHour + ":" + strEstMinute);
                    TextView tvEstTime = getView().findViewById(R.id.detail_estimated_time);
                    if (tvEstTime != null)
                        tvEstTime.setText(workOrder.getEstTime());

                }

            }
        });

        viewModel.getEstTimeResponseLiveData().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double estTime) {

                if (estTime != null) {
                    String strEstHour = String.format("%02d",estTime.intValue());
                    int estMinute = (int) (estTime - estTime.intValue());
                    String strEstMinute =  String.format("%02d",estMinute);
                    workOrder.setEstTime(strEstHour + ":" + strEstMinute);
                    TextView tvEstTime = getView().findViewById(R.id.detail_estimated_time);
                    if (tvEstTime != null)
                        tvEstTime.setText(workOrder.getEstTime());
                }
            }
        });

        viewModel.getMaintenanceTypeLiveData().observe(getViewLifecycleOwner(), new Observer<List<MaintenanceType>>() {
            @Override
            public void onChanged(List<MaintenanceType> maintenanceTypes) {
                if (maintenanceTypes != null){
                    maintenanceTypeList.clear();
                    for (MaintenanceType type: maintenanceTypes){
                        maintenanceTypeList.add(type);
                        if (type.getId() == workOrder.getMaintenanceTypeId()){
                            setupMaintenanceChip(type);
                        }
                    }
                }
            }
        });

        viewModel.getWorkOrderStatusLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrderStatus>>() {
            @Override
            public void onChanged(List<WorkOrderStatus> statuses) {
                if (statuses != null){
                    workOrderStatusList.clear();
                    for (WorkOrderStatus status : statuses){
                        workOrderStatusList.add(status);
                        if (status.getId() == workOrder.getStatusId()){
                           setupStatusChip(status);
                        }
                    }
                }
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (workOrder != null) {

            TextView tvPriorityDescription = view.findViewById(R.id.work_order_detail_priority_text);
            ImageView ivPriorityIcon = view.findViewById(R.id.work_order_detail_priority_icon);
            TextView tvAsset = view.findViewById(R.id.detail_asset);
            Chip chipType = view.findViewById(R.id.detail_type);
            Chip chipStatus = view.findViewById(R.id.detail_status);
            TextView tvDescription = view.findViewById(R.id.detail_description);
            TextView tvRequestedByName = view.findViewById(R.id.detail_requestedBy_Name);
            TextView tvRequestedByEmail = view.findViewById(R.id.detail_requestedBy_email);
            TextView tvScheduledDate = view.findViewById(R.id.detail_scheduled_date);
            TextView tvEstTime = view.findViewById(R.id.detail_estimated_time);
            TextView tvNotes = view.findViewById(R.id.detail_note);

            DrawerLayout sideDrawer = view.findViewById(R.id.detail_drawer_right);
            sideDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {

                }

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {
                    if(adapter.getItemViewType(1) == ChipListAdapter.ITEM_TYPE_MAINTENANCE_TYPE) {
                        adapter.notifyItemRangeRemoved(1, maintenanceTypeDisplayList.size());
                    } else {
                        adapter.notifyItemRangeRemoved(1, workOrderStatusDisplayList.size());
                    }
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
            /*RadioGroup radioGroup = view.findViewById(R.id.detail_drawer_group);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = (RadioButton) view.findViewById(checkedId);
                    String selection = String.valueOf(radioButton.getText()).replaceAll("_", " ");
                    Chip chipType = view.findViewById(R.id.detail_type);
                    chipType.setText(selection);
                    int chipColor = getResources().getIdentifier(selection, "color", getActivity().getPackageName());
                    chipType.setChipBackgroundColorResource(chipColor);

                    sideDrawer.closeDrawer(Gravity.RIGHT);
                }
            });*/

            sideDrawer.closeDrawer(Gravity.RIGHT);

            Drawable img;
            if (workOrder.getPriorityOrder() < 7) {
                img = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_high_priority, null);
                ivPriorityIcon.setImageDrawable(img);
            } else if (workOrder.getPriorityOrder() < 9) {
                img = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_medium_priority, null);
                ivPriorityIcon.setImageDrawable(img);
            } else {
                img = ResourcesCompat.getDrawable(this.getResources(), R.drawable.ic_calendar, null);
                ivPriorityIcon.setImageDrawable(img);
            }

            tvPriorityDescription.setText(workOrder.getExtraFields().getPriorityName());
            tvAsset.setText(workOrder.getAssets());

            chipType.setText(workOrder.getExtraFields().getMaintenanceType());
            chipType.setOnClickListener(new Chip.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!sideDrawer.isDrawerOpen(Gravity.RIGHT)) {
                        /*radioGroup.removeAllViews();

                        for (MaintenanceType type: maintenanceTypeList){
                            RadioButton chipType = new RadioButton(radioGroup.getContext());
                            Log.d(TAG, type.getStrName() + "=" + String.valueOf(chipType.getId()));
                            chipType.setText(type.getStrName());

                            radioGroup.addView(chipType);
                        }*/
                        workOrderStatusDisplayList.clear();
                        maintenanceTypeDisplayList.addAll(maintenanceTypeList);
                        adapter.setItemViewType(ChipListAdapter.ITEM_TYPE_MAINTENANCE_TYPE);
                        adapter.notifyItemRangeInserted(0,maintenanceTypeDisplayList.size());

                        sideDrawer.openDrawer(Gravity.RIGHT);
                    }

                }
            });

            chipStatus.setText(workOrder.getExtraFields().getWorkOrderStatus());
            chipStatus.setOnClickListener(new Chip.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!sideDrawer.isDrawerOpen(Gravity.RIGHT)) {
                        workOrderStatusDisplayList.clear();
                        workOrderStatusDisplayList.addAll(workOrderStatusList);
                        adapter.setItemViewType(ChipListAdapter.ITEM_TYPE_WORK_ORDER_STATUS);
                        adapter.notifyItemRangeInserted(0,workOrderStatusDisplayList.size());

                        sideDrawer.openDrawer(Gravity.RIGHT);
                    }
                }
            });

            tvDescription.setText(workOrder.getDescription());
            tvRequestedByEmail.setText(workOrder.getGuestEmail());
            tvRequestedByName.setText(workOrder.getGuestName());

            String scheduleDate = workOrder.getEstCompletionDate();
            if (scheduleDate == null){
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                tvScheduledDate.setText(dateFormat.format(date));
            } else {
                tvScheduledDate.setText(workOrder.getEstCompletionDate());
            }

            String estTime = workOrder.getEstTime();
            if (estTime == null) {
                tvEstTime.setText("N/A");
                viewModel.getWorkOrderEstTime(workOrder.getId(), userId);
            } else {
                tvEstTime.setText(estTime);
            }
            tvNotes.setText(workOrder.getCompletionNotes());
        }



        viewModel.getMaintenanceTypeDetails();
        viewModel.getWorkOrderStatusDetails();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupMaintenanceChip(MaintenanceType type){
        Chip chipType = getView().findViewById(R.id.detail_type);
        try {
            String resourceName = type.getStrName().replaceAll(" ", "_");
            int chipColor = getResources().getIdentifier(resourceName, "color", getActivity().getPackageName());
            String hexColor = getResources().getString(chipColor).replaceAll("#ff", "");
            if (Utils.isColorDark(Integer.parseInt(hexColor, 16)))
                chipType.setTextColor(Color.WHITE);
            else
                chipType.setTextColor(Color.BLACK);
            chipType.setChipBackgroundColorResource(chipColor);

        } catch (Exception e){
            chipType.setChipBackgroundColorResource(R.color.type_NA);
            chipType.setTextColor(Color.WHITE);
        }
    }

    private void setupStatusChip(WorkOrderStatus status){
        String resourceColor = "status_"+ String.valueOf(status.getIntControlID());
        int chipColor = getResources().getIdentifier(resourceColor, "color", getActivity().getPackageName());
        Chip chipStatus = getView().findViewById(R.id.detail_status);
        try{
            chipStatus.setChipBackgroundColorResource(chipColor);
            String hexColor = getResources().getString(chipColor).replaceAll("#ff", "");
            if (Utils.isColorDark(Integer.parseInt(hexColor, 16)))
                chipStatus.setTextColor(Color.WHITE);
            else
                chipStatus.setTextColor(Color.BLACK);
        } catch (Exception e){
            chipStatus.setChipBackgroundColorResource(R.color.type_NA);
            chipStatus.setTextColor(Color.WHITE);
        }
    }

    class OnMaintenanceSelectedListener implements ChipListAdapter.OnItemClickListener {

        private View view;

        public OnMaintenanceSelectedListener(View view) {
            this.view = view;
        }

        @Override
        public void onItemClick(MaintenanceType type) {
            setupMaintenanceChip(type);
        }
    }

    class OnStatusSelectedListener implements ChipListAdapter.OnStatusItemClickListener {

        private View view;

        public OnStatusSelectedListener(View view) {
            this.view = view;
        }

        @Override
        public void onItemClick(WorkOrderStatus status) {
            setupStatusChip(status);
        }
    }


}
