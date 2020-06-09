package ITM.maint.fiix_custom_mobile.ui.view;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.SurfaceRequest;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.chip.Chip;
import com.google.common.base.Objects;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.di.AppExecutor;
import ITM.maint.fiix_custom_mobile.firebase.CodeAnalyzer;
import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.BarcodeField;
import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeResultFragment;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.BarcodeViewModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;
import dagger.android.support.DaggerFragment;

public class BarcodeFragment extends DaggerFragment implements  View.OnClickListener, DisplayManager.DisplayListener {

    private static final String TAG = "BarcodeFragment";
    
    
    private BarcodeViewModel barcodeViewModel;
    @Inject
    AppExecutor appExecutor;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private GraphicOverlay graphicOverlay;
    private View settingsButton;
    private View flashButton;
    private Chip promptChip;
    private AnimatorSet promptChipAnimator;
    private WorkflowModel workflowModel;
    private WorkflowModel.WorkflowState currentWorkflowState;
    private Camera camera;
    private CameraSourcePreview previewView;
    private DisplayManager displayManager;
    private int displayID;
    private ImageAnalysis imageAnalysis;
    private MainThreadExecutor mainThreadExecutor;
    private PreviewSurfaceProvider previewSurfaceProvider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        barcodeViewModel =
                new ViewModelProvider(this).get(BarcodeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_barcode, container, false);

        displayManager = (DisplayManager) this.getContext().getSystemService(Context.DISPLAY_SERVICE);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this.getContext());
        mainThreadExecutor = new MainThreadExecutor();

        setUpWorkflowModel();
        //cameraSource = new CameraSource(graphicOverlay, workflowModel);
        if (!isCameraPermissionGranted()){
            Toast.makeText(this.getContext(), "Camera permission Disabled. Check Settings...Apps & notifications...App Permissions", Toast.LENGTH_LONG).show();
            getParentFragmentManager().popBackStackImmediate();
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        graphicOverlay = view.findViewById(R.id.camera_preview_graphic_overlay);
        graphicOverlay.setOnClickListener(this);

        promptChip = view.findViewById(R.id.bottom_prompt_chip);
        promptChipAnimator =
                (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(), R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(promptChip);


        view.findViewById(R.id.close_button).setOnClickListener(this);
        flashButton = view.findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        previewView = getView().findViewById(R.id.camera_preview);

        view.post(new Runnable() {
            @Override
            public void run() {
                displayID = previewView.getDisplay().getDisplayId();
                graphicOverlay.setCameraInfo(previewView.getCameraPreviewSize());
                openCamera();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

        Preview preview = new Preview.Builder().build();
        Surface surface = previewView.getSurface();
        previewSurfaceProvider = new PreviewSurfaceProvider(surface, mainThreadExecutor);
        preview.setSurfaceProvider(mainThreadExecutor, previewSurfaceProvider);


        imageAnalysis = new ImageAnalysis.Builder()
                .setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        CodeAnalyzer codeAnalyzer = new CodeAnalyzer(this.getContext(), graphicOverlay, mainThreadExecutor, workflowModel);
        imageAnalysis.setAnalyzer(mainThreadExecutor, codeAnalyzer);

        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        //bind to lifecycle:
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);



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
        }, ContextCompat.getMainExecutor(this.getContext()));

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

    private class PreviewSurfaceProvider implements Preview.SurfaceProvider {
        private Surface surface;
        private Executor executor;

        public PreviewSurfaceProvider(Surface surface, Executor executor) {
            this.surface = surface;
            this.executor = executor;
        }

        @Override
        public void onSurfaceRequested(@NonNull SurfaceRequest request) {
            //if (isShuttingDown()) {
             //   request.willNotProvideSurface();
             //   return;
            //}

            // Provide the surface and wait for the result to clean up the surface.
            request.provideSurface(surface, mainThreadExecutor, (result) -> {
                // In all cases (even errors), we can clean up the state. As an
                // optimization, we could also optionally check for REQUEST_CANCELLED
                // since we may be able to reuse the surface on subsequent surface requests.
                //TODO("Not yet implemented")
            });
        }
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }


    @Override
    public void onDisplayAdded(int displayId) {

    }

    @Override
    public void onDisplayRemoved(int displayId) {

    }

    @Override
    public void onDisplayChanged(int displayId) {
        if (displayId == this.displayID){
            imageAnalysis.setTargetRotation(((WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation());
        }
    }
}
