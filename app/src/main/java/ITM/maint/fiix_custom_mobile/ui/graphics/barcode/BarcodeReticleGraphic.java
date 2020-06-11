package ITM.maint.fiix_custom_mobile.ui.graphics.barcode;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import androidx.core.content.ContextCompat;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.ui.graphics.barcode.BarcodeGraphicBase;
import ITM.maint.fiix_custom_mobile.ui.graphics.camera.CameraReticleAnimator;
import ITM.maint.fiix_custom_mobile.ui.view.GraphicOverlay;

public class BarcodeReticleGraphic extends BarcodeGraphicBase {

    private final CameraReticleAnimator animator;

    private final Paint ripplePaint;
    private final int rippleSizeOffset;
    private final int rippleStrokeWidth;
    private final int rippleAlpha;
    private final Point[] corners;

    public BarcodeReticleGraphic(GraphicOverlay overlay, CameraReticleAnimator animator, Point[] corners) {
        super(overlay);
        this.animator = animator;
        this.corners = corners;

        Resources resources = overlay.getResources();
        ripplePaint = new Paint();
        ripplePaint.setStyle(Paint.Style.STROKE);
        ripplePaint.setColor(ContextCompat.getColor(context, R.color.reticle_ripple));
        rippleSizeOffset =
                resources.getDimensionPixelOffset(R.dimen.barcode_reticle_ripple_size_offset);
        rippleStrokeWidth =
                resources.getDimensionPixelOffset(R.dimen.barcode_reticle_ripple_stroke_width);
        rippleAlpha = ripplePaint.getAlpha();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draws the ripple to simulate the breathing animation effect.
        ripplePaint.setAlpha((int) (rippleAlpha * animator.getRippleAlphaScale()));
        ripplePaint.setStrokeWidth(rippleStrokeWidth * animator.getRippleStrokeWidthScale());
        float offset = rippleSizeOffset * animator.getRippleSizeScale();
        RectF rippleRect =
                new RectF(
                        boxRect.left - offset,
                        boxRect.top - offset,
                        boxRect.right + offset,
                        boxRect.bottom + offset);
        canvas.drawRoundRect(rippleRect, boxCornerRadius, boxCornerRadius, ripplePaint);
        canvas.drawCircle(corners[0].x, corners[0].y, 2f, ripplePaint);
        canvas.drawCircle(corners[1].x, corners[1].y, 2f, ripplePaint);
        canvas.drawCircle(corners[2].x, corners[2].y, 2f, ripplePaint);
        canvas.drawCircle(corners[3].x, corners[3].y, 2f, ripplePaint);

    }
}
