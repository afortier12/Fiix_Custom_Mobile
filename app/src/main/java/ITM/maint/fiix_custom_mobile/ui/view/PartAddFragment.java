package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.NotificationsViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.PartAddViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;

public class PartAddFragment extends Fragment {

    private PartAddViewModel partAddViewModel;
    private WorkflowModel workflowModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_part_add, container, false);
        setupWorkFlowModel(root);

        return root;
    }

    private void setupWorkFlowModel(View root){
        workflowModel = new ViewModelProvider(this).get(WorkflowModel.class);
        final TextView textView = root.findViewById(R.id.text_barcode);
        workflowModel.getBarcode().observe(getViewLifecycleOwner(), new Observer<FirebaseVisionBarcode>() {
            @Override
            public void onChanged(FirebaseVisionBarcode firebaseVisionBarcode) {
                String s = workflowModel.getBarcodeValue();
                textView.setText(s);
                partAddViewModel.setBarcodeValue(s);
            }
        });

    }
}
