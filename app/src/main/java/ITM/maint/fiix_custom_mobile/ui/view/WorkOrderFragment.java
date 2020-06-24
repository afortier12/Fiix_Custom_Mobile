package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.IWorkOrderRepository;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderViewModel;

public class WorkOrderFragment extends Fragment {

    private WorkOrderViewModel viewModel;
    private ProgressBarDialog progressBarDialog;
    private RecyclerView recyclerView;
    private WorkOrderAdapter adapter;
    private ArrayList<Integer> workOrderIdList;

    private TextView lblCode;
    private TextView lblPriority;
    private TextView lblAsset;
    private TextView lblDescription;
    private TextView lblType;
    private String username;
    private int id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        workOrderIdList = new ArrayList<>();

        progressBarDialog = new ProgressBarDialog(getContext());

        viewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order, container, false);

        workOrderIdList.clear();
        viewModel.getWorkOrderTaskResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrderTask>>() {
            @Override
            public void onChanged(List<WorkOrderTask> workOrderTasks) {
                if (workOrderTasks != null){
                    for (WorkOrderTask workOrderTask : workOrderTasks){
                        workOrderIdList.add(workOrderTask.getWorkOrderId());
                    }
                    viewModel.findWorkOrders(workOrderIdList);
                }
            }
        });

        username = getArguments().getString("User");
        id = getArguments().getInt("id");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.work_order_toolbar);

        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        lblAsset = getView().findViewById(R.id.work_order_asset);
        lblCode = getView().findViewById(R.id.work_order_code);
        lblDescription = getView().findViewById(R.id.work_order_description);
        lblPriority = getView().findViewById(R.id.work_order_priority);
        lblType = getView().findViewById(R.id.work_order_type);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        viewModel.findWorkOrderTasks(username, 0, 0);
    }

    @Override
    public void onPause() {
        progressBarDialog.dismiss();
        super.onPause();
    }

    @Override
    public void onDestroy(){
        viewModel.dispose();
        super.onDestroy();
    }


}
