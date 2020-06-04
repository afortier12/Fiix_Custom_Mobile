package ITM.maint.fiix_custom_mobile.ui.view;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.common.base.Objects;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.AppExecutor;
import ITM.maint.fiix_custom_mobile.CodeAnalyzer;
import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.BarcodeField;
import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeResultFragment;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.BarcodeViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;

public class BarcodeFragment extends Fragment implements  View.OnClickListener {

    private BarcodeViewModel barcodeViewModel;
    @Inject
    AppExecutor appExecutor;

    private static final String TAG = "TestActivity";
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSourcePreview preview;
    private View settingsButton;
    private View flashButton;
    private Chip promptChip;
    private AnimatorSet promptChipAnimator;
    private WorkflowModel workflowModel;
    private WorkflowModel.WorkflowState currentWorkflowState;
    private Camera camera;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        barcodeViewModel =
                new ViewModelProvider(this).get(BarcodeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_barcode, container, false);

        GraphicOverlay graphicOverlay = root.findViewById(R.id.camera_preview_graphic_overlay);
        graphicOverlay.setOnClickListener(this);

        promptChip = root.findViewById(R.id.bottom_prompt_chip);
        promptChipAnimator =
                (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(), R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(promptChip);


        root.findViewById(R.id.close_button).setOnClickListener(this);
        flashButton = root.findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        settingsButton = root.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this.getContext());


        setUpWorkflowModel();
        //cameraSource = new CameraSource(graphicOverlay, workflowModel);
        if (!isCameraPermissionGranted()){
            Toast.makeText(this.getContext(), "Camera permission Disabled. Check Settings...Apps & notifications...App Permissions", Toast.LENGTH_LONG).show();
            getParentFragmentManager().popBackStackImmediate();
        }

        openCamera();

        return root;
    }

    @Override
    public void onClick(View v) {

    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                this.getContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;
    }


    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        CodeAnalyzer codeAnalyzer = new CodeAnalyzer(this.getContext(), appExecutor.detectorThread(), workflowModel);
        imageAnalysis.setAnalyzer(appExecutor.analyzerThread(), codeAnalyzer);

        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        //bind to lifecycle:
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);

    }

    private void openCamera() {
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future
                    // This should never be reached
                    e.printStackTrace();
                }
            }
        }, appExecutor.mainThread());  //ContextCompat.getMainExecutor(this));

    }

    private void startCameraPreview() {
        if (!workflowModel.isCameraLive() && camera != null) {
            workflowModel.markCameraLive();
        }
    }

    private void stopCameraPreview() {
        if (workflowModel.isCameraLive()) {
            workflowModel.markCameraFrozen();
            flashButton.setSelected(false);
        }
    }

    private void setUpWorkflowModel() {
        workflowModel = new ViewModelProvider(this).get(WorkflowModel.class);

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel.workflowState.observe(
                getViewLifecycleOwner(),
                workflowState -> {
                    if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                        return;
                    }

                    currentWorkflowState = workflowState;
                    Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

                    boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);

                    switch (workflowState) {
                        case DETECTING:
                            promptChip.setVisibility(View.VISIBLE);
                            promptChip.setText(R.string.prompt_point_at_a_barcode);
                            startCameraPreview();
                            break;
                        case CONFIRMING:
                            promptChip.setVisibility(View.VISIBLE);
                            promptChip.setText(R.string.prompt_move_camera_closer);
                            startCameraPreview();
                            break;
                        case SEARCHING:
                            promptChip.setVisibility(View.VISIBLE);
                            promptChip.setText(R.string.prompt_searching);
                            stopCameraPreview();
                            break;
                        case DETECTED:
                        case SEARCHED:
                            promptChip.setVisibility(View.GONE);
                            stopCameraPreview();
                            break;
                        default:
                            promptChip.setVisibility(View.GONE);
                            break;
                    }

                    boolean shouldPlayPromptChipEnteringAnimation =
                            wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
                    if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
                        promptChipAnimator.start();
                    }
                });

        workflowModel.detectedBarcode.observe(
                getViewLifecycleOwner(),
                barcode -> {
                    if (barcode != null) {
                        ArrayList<BarcodeField> barcodeFieldList = new ArrayList<>();
                        barcodeFieldList.add(new BarcodeField("Raw Value", barcode.getRawValue()));
                        BarcodeResultFragment.show(getParentFragmentManager(), barcodeFieldList);
                    }
                });
    }

}
