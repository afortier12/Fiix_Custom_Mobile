package ITM.maint.fiix_custom_mobile.firebase;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.experimental.UseExperimental;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeBoundGraphic;
import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeConfirmingGraphic;
import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeLoadingGraphic;
import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeReticleGraphic;
import ITM.maint.fiix_custom_mobile.ui.graphics.camera.CameraReticleAnimator;
import ITM.maint.fiix_custom_mobile.ui.view.GraphicOverlay;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.WorkflowModel.WorkflowState;
import ITM.maint.fiix_custom_mobile.utils.PreferenceUtils;


public class CodeAnalyzer  implements ImageAnalysis.Analyzer {

    public static final String TAG = "CodeAnalyzer";
    private Context context;
    private Executor executor;
    private GraphicOverlay graphicOverlay;
    private final CameraReticleAnimator cameraReticleAnimator;
    private FirebaseVisionBarcodeDetector barcodeDetector;
    private WorkflowModel workflowModel;


    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public CodeAnalyzer(Context context, GraphicOverlay graphicOverlay, Executor executor, WorkflowModel workflowModel) {
        this.context = context;
        this.executor = executor;
        this.graphicOverlay = graphicOverlay;
        this.cameraReticleAnimator = new CameraReticleAnimator(graphicOverlay);
        this.workflowModel = workflowModel;
    }

    private int getFirebaseRotation(Context context) {
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case Surface.ROTATION_90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case Surface.ROTATION_180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case Surface.ROTATION_270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException(
                        "Invalid rotation value."
                );
        }
    }


    @Override
    @UseExperimental(markerClass = androidx.camera.core.ExperimentalGetImage.class)
    public void analyze(@NonNull ImageProxy image) {
        if (image == null || image.getImage() == null) {
            return;
        }

        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                        .build();
        barcodeDetector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);

        FirebaseVisionImage visionImage = FirebaseVisionImage.fromMediaImage(image.getImage(), getFirebaseRotation(context) );

        //Log.d(TAG, String.format("Width = %d", visionImage.getBitmap().getWidth()));
        //Log.d(TAG, String.format("Height = %d", visionImage.getBitmap().getHeight()));

        Task<List<FirebaseVisionBarcode>> task;
        task = barcodeDetector.detectInImage(visionImage);
        task.addOnSuccessListener(executor, barcodes -> {
                    if (!barcodes.isEmpty()) {
                        FirebaseVisionBarcode barcodeInCenter = null;
                        for (FirebaseVisionBarcode barcode : barcodes) {
                            float width = graphicOverlay.getWidth()/2;
                            float height = graphicOverlay.getHeight()/2;

                            RectF box = graphicOverlay.translateRect(Objects.requireNonNull(barcode.getBoundingBox()));
                            if (box.contains(width , height)) {
                                barcodeInCenter = barcode;
                                break;
                            }

                            graphicOverlay.clear();
                            if (barcodeInCenter == null) {
                                cameraReticleAnimator.start();
                                //graphicOverlay.add(new BarcodeBoundGraphic(graphicOverlay, box));
                                graphicOverlay.add(new BarcodeReticleGraphic(graphicOverlay, cameraReticleAnimator));
                                workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
                            } else {
                                cameraReticleAnimator.cancel();
                                float sizeProgress =
                                        PreferenceUtils.getProgressToMeetBarcodeSizeRequirement(graphicOverlay, barcodeInCenter);
                                if (sizeProgress < 1) {
                                    // Barcode in the camera view is too small, so prompt user to move camera closer.
                                    graphicOverlay.add(new BarcodeBoundGraphic(graphicOverlay, box));
                                    graphicOverlay.add(new BarcodeConfirmingGraphic(graphicOverlay, barcodeInCenter));
                                    workflowModel.setWorkflowState(WorkflowModel.WorkflowState.CONFIRMING);
                                } else {
                                    // Barcode size in the camera view is sufficient.
                                    if (PreferenceUtils.shouldDelayLoadingBarcodeResult(graphicOverlay.getContext())) {
                                        ValueAnimator loadingAnimator = createLoadingAnimator(graphicOverlay, barcodeInCenter);
                                        loadingAnimator.start();
                                        graphicOverlay.add(new BarcodeLoadingGraphic(graphicOverlay, loadingAnimator));
                                        workflowModel.setWorkflowState(WorkflowModel.WorkflowState.SEARCHING);
                                    } else {
                                        workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTED);
                                        workflowModel.detectedBarcode.setValue(barcodeInCenter);
                                    }
                                }
                            }
                            graphicOverlay.invalidate();

                        }
                    } /*else {
                        graphicOverlay.clear();
                        cameraReticleAnimator.start();
                        graphicOverlay.add(new BarcodeReticleGraphic(graphicOverlay, cameraReticleAnimator));
                        workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
                        graphicOverlay.postInvalidate();
                    }*/
                });
        image.close();
    }

    private ValueAnimator createLoadingAnimator(
            GraphicOverlay graphicOverlay, FirebaseVisionBarcode barcode) {
        float endProgress = 1.1f;
        ValueAnimator loadingAnimator = ValueAnimator.ofFloat(0f, endProgress);
        loadingAnimator.setDuration(2000);
        loadingAnimator.addUpdateListener(
                animation -> {
                    if (Float.compare((float) loadingAnimator.getAnimatedValue(), endProgress) >= 0) {
                        graphicOverlay.clear();
                        workflowModel.setWorkflowState(WorkflowState.SEARCHED);
                        workflowModel.detectedBarcode.setValue(barcode);
                    } else {
                        graphicOverlay.invalidate();
                    }
                });
        return loadingAnimator;
    }

    /*@Override
    protected void onFailure(Exception e) {
        Log.e(TAG, "Barcode detection failed!", e);
    }

    @Override
    public void stop() {
        try {
            barcodeDetector.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to close barcode detector!", e);
        }
    }*/
}




