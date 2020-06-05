package ITM.maint.fiix_custom_mobile.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ITM.maint.fiix_custom_mobile.R;


/** Preview the camera image in the screen. */
public class CameraSourcePreview extends RelativeLayout {
    private static final String TAG = "CameraSourcePreview";

    private static final int STATE_PREVIEW = 1;
    private static final int MAX_CAMERA_PREVIEW_WIDTH = 1080;
    private static final int MAX_CAMERA_PREVIEW_HEIGHT = 1920;


    private GraphicOverlay graphicOverlay;
    private SurfaceView surfaceView;
    private boolean surfaceAvailable = false;
    private CameraDevice camera;
    private int state = STATE_PREVIEW;
    private Context context;
    private Surface surface;
    private Size cameraPreviewSize;
    private OrientationEventListener orientationEventListener;
    private int orientation;


    public CameraSourcePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        surfaceView = new SurfaceView(context);
        surfaceView.getHolder().addCallback(new surfaceCallback());
        addView(surfaceView);

        orientationEventListener = new OrientationEventListener(this.getContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;

                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }

                // Updates target rotation value to {@link ImageAnalysis}
                //imageAnalysis.setTargetRotation(rotation);
                setOrientation(rotation);
            }
        };
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
    }

    private void setCameraPreviewSize(int width, int height){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int displayRotation = ((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
        boolean swappedDimensions = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (orientation == 90 || orientation == 270) {
                    swappedDimensions = true;
                }
                break;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                if (orientation == 0 || orientation == 180) {
                    swappedDimensions = true;
                }
                break;
        }

        Point displaySize = new Point();
        int rotatedPreviewWidth = width;
        int rotatedPreviewHeight = height;
        int maxPreviewWidth = displayMetrics.widthPixels;
        int maxPreviewHeight = displayMetrics.heightPixels;

        if (swappedDimensions) {
            rotatedPreviewWidth = height;
            rotatedPreviewHeight = width;
            maxPreviewWidth = displayMetrics.heightPixels;
            maxPreviewHeight = displayMetrics.widthPixels;
        }

        if (maxPreviewWidth > MAX_CAMERA_PREVIEW_WIDTH) {
            maxPreviewWidth = MAX_CAMERA_PREVIEW_WIDTH;
        }

        if (maxPreviewHeight > MAX_CAMERA_PREVIEW_HEIGHT) {
            maxPreviewHeight = MAX_CAMERA_PREVIEW_HEIGHT;
        }

        StreamConfigurationMap map = setupCamera(displayMetrics.widthPixels, displayMetrics.heightPixels);
        List<Size> previewSizeList = Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888));
        Size largest = Collections.max(previewSizeList, new CompareSizesByArea());

        List<Size> surfaceSizes = Arrays.asList(map.getOutputSizes(SurfaceHolder.class));
        Size[] surfaceSizeArray = surfaceSizes.stream().toArray(n->new Size[n]);
        cameraPreviewSize = chooseOptimalSize(surfaceSizeArray,
                rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                maxPreviewHeight, largest);

        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();

        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutParams.width = cameraPreviewSize.getWidth();
            layoutParams.height = cameraPreviewSize.getHeight();
        } else {
            layoutParams.height = cameraPreviewSize.getWidth();
            layoutParams.width = cameraPreviewSize.getHeight();
        }
        surfaceView.setLayoutParams(layoutParams);
    }

    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

        @Override
        public Comparator<Size> reversed() {
            return null;
        }
    }

    private StreamConfigurationMap setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) this.getContext().getSystemService(Context.CAMERA_SERVICE);
        try {
            for(String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if(cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT){
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                return map;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int layoutWidth = right - left;
        int layoutHeight = bottom - top;

        float previewSizeRatio = (float) layoutWidth / layoutHeight;
        if (cameraPreviewSize != null) {
            if (isPortraitMode(getContext())) {
                // Camera's natural orientation is landscape, so need to swap width and height.
                previewSizeRatio = (float) cameraPreviewSize.getHeight() / cameraPreviewSize.getWidth();
            } else {
                previewSizeRatio = (float) cameraPreviewSize.getWidth() / cameraPreviewSize.getHeight();
            }
        }

        // Match the width of the child view to its parent.
        int childWidth = layoutWidth;
        int childHeight = (int) (childWidth / previewSizeRatio);
        if (childHeight <= layoutHeight) {
            for (int i = 0; i < getChildCount(); ++i) {
                getChildAt(i).layout(0, 0, childWidth, childHeight);
            }
        } else {
            // When the child view is too tall to be fitted in its parent: If the child view is static
            // overlay view container (contains views such as bottom prompt chip), we apply the size of
            // the parent view to it. Otherwise, we offset the top/bottom position equally to position it
            // in the center of the parent.
            int excessLenInHalf = (childHeight - layoutHeight) / 2;
            for (int i = 0; i < getChildCount(); ++i) {
                View childView = getChildAt(i);
                if (childView.getId() == R.id.static_overlay_container) {
                    childView.layout(0, 0, childWidth, layoutHeight);
                } else {
                    childView.layout(0, -excessLenInHalf, childWidth, layoutHeight + excessLenInHalf);
                }
            }
        }
    }

    public boolean isSurfaceAvailable() {
        return surfaceAvailable;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Surface getSurface() {
        return surface;
    }

    public void setAspectRatio(int width, int height) {
        //surfaceView.setAspectRatio(width, height);
        android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        surfaceView.setLayoutParams(lp);
    }

    public void setTransform(Matrix matrix) {
        //surfaceView.setTransform(matrix);
    }

    class surfaceCallback implements SurfaceHolder.Callback {


        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceAvailable = true;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            setCameraPreviewSize(width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            surfaceAvailable = false;
        }
    }

    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

}