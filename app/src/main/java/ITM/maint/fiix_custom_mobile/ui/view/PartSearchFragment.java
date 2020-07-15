package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.ui.adapter.PartFindResultsAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartSearchViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.SharedViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;

public class PartSearchFragment extends Fragment {

    private WorkflowModel workflowModel;
    private String barcode;
    private ProgressBarDialog progressBarDialog;
    private RecyclerView recyclerView;

    private PartSearchViewModel viewModel;
    private PartFindResultsAdapter adapter;
    private ArrayList<Part> partList;

    private String username;
    private int userId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

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

        viewModel.getPartResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<?>>() {
            @Override
            public void onChanged(List<?> objects) {
                if (objects != null) {
                    partList.clear();
                    partList.addAll((Collection<? extends Part>) objects);
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

        /*NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        BottomNavigationView navView = view.findViewById(R.id.nav_view);

        NavigationUI.setupWithNavController(
                navView, navController, appBarConfiguration);*/

        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                username = s;
            }
        });

        sharedViewModel.getUserId().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer i) {
                userId = i;
            }
        });

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

    }

    @Override
    public void onPause() {
        progressBarDialog.dismiss();
        super.onPause();
    }




}
