package com.avstaim.darkside.artists;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Artist, that can draw a simple circle.
 */
@SuppressWarnings("unused")
public class CircleArtist implements Artist {
    private static final int CLIP_WORKAROUND_PADDING = 2;
    private static final float GRADIENT_REFERENCE_RADIUS = 100f;

    @NonNull private final PointF mCenter = new PointF(0, 0);
    @NonNull private final PointF mTranslation = new PointF(0, 0);
    @NonNull private final Matrix mMatrix = new Matrix();

    @NonNull private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Nullable private LinearGradient mGradient;

    private float mRadius = 100f;

    private boolean mIsVisible;
    private boolean mShouldUseClipWorkaround;

    @Override
    public void setSize(float width, float height) {
        float diameter = Math.min(width, height);
        mRadius = diameter / 2;
        scaleGradientIfNeeded();
    }

    @Override
    public void setSquareSize(float size) {
        setSize(size, size);
    }

    @Override
    public void setCenter(int x, int y) {
        mCenter.set(x, y);
    }

    @Override
    public void setAlpha(@FloatRange(from = 0f, to = 1f) float alpha) {
        if (alpha > 1f) alpha = 1f;
        else if (alpha < 0f) alpha = 0f;

        int alphaRounded = Math.round(alpha * 255f);
        mPaint.setAlpha(alphaRounded);
    }

    @Override
    public void setRotation(float rotation) {
        // Do nothing, because rotated circle is the same circle
    }

    @Override
    public void setTranslation(float dx, float dy) {
        mTranslation.x = dx;
        mTranslation.y = dy;
    }

    /**
     * Draw path to given canvas.
     *
     * @param canvas to draw into
     */
    @Override
    @CalledOnEachFrame
    public void draw(@NonNull Canvas canvas) {
        if (!mIsVisible) {
            return;
        }

        boolean shouldTranslate = !mTranslation.equals(0, 0);

        if (shouldTranslate) {
            canvas.save();
            canvas.translate(mTranslation.x, mTranslation.y);
        }

        applyClipWorkaroundIfNeeded(canvas);
        canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);

        if (shouldTranslate) {
            canvas.restore();
        }
    }

    @NonNull
    @Override
    public RectF getBounds() {
        return new RectF(
                mCenter.x - mRadius,
                mCenter.y - mRadius,
                mCenter.x + mRadius,
                mCenter.y + mRadius
        );
    }

    @Override
    public void setPaintStyle(@NonNull Paint.Style style) {
        mPaint.setStyle(style);
    }

    @Override
    public void setColor(@ColorInt int color) {
        if (mGradient != null) {
            mPaint.setShader(null);
            mGradient = null;
        }
        mPaint.setColor(color);
    }

    @Override
    public void setStrokeWidth(float width) {
        mPaint.setStrokeWidth(width);
    }

    @Override
    public void setPaint(@NonNull Paint paint) {
        mPaint = paint;
    }

    @Override
    public void setShader(@Nullable Shader shader) {
        mPaint.setShader(shader);
    }

    @Override
    public void setVisible(boolean isVisible) {
        mIsVisible = isVisible;
    }

    public void enableCanvasClippingWorkaround() {
        // Android canvas has very aggressive optimisation, that clips drawing area limited to drawing bounds.
        // This causes outer oknyx circle to be clipped in BOTTOM-RIGHT for some pixels.

        mShouldUseClipWorkaround = true;
    }

    public void setAliceGradient(@NonNull @ColorInt int[] colors,
                          @NonNull GradientDrawable.Orientation orientation) {
        float size = GRADIENT_REFERENCE_RADIUS * 2;

        float x0, y0, x1, y1;

        switch (orientation) {
            case BL_TR:
                x0 = 0;
                y0 = size;
                x1 = size;
                y1 = 0;
                break;
            case BOTTOM_TOP:
                x0 = size / 2;
                y0 = size;
                x1 = size / 2;
                y1 = 0;
                break;
            case BR_TL:
                x0 = size;
                y0 = size;
                x1 = 0;
                y1 = 0;
                break;
            case LEFT_RIGHT:
                x0 = 0;
                y0 = size / 2;
                x1 = size;
                y1 = size / 2;
                break;
            case RIGHT_LEFT:
                x0 = size;
                y0 = size / 2;
                x1 = 0;
                y1 = size / 2;
                break;
            case TL_BR:
                x0 = 0;
                y0 = 0;
                x1 = size;
                y1 = size;
                break;
            case TOP_BOTTOM:
                x0 = size / 2;
                y0 = 0;
                x1 = size / 2;
                y1 = size;
                break;
            case TR_BL:
            default:
                x0 = size;
                y0 = 0;
                x1 = 0;
                y1 = size;
                break;
        }

        mGradient = new LinearGradient(x0, y0, x1, y1, colors, null, Shader.TileMode.MIRROR);
        scaleGradientIfNeeded();
        mPaint.setShader(mGradient);
    }

    @CalledOnEachFrame
    private void scaleGradientIfNeeded() {
        if (mGradient != null) {
            mMatrix.reset();
            float scale = mRadius / GRADIENT_REFERENCE_RADIUS;
            mMatrix.preTranslate(mCenter.x, mCenter.y);
            mMatrix.postScale(scale, scale, mCenter.x, mCenter.y);
            mGradient.setLocalMatrix(mMatrix);
        }
    }

    @CalledOnEachFrame
    private void applyClipWorkaroundIfNeeded(@NonNull Canvas canvas) {
        if (!mShouldUseClipWorkaround) {
            return;
        }

        float displacement = mRadius + CLIP_WORKAROUND_PADDING;

        canvas.save();
        canvas.clipRect(
                mCenter.x - displacement,
                mCenter.y - displacement,
                mCenter.x + displacement,
                mCenter.y + displacement
        );
        canvas.drawColor(Color.TRANSPARENT);
        canvas.restore();
    }
}
