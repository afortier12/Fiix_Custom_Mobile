/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ITM.maint.fiix_custom_mobile.ui.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * A view which renders a series of custom graphics to be overlaid on top of an associated preview
 * (i.e., the camera preview). The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 *
 * <p>Supports scaling and mirroring of the graphics relative the camera's preview properties. The
 * idea is that detection items are expressed in terms of a preview size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 *
 * <p>Associated {@link Graphic} items should use {@link #translateX(float)} and {@link
 * #translateY(float)} to convert to view coordinate from the preview's coordinate.
 */
public class GraphicOverlay extends View {
    private final Object lock = new Object();

    private int previewWidth;
    private float widthScaleFactor = 1.0f;
    private int previewHeight;
    private float heightScaleFactor = 1.0f;
    private final List<Graphic> graphics = new ArrayList<>();

    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instances to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */
    public abstract static class Graphic {
        protected final GraphicOverlay overlay;
        protected final Context context;

        protected Graphic(GraphicOverlay overlay) {
            this.overlay = overlay;
            this.context = overlay.getContext();
        }

        /** Draws the graphic on the supplied canvas. */
        protected abstract void draw(Canvas canvas);
    }

    public GraphicOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.bringToFront();
    }

    /** Removes all graphics from the overlay. */
    public void clear() {
        synchronized (lock) {
            graphics.clear();
        }
    }

    /** Adds a graphic to the overlay. */
    public void add(Graphic graphic) {
        synchronized (lock) {
            graphics.add(graphic);
        }
    }


    public float translateX(float x) {
        return x * widthScaleFactor;
    }

    public float translateY(float y) {
        return y * heightScaleFactor;
    }

    /**
     * Adjusts the {@code rect}'s coordinate from the preview's coordinate system to the view
     * coordinate system.
     */
    public RectF translateRect(Rect rect) {
        return new RectF(
                translateX(rect.left),
                translateY(rect.top),
                translateX(rect.right),
                translateY(rect.bottom));
    }

    /** Draws the overlay with its associated graphic objects. */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (previewWidth > 0 && previewHeight > 0) {
            widthScaleFactor = (float) getWidth() / previewWidth;
            heightScaleFactor = (float) getHeight() / previewHeight;
        }

        synchronized (lock) {
            for (Graphic graphic : graphics) {
                graphic.draw(canvas);
            }
        }
        this.bringToFront();
    }

    public static boolean isPortraitMode(Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }
}