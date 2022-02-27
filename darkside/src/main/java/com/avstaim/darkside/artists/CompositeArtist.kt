package com.avstaim.darkside.artists

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader

/**
 * Artist which contains of multiple artists.
 */
class CompositeArtist(private val artists: List<Artist>) : Artist {

    private inline fun forEachArtist(block: Artist.() -> Unit) = artists.forEach(block)

    override fun setSize(width: Float, height: Float) = forEachArtist { setSize(width, height) }
    override fun setSquareSize(size: Float) = forEachArtist { setSquareSize(size) }
    override fun setCenter(x: Int, y: Int) = forEachArtist { setCenter(x, y) }
    override fun setAlpha(alpha: Float) = forEachArtist { setAlpha(alpha) }
    override fun setRotation(rotation: Float) = forEachArtist { setRotation(rotation) }
    override fun setTranslation(dx: Float, dy: Float) = forEachArtist { setTranslation(dx, dy) }
    override fun setPaintStyle(style: Paint.Style) = forEachArtist { setPaintStyle(style) }
    override fun setColor(color: Int) = forEachArtist { setColor(color) }
    override fun setStrokeWidth(width: Float) = forEachArtist { setStrokeWidth(width) }
    override fun setPaint(paint: Paint) = forEachArtist { setPaint(paint) }
    override fun setShader(shader: Shader?) = forEachArtist { setShader(shader) }
    override fun setVisible(isVisible: Boolean) = forEachArtist { setVisible(isVisible) }

    override fun draw(canvas: Canvas) = forEachArtist { draw(canvas) }

    override fun getBounds(): RectF {
        val bounds = RectF(0f, 0f, 0f, 0f)

        forEachArtist {
            val childBounds = getBounds()

            bounds.left = Math.min(bounds.left, childBounds.left)
            bounds.top = Math.min(bounds.top, childBounds.top)
            bounds.right = Math.max(bounds.right, childBounds.right)
            bounds.bottom = Math.max(bounds.bottom, childBounds.bottom)
        }

        return bounds
    }

}
