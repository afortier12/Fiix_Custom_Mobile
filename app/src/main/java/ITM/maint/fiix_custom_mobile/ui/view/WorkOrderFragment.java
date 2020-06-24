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

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkOrderViewModel;

public class WorkOrderFragment extends Fragment {

    private WorkOrderViewModel viewModel;
    private ProgressBarDialog progressBarDialog;
    private TextView lblCode;
    private TextView lblPriority;
    private TextView lblAsset;
    private TextView lblDescription;
    private TextView lblType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        progressBarDialog = new ProgressBarDialog(getContext());

        viewModel =
               new ViewModelProvider(this).get(WorkOrderViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_work_order, container, false);


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
