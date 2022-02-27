package com.avstaim.darkside.artists;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A lightweight alternative to {@link android.graphics.drawable.Drawable} class.
 */
public interface Artist {
    /**
     * Set bounds to draw into.
     *
     * @param width to resize
     * @param height to resize
     */
    void setSize(float width, float height);

    /**
     * Set square bounds to artist.
     *
     * @param size to resize (px)
     */
    void setSquareSize(float size);

    /**
     * Set center point to pin drawing.
     *
     * @param x coordinate of center point (px)
     * @param y coordinate of center point (px)
     */
    void setCenter(int x, int y);

    /**
     *
     * @param alpha set the alpha [0f..1f] of the drawing
     */
    void setAlpha(@FloatRange(from = 0f, to = 1f) float alpha);

    /**
     * @param rotation angle to rotate drawing
     */
    void setRotation(float rotation);

    /**
     * @param dx to translate from center point (px)
     * @param dy to translate from center point (px)
     */
    void setTranslation(float dx, float dy);

    /**
     * Tell artist to draw on the given canvas.
     *
     * @param canvas to draw into
     */
    @CalledOnEachFrame
    void draw(@NonNull Canvas canvas);

    /**
     * @return known bounds of drawing artist is about to draw
     */
    @NonNull
    RectF getBounds();

    /**
     * Set the paint's style, used for controlling how primitives'
     * geometries are interpreted (except for drawBitmap, which always assumes
     * Fill).
     *
     * @param style The new style to set in the paint
     */
    void setPaintStyle(@NonNull Paint.Style style);

    /**
     * Set the artist's color if applicable. Note that the color is an int containing alpha
     * as well as r,g,b. This 32bit value is not premultiplied, meaning that
     * its alpha can be any value, regardless of the values of r,g,b.
     * See the Color class for more details.
     *
     * @param color The new color (including alpha) to set in the paint.
     */
    void setColor(@ColorInt int color);

    /**
     * Set the width for stroking when {@link Paint.Style#STROKE} is used.
     * Pass 0 to stroke in hairline mode.
     * Hairlines always draws a single pixel independent of the canvas's matrix.
     *
     * @param width set the paint's stroke width, used whenever the paint's
     *              style is Stroke or StrokeAndFill.
     */
    void setStrokeWidth(float width);

    /**
     * Override paint of artist if possible.
     *
     * @param paint to override
     */
    void setPaint(@NonNull Paint paint);

    /**
     * Set shader to artist (not supported on some artists.
     *
     * @param shader to set
     */
    void setShader(@Nullable Shader shader);

    /**
     * Set visibility to artist's drawing.
     *
     * @param isVisible {@code true} to allow drawing, {@code false} to make all {@link #draw(Canvas)} calls to do nothing.
     */
    void setVisible(boolean isVisible);
}
