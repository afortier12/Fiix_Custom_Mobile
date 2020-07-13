package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
