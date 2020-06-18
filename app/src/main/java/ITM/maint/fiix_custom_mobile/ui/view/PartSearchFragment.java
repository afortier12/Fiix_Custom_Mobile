package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.ui.adapter.PartFindResultsAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartAddViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartSearchViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;

public class PartSearchFragment extends Fragment {

    private WorkflowModel workflowModel;
    private String barcode;
    private ProgressBarDialog progressBarDialog;
    private RecyclerView recyclerView;

    private PartSearchViewModel viewModel;
    private PartFindResultsAdapter adapter;
    private ArrayList<Part> partList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        partList =  new ArrayList<Part>();

        progressBarDialog = new ProgressBarDialog(getContext());

        viewModel = new ViewModelProvider(this).get(PartSearchViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_part_search, container, false);

        partList.clear();
        adapter = new PartFindResultsAdapter(partList);

        recyclerView = root.findViewById(R.id.fragment_partsearch_searchResultsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        viewModel.getPartResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<Part>>() {
            @Override
            public void onChanged(List<Part> objects) {
                if (objects != null) {
                    partList.clear();
                    partList.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
                progressBarDialog.dismiss();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //progressBarDialog.show();

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

    }

    @Override
    public void onPause() {
        progressBarDialog.dismiss();
        super.onPause();
    }




}
