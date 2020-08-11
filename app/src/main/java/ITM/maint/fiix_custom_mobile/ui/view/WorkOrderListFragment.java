package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderListAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.SharedViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderListViewModel;
import ITM.maint.fiix_custom_mobile.utils.Status;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderListFragment extends Fragment  {

    private static final String TAG = "WorkOrderListFragment";
    private WorkOrderListViewModel viewModel;
    private ProgressBarDialog progressBarDialog;
    private RecyclerView recyclerView;
    private WorkOrderListAdapter adapter;


    private String username;
    private int id;
    private ArrayList<WorkOrder> workOrderList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        workOrderList = new ArrayList<WorkOrder>();

        progressBarDialog = new ProgressBarDialog(getContext());

        viewModel = new ViewModelProvider(this).get(WorkOrderListViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order_list, container, false);

        setMargin(root);

        workOrderList.clear();
        adapter = new WorkOrderListAdapter(workOrderList, new OnWorkOrderSelectedListener(root));

        recyclerView = root.findViewById(R.id.work_order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        viewModel.getWorkOrderDBLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrderJoinPriority>>() {
            @Override
            public void onChanged(List<WorkOrderJoinPriority> workOrderJoinPriorities) {
                List<Integer> assetIds = new ArrayList<>();
                if (workOrderJoinPriorities != null) {
                    workOrderList.clear();
                    Gson gson = new Gson();
                    for (WorkOrderJoinPriority workOrderJoinPriority : workOrderJoinPriorities){
                        for (WorkOrder workOrder : workOrderJoinPriority.getWorkOrderList()) {
                            String jsonString = gson.toJson(workOrder);
                            WorkOrder joinedWorkOrder = gson.fromJson(jsonString, WorkOrder.class);
                            joinedWorkOrder.setPriorityOrder(workOrderJoinPriority.getPriority().getOrder());
                            try {
                                List<Integer> workOrderAssetIds = Utils.splitStringToListOfInt(joinedWorkOrder.getAssetIds());
                                assetIds.add(workOrderAssetIds.get(0));
                            } catch (Exception e){
                                Log.d(TAG, e.getMessage());
                            }
                            workOrderList.add(joinedWorkOrder);
                        }
                    }
                    Collections.sort(workOrderList, new Comparator<WorkOrder>() {
                        @Override
                        public int compare(WorkOrder o1, WorkOrder o2) {
                            return Integer.compare(o1.getPriorityOrder(), o2.getPriorityOrder());
                        }
                    });

                    viewModel.getDepartmentsPlants(workOrderList);
                    adapter.notifyDataSetChanged();
                }
                progressBarDialog.dismiss();
                }
        });

        viewModel.getWorkOrderResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrder>>() {
            @Override
            public void onChanged(List<WorkOrder> workOrders) {
                if (workOrders != null){
                    //viewModel.insertWorkOrders(workOrders, username);
                    Log.d(TAG, "work orders response live data ");
                } else {
                    Log.d(TAG, "no work orders to populate in fragment ");
                }
            }
        });

        viewModel.getResponseStatus().observe(getViewLifecycleOwner(), new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                Snackbar.make(getView(), status.getMessage(), Snackbar.LENGTH_LONG).show();
                progressBarDialog.dismiss();
            }
        });

        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getActivity().getApplication(), this);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedViewModel.class);

        String usernameArg = getArguments().getString("User");
        if (usernameArg != null) {
            sharedViewModel.setUsername(usernameArg);
            sharedViewModel.setUserId(getArguments().getInt("id"));
        } else if (savedInstanceState != null){
            sharedViewModel.setUsername(savedInstanceState.getString("USERNAME_KEY"));
            sharedViewModel.setUserId(savedInstanceState.getInt("USERID_KEY"));
        }

        username = sharedViewModel.getUsername().getValue();
        id = sharedViewModel.getUserId().getValue();



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBarDialog.show();

        /*NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        Toolbar toolbar = view.findViewById(R.id.part_search_toolbar);
        NavigationUI.setupWithNavController(null, navController, appBarConfiguration);*/



        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null) {
                    NavController navController = Navigation.findNavController(getView());
                    navController.popBackStack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        viewModel.getWorkOrders(username, id);
    }


    public static int getBottomMargin(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight)
            return realHeight - usableHeight;
        else
            return 0;
    }

    private void setMargin(View view){
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)p;
            lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin , getBottomMargin(getActivity()));
            view.setLayoutParams(lp);
        }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USERNAME_KEY", username);
        outState.putInt("USERID_KEY", id);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class OnWorkOrderSelectedListener implements WorkOrderListAdapter.OnItemClickListener {

        private View view;

        public OnWorkOrderSelectedListener(View view) {
            this.view = view;
        }

        @Override
        public void onItemClick(WorkOrder workOrder) {
            WorkOrderListFragmentDirections.HomeToWorkOrder action =
                    WorkOrderListFragmentDirections.homeToWorkOrder();
            action.setWorkOrder(workOrder);
            Navigation.findNavController(view).navigate(action);
        }
    }

}
