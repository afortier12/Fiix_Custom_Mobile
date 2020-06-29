package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.stream.JsonToken;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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


    private String username;
    private int id;
    private ArrayList<WorkOrder> workOrderList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        workOrderList = new ArrayList<WorkOrder>();

        progressBarDialog = new ProgressBarDialog(getContext());

        viewModel = new ViewModelProvider(this).get(WorkOrderViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order, container, false);

        workOrderList.clear();
        adapter = new WorkOrderAdapter(workOrderList);

        recyclerView = root.findViewById(R.id.fragment_work_order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        viewModel.getWorkOrderResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrder>>() {
            @Override
            public void onChanged(List<WorkOrder> workOrders) {
                if (workOrders != null){
                    workOrderList.clear();
                    workOrderList.addAll(workOrders);
                    adapter.notifyDataSetChanged();
                }
                progressBarDialog.dismiss();
            }
        });

        viewModel.getResponseStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                progressBarDialog.dismiss();
            }
        });

        username = getArguments().getString("User");
        id = getArguments().getInt("id");

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBarDialog.show();

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.work_order_toolbar);

        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        viewModel.findWorkOrderTasks(username, id, 0);
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
