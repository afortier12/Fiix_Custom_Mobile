package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderTaskAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.SharedViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderTaskViewModel;

public class WorkOrderTaskFragment extends Fragment implements WorkOrderTaskUpdateDialog.OnUpdateListener,
        WorkOrderTaskAdapter.OnDeleteTaskListener{

    public static final String TAG = "WorkOrderTaskFragment";

    private static final int TASK_UPDATE_FRAGMENT_REQUEST_CODE = 25;
    private static final int TASK_DELETE_FRAGMENT_REQUEST_CODE = 26;


    private RecyclerView recyclerView;
    private WorkOrderTaskAdapter adapter;
    private WorkOrderTaskViewModel viewModel;

    private String username;
    private int userId;
    private WorkOrder workOrder;

    private ArrayList<WorkOrderTask> workOrderTaskList;

    public WorkOrderTaskFragment(String username, int userId, WorkOrder workOrder) {
        this.username = username;
        this.userId = userId;
        this.workOrder = workOrder;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        workOrderTaskList = new ArrayList<WorkOrderTask>();

        viewModel = new ViewModelProvider(this).get(WorkOrderTaskViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order_task, container, false);

        workOrderTaskList.clear();

        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getActivity().getApplication(), this);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedViewModel.class);

        adapter = new WorkOrderTaskAdapter(workOrderTaskList,  this);

        recyclerView = root.findViewById(R.id.work_order_task_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        viewModel.getWorkOrderTaskResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<WorkOrderTask>>() {
            @Override
            public void onChanged(List<WorkOrderTask> workOrderTasks) {
                if (workOrderTasks != null) {
                    SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getActivity().getApplication(), getActivity());
                    SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedViewModel.class);
                    sharedViewModel.setWorkOrderTaskList(workOrderTasks);

                    workOrderTaskList.clear();
                    workOrderTaskList.addAll(workOrderTasks);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getWorkOrderTasks(username, userId, workOrder.getId());

        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getActivity().getApplication(), getActivity());
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedViewModel.class);
        sharedViewModel.getTask().observe(getViewLifecycleOwner(), new Observer<WorkOrderTask>() {
            @Override
            public void onChanged(WorkOrderTask workOrderTask) {
                workOrderTaskList.add(workOrderTask);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        View current = getActivity().getCurrentFocus();
        if (current != null) current.clearFocus();
    }

    //From WorkOrderUpdateDialog
    @Override
    public void sendUpdate(WorkOrderTask task, String note, Double timeActual) {
        int index = 0;
        task.setCompletionNotes(note);
        task.setTimeSpentHours(timeActual);
        for (WorkOrderTask taskFromList: workOrderTaskList){
            if (taskFromList.getId() == task.getId()){
                workOrderTaskList.set(index, task);
                viewModel.updateWorkOrderTask(task);
                break;
            }
            index++;
        }

    }

    //From WorkOrderTaskAdapter
    @Override
    public void sendDelete(WorkOrderTask task) {
        viewModel.deleteTask(task);
        workOrderTaskList.remove(task);
        adapter.notifyDataSetChanged();
    }

    //From WorkOrderTaskAdapter
    @Override
    public void sendClose(WorkOrderTask task) {
        WorkOrderTaskUpdateDialog dialog = new WorkOrderTaskUpdateDialog(task);
        dialog.setTargetFragment(WorkOrderTaskFragment.this, TASK_UPDATE_FRAGMENT_REQUEST_CODE);
        dialog.show(getParentFragmentManager(), "Close Task");
    }
}
