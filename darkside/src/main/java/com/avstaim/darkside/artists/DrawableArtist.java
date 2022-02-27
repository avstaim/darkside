package com.avstaim.darkside.artists;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Artist, that can draw svg-like paths to given canvas.
 */
@SuppressWarnings("unused")
public class DrawableArtist implements Artist {

    @NonNull private final Drawable mDrawable;

    @NonNull private final RectF mBounds = new RectF();
    @NonNull private final RectF mBoundsWithInset = new RectF();
    @NonNull private final PointF mCenter = new PointF(0, 0);
    @NonNull private final PointF mInset = new PointF(0, 0);
    @NonNull private final PointF mTranslation = new PointF(0, 0);

    private float mRotation = 0f;
    private boolean mIsVisible;

    public DrawableArtist(@NonNull Drawable drawable) {
        mDrawable = drawable;

        mBounds.set(mDrawable.getBounds());
    }

    /**
     * Set bounds to draw path into.
     *
     * @param width to resize
     * @param height to resize
     */
    @Override
    public void setSize(float width, float height) {
        if (mCenter.equals(0, 0)) {
            mBounds.set(0, 0, width, height);
        } else {
            mBounds.set(
                    mCenter.x - width / 2,
                    mCenter.y - height / 2,
                    mCenter.x + width / 2,
                    mCenter.y + height / 2
            );
        }
        applyBounds();
    }

    /**
     * Set square bounds to artist.
     *
     * @param size to resize
     */
    @Override
    public void setSquareSize(float size) {
        setSize(size, size);
    }

    /**
     * Set center point to pin drawing.
     *
     * @param x of center point
     * @param y of center point
     */
    @Override
    public void setCenter(int x, int y) {
        mCenter.set(x, y);

        float width = mBounds.width();
        float height = mBounds.height();

        if (mCenter.equals(0, 0)) {
            mBounds.set(0, 0, width, height);
        } else {
            mBounds.set(x - width / 2, y - height / 2, x + width / 2, y + height / 2);
        }
        applyBounds();
    }

    /**
     *
     * @param alpha set the alpha [0f..1f] of the drawing
     */
    @Override
    public void setAlpha(@FloatRange(from = 0f, to = 1f) float alpha) {
        if (alpha > 1f) alpha = 1f;
        else if (alpha < 0f) alpha = 0f;

        int alphaRounded = Math.round(alpha * 255f);
        mDrawable.setAlpha(alphaRounded);
    }

    @Override
    public void setRotation(float rotation) {
        mRotation = rotation;
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
        boolean shouldRotate = !MathUtils.isEqual(mRotation, 0f);

        canvas.save();
        if (shouldTranslate) {
            canvas.translate(mTranslation.x, mTranslation.y);
        }
        if (shouldRotate) {
            canvas.rotate(mRotation, mCenter.x, mCenter.y);
        }
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @NonNull
    @Override
    public RectF getBounds() {
        return mBounds;
    }

    @Override
    public void setPaintStyle(@NonNull Paint.Style style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColor(int color) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStrokeWidth(float width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPaint(@NonNull Paint paint) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setShader(@Nullable Shader shader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVisible(boolean isVisible) {
        mIsVisible = isVisible;
    }

    void inset(float dx, float dy) {
        mInset.set(dx, dy);
    }

    private void applyBounds() {
        mBoundsWithInset.set(mBounds);
        mBoundsWithInset.inset(mInset.x, mInset.y);
        mDrawable.setBounds(
                (int) mBoundsWithInset.left,
                (int) mBoundsWithInset.top,
                (int) mBoundsWithInset.right,
                (int) mBoundsWithInset.bottom
        );
    }
}
