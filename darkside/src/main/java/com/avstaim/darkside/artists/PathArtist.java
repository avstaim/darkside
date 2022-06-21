package com.avstaim.darkside.artists;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.avstaim.darkside.util.MathUtils;

/**
 * Artist, that can draw svg-like paths to given canvas.
 */
@SuppressWarnings("unused")
public class PathArtist implements Artist {

    @NonNull final Path mPath;
    @NonNull private final Path mTransformedPath;
    @NonNull private final Path mTrimmedPath;

    @NonNull private final RectF mSourceBounds = new RectF();
    @NonNull private final RectF mBounds = new RectF();
    @NonNull private final RectF mLastBounds = new RectF();
    @NonNull private final PointF mCenter = new PointF(0, 0);
    @NonNull private final PointF mTranslation = new PointF(0, 0);

    @NonNull private final Matrix mMatrix = new Matrix();

    @Nullable private RectF mOverriddenViewPort;

    private float mRotation = 0f;

    @NonNull private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Nullable private PathMeasure mPathMeasure;

    private float mTrimPathStart = 0f;
    private float mTrimPathEnd = 1f;
    private float mTrimPathOffset = 0f;

    boolean mIsPathDirty;
    private boolean mIsTrimDirty;
    private boolean mIsVisible;

    PathArtist() {
        mPath = new Path();
        mTransformedPath = new Path();
        mTrimmedPath = new Path();
    }

    protected PathArtist(@NonNull Path path) {
        mPath = path;
        mTransformedPath = new Path(mPath);
        mTrimmedPath = new Path(mPath);

        initBounds();

        mBounds.set(mSourceBounds);
    }

    public static PathArtist forPathData(@NonNull String pathData) {
        Path path = PathParser.createPathFromPathData(pathData);
        if (path == null) {
            //error("Path parse error");
            path = new Path();
        }
        return new PathArtist(path);
    }

    final void initBounds() {
        mPath.computeBounds(mSourceBounds, true);
        normalizeSourceBounds();
    }

    @Override
    public void setSize(float width, float height) {
        if (MathUtils.isEqual(mBounds.width(), width) && MathUtils.isEqual(mBounds.height(), height)) {
            return;
        }

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
        mIsPathDirty = true;
    }

    @Override
    public void setSquareSize(float size) {
        setSize(size, size);
    }

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
        mIsPathDirty = true;
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
        mRotation = rotation;
    }

    @Override
    public void setTranslation(float dx, float dy) {
        mTranslation.x = dx;
        mTranslation.y = dy;
    }

    public void setTrim(float start, float end, float offset) {
        if (start != mTrimPathStart || end != mTrimPathEnd || offset != mTrimPathOffset) {
            mTrimPathStart = start;
            mTrimPathEnd = end;
            mTrimPathOffset = offset;
            mIsTrimDirty = true;
        }
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

        transformPathIfNeeded();

        boolean shouldTranslate = !mTranslation.equals(0, 0);
        boolean shouldRotate = !MathUtils.isEqual(mRotation, 0f);
        boolean shouldSaveLoadCanvas = shouldRotate || shouldTranslate;

        if (shouldSaveLoadCanvas) {
            canvas.save();
        }
        if (shouldTranslate) {
            canvas.translate(mTranslation.x, mTranslation.y);
        }
        if (shouldRotate) {
            canvas.rotate(mRotation, mCenter.x, mCenter.y);
        }

        Path pathToDraw = hasTrim() ? mTrimmedPath : mTransformedPath;
        canvas.drawPath(pathToDraw, mPaint);

        if (shouldSaveLoadCanvas) {
            canvas.restore();
        }
    }

    @NonNull
    @Override
    public RectF getBounds() {
        return mBounds;
    }

    @Override
    public void setPaintStyle(@NonNull Paint.Style style) {
        mPaint.setStyle(style);
    }

    @Override
    public void setColor(@ColorInt int color) {
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

    public void overrideViewPort(float left, float top, float right, float bottom) {
        mOverriddenViewPort = new RectF(left, top, right, bottom);
    }

    public void resetViewPort() {
        mOverriddenViewPort = null;
    }

    private void normalizeSourceBounds() {
        float width = mSourceBounds.width();
        float height = mSourceBounds.height();

        if (width > height) {
            float dy = (width - height) / 2;
            mSourceBounds.inset(0, -dy);
        } else if (width < height) {
            float dx = (height - width) / 2;
            mSourceBounds.inset(-dx, 0);
        }
    }

    @CalledOnEachFrame
    private void transformPathIfNeeded() {
        if (mIsPathDirty) {
            transformPath();
        } else if (mIsTrimDirty) {
            trimPathIfNeeded();
        }
        mIsPathDirty = false;
        mIsTrimDirty = false;
    }

    @CalledOnEachFrame
    private void transformPath() {
        mMatrix.reset();

        RectF sourceBounds = mOverriddenViewPort != null ? mOverriddenViewPort : mSourceBounds;
        mMatrix.setRectToRect(sourceBounds, mBounds, Matrix.ScaleToFit.FILL);

        mPath.transform(mMatrix, mTransformedPath);
        trimPathIfNeeded();

        mLastBounds.set(mBounds);

        scaleShaderIfNeeded();
    }

    /**
     * This method code is taken from {@link androidx.vectordrawable.graphics.drawable.VectorDrawableCompat}. Considered to be stable and final.
     * Should not be modified or statically analyzed. NOSONAR!
     *
     * Copyright (C) 2015 The Android Open Source Project Licensed under the Apache License, Version 2.0
     */
    @SuppressWarnings("ALL") // is a fork from AOSP, no modifications without real need
    private void trimPathIfNeeded() {
        if (hasTrim()) {
            float start = (mTrimPathStart + mTrimPathOffset) % 1.0f;
            float end = (mTrimPathEnd + mTrimPathOffset) % 1.0f;

            if (mPathMeasure == null) {
                mPathMeasure = new PathMeasure();
            }
            mPathMeasure.setPath(mTransformedPath, false);

            float len = mPathMeasure.getLength();
            start = start * len;
            end = end * len;
            mTrimmedPath.reset();
            if (start > end) {
                mPathMeasure.getSegment(start, len, mTrimmedPath, true);
                mPathMeasure.getSegment(0f, end, mTrimmedPath, true);
            } else {
                mPathMeasure.getSegment(start, end, mTrimmedPath, true);
            }
            mTrimmedPath.rLineTo(0, 0); // fix bug in measure
        }
    }

    private boolean hasTrim() {
        return mTrimPathStart != 0.0f || mTrimPathEnd != 1.0f;
    }

    private void scaleShaderIfNeeded() {
        Shader shader = mPaint.getShader();
        if (shader != null) {
            shader.setLocalMatrix(mMatrix);
        }
    }
}
