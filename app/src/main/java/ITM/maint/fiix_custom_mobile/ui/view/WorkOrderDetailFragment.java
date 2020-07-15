package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderDetailViewModel;

public class WorkOrderDetailFragment extends Fragment {

    public static final String TAG = "WorkOrderDetailFragment";

    private WorkOrderDetailViewModel viewModel;
    private String username;
    private int userId;
    private int workOrderId;

    public WorkOrderDetailFragment(String username, int userId, int workOrderId) {
        this.username = username;
        this.userId = userId;
        this.workOrderId = workOrderId;
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
        LinearLayout descriptionLayout = root.findViewById(R.id.detail_description__layout);
        descriptionLayout.bringToFront();


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
