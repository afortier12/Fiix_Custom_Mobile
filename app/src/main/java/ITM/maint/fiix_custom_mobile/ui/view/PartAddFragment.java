package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartResponse;
import ITM.maint.fiix_custom_mobile.ui.adapter.PartFindResultsAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.BarcodeViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.NotificationsViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartAddViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;

public class PartAddFragment extends Fragment {

    private WorkflowModel workflowModel;
    private String barcode;

    private PartAddViewModel viewModel;
    private PartFindResultsAdapter adapter;

    private TextInputEditText typeEditText, makeEditText;
    private Button searchButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        adapter = new PartFindResultsAdapter();
        viewModel = new ViewModelProvider(this).get(PartAddViewModel.class);
        viewModel.init();
        viewModel.getPartResponseLiveData().observe(getViewLifecycleOwner(), new Observer<PartResponse>() {
            @Override
            public void onChanged(PartResponse partResponse) {
                if (partResponse != null)
                    adapter.setResults(partResponse.getParts());
            }
        });
        View root = inflater.inflate(R.layout.fragment_part_add, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        //final TextView textView = getView().findViewById(R.id.text_barcode);
        //textView.setText(barcode);

        viewModel.findParts();

    }

    @Override
    public void onPause() {
        super.onPause();
    }




}
