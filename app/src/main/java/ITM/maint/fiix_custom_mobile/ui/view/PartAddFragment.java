package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartAddViewModel;

public class PartAddFragment extends Fragment {

    private String barcode;
    private ProgressBarDialog progressBarDialog;
    private TextView lblBarcode;
    private TextInputEditText fldMake;
    private TextInputEditText fldModel;
    private TextInputEditText fldPartNumber;
    private Button btnSubmit;

    private PartAddViewModel viewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        progressBarDialog = new ProgressBarDialog(getContext());

        viewModel = new ViewModelProvider(this).get(PartAddViewModel.class);
        viewModel.init();

        View root = inflater.inflate(R.layout.fragment_part_add, container, false);

        viewModel.getPartDBLiveData().observe(getViewLifecycleOwner(), new Observer<Part>() {
            @Override
            public void onChanged(Part part) {
                if (part != null){
                    fldMake.setText(part.getMake());
                    fldModel.setText(part.getModel());
                    fldPartNumber.setText(part.getUnspcCode());
                    progressBarDialog.dismiss();
                } else {
                    viewModel.findPart(barcode, 0);
                }
            }
        });

        viewModel.getPartResponseLiveData().observe(getViewLifecycleOwner(), new Observer<List<?>>() {
            @Override
            public void onChanged(List<?> parts) {
                if (parts != null) {
                    if (!parts.isEmpty()) {
                        Part part = (Part) parts.get(0);
                        fldMake.setText(part.getMake());
                        fldModel.setText(part.getModel());
                        fldPartNumber.setText(part.getUnspcCode());
                    }
                }
                progressBarDialog.dismiss();
            }

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        Toolbar toolbar = view.findViewById(R.id.part_add_toolbar);

        NavigationUI.setupWithNavController(
                toolbar, navController, appBarConfiguration);

        progressBarDialog.show();

        lblBarcode = getView().findViewById(R.id.label_barcode);
        fldMake = getView().findViewById(R.id.fragment_partadd_make);
        fldModel = getView().findViewById(R.id.fragment_partadd_model);
        fldPartNumber = getView().findViewById(R.id.fragment_partadd_pn);
        btnSubmit = getView().findViewById(R.id.fragment_partadd_submit);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        PartAddFragmentArgs args = PartAddFragmentArgs.fromBundle(getArguments());
        barcode = args.getBarcode();
        lblBarcode.setText("Barcode: " + barcode);

        viewModel.findPart(barcode, 1);

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
