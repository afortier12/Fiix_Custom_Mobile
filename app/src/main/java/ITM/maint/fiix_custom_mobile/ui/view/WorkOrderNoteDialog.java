package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderNoteDialog extends DialogFragment {

    public static final String TAG = "WorkOrderNoteDialog";
    private WorkOrderRepository workOrderRepository;
    private TextInputLayout layoutNotes;
    private TextInputEditText txtNotes;
    private MaterialButton update;
    private MaterialButton cancel;

    private WorkOrder workOrder;

    public interface OnNoteListener {
        void sendNote(String note);
    }

    public OnNoteListener onNoteListener;

    public WorkOrderNoteDialog(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height =ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onNoteListener = (OnNoteListener) getTargetFragment();
            Log.d(TAG, "onAttach: " + onNoteListener);
        } catch (ClassCastException e){
            Log.d(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_work_order_notes, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutNotes = (TextInputLayout) view.findViewById(R.id.note_layout);
        txtNotes = (TextInputEditText) view.findViewById(R.id.detail_note);
        txtNotes.setText(workOrder.getCompletionNotes());

        update = (MaterialButton) view.findViewById(R.id.rca_update_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = txtNotes.getText().toString();

                if(note.length() == 0) {
                    Snackbar.make(getView(), "No note entered!", Snackbar.LENGTH_LONG).show();
                    layoutNotes.setError("Note is required or press cancel");
                }

                onNoteListener.sendNote(note);
                dismissFragment();
            }
        });

        cancel = (MaterialButton) view.findViewById(R.id.rca_cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissFragment();
            }
        });
    }

    private void dismissFragment(){
        this.dismiss();
    }

}
