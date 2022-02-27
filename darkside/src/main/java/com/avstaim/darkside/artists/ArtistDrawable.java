package com.avstaim.darkside.artists;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Wrapper for {@link Artist} into {@link Drawable}.
 */
public class ArtistDrawable<A extends Artist> extends Drawable {
    @NonNull
    private final A mArtist;

    private int mIntrinsicWidth = -1;
    private int mIntrinsicHeight = -1;

    public ArtistDrawable(@NonNull A artist) {
        mArtist = artist;
        mArtist.setVisible(true);
    }

    @NonNull
    public A getArtist() {
        return mArtist;
    }

    public void setIntrinsicSize(int width, int height) {
        mIntrinsicWidth = width;
        mIntrinsicHeight = height;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mArtist.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        mArtist.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(@NonNull Rect bounds) {
        int width = bounds.width();
        int height = bounds.height();
        onBoundsChange(width, height, bounds.left + width / 2, bounds.top + height / 2);
    }

    private void onBoundsChange(int width, int height, int centerX, int centerY) {
        mArtist.setSize(width, height);
        mArtist.setCenter(centerX, centerY);
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicHeight;
    }
}
