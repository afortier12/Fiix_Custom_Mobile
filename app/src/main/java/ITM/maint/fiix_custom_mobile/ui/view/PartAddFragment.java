package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.BarcodeViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.NotificationsViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartAddViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;

public class PartAddFragment extends Fragment {

    private PartAddViewModel partAddViewModel;
    private WorkflowModel workflowModel;
    private String barcode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_part_add, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PartAddFragmentArgs args = PartAddFragmentArgs.fromBundle(getArguments());
        barcode = args.getBarcode();
        final TextView textView = getView().findViewById(R.id.text_barcode);
        textView.setText(barcode);

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
