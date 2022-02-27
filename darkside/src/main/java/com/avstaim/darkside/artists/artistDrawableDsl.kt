@file:Suppress("unused")

package com.avstaim.darkside.artists

import android.content.Context
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.graphics.Shader
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.google.android.material.color.MaterialColors

/**
 * Dsl used to create ArtistDrawable from Kotlin as easy as a pie.
 *
 *         (
 *     (   )  )
 *     )  ( )
 *     .....
 *  .:::::::::.
 * ~\_______/~
 */

fun Context.artistDrawable(init: ArtistDrawableBuilder.() -> Unit): ArtistDrawable<out Artist> {
    val builder = ArtistDrawableBuilder(this)
    builder.init()
    return builder.build()
}

fun Context.pathDrawable(init: PathArtistBuilder.() -> Unit): ArtistDrawable<PathArtist> =
    artistDrawable { pathArtist(init) } as ArtistDrawable<PathArtist>

fun Context.morphableDrawable(init: MorphablePathArtistDrawableBuilder.() -> Unit): MorphablePathArtistDrawable {
    val builder = MorphablePathArtistDrawableBuilder(this)
    builder.init()
    return builder.buildDrawable()
}

fun Context.pathData(@StringRes path: Int) = PathParser.createNodesFromPathData(getString(path))

fun Context.pathArtist(init: PathArtistBuilder.() -> Unit): PathArtist {
    return PathArtistBuilder(this).apply(init).build()
}

fun Context.circleArtist(init: CircleArtistBuilder.() -> Unit): CircleArtist {
    return CircleArtistBuilder(this).apply(init).build()
}

class ArtistDrawableBuilder internal constructor(private val context: Context) {

    private var artist: Artist? = null

    fun compositeArtist(init: CompositeArtistBuilder.() -> Unit) {
        val builder = CompositeArtistBuilder(context)
        builder.init()
        artist = builder.build()
    }

    fun pathArtist(init: PathArtistBuilder.() -> Unit) {
        val builder = PathArtistBuilder(context)
        builder.init()
        artist = builder.build()
    }

    var intrinsicWidth = -1
    var intrinsicHeight = -1
    var intrinsicSize = -1

    internal fun build(): ArtistDrawable<out Artist> {
        return artist?.let {
            ArtistDrawable(it).also { artistDrawable ->
                if (intrinsicSize > -1) {
                    artistDrawable.setIntrinsicSize(intrinsicSize, intrinsicSize)
                } else {
                    artistDrawable.setIntrinsicSize(intrinsicWidth, intrinsicHeight)
                }
            }
        } ?: throw IllegalStateException("No artist provided")
    }
}

abstract class ArtistBuilder(private val context: Context) {

    // null is for undefined and use default ones
    var width: Float? = null
    var height: Float? = null
    var size: Float? = null
    var center: Point? = null
    var alpha: Float? = null
    var rotation: Float? = null
    var translationX: Float? = null
    var translationY: Float? = null
    var paintStyle: Paint.Style? = null
    @ColorInt var color: Int? = null
    @ColorRes var colorRes: Int? = null
    @ArrayRes var colorAttr: Int? = null
    var strokeWidth: Float? = null
    var paint: Paint? = null
    var shader: Shader? = null
    var isVisible: Boolean? = null


    internal fun applyToArtist(artist: Artist) {
        width?.let { width ->
            height?.let { height ->
                artist.setSize(width, height)
            }
        }

        size?.let { artist.setSquareSize(it) }
        center?.let { artist.setCenter(it.x, it.y) }
        rotation?.let { artist.setRotation(it) }

        if (translationX != null || translationY != null) {
            val dx = translationX ?: 0f
            val dy = translationY ?: 0f
            artist.setTranslation(dx, dy)
        }

        paintStyle?.let { artist.setPaintStyle(it) }
        color?.let { artist.setColor(it) }
        colorRes?.let { artist.setColor(context.resources.getColor(it)) }
        colorAttr?.let { artist.setColor(MaterialColors.getColor(context, it, "ArtistBuilder")) }
        strokeWidth?.let { artist.setStrokeWidth(it) }
        paint?.let { artist.setPaint(it) }
        shader?.let { artist.setShader(it) }
        isVisible?.let { artist.setVisible(it) }

        alpha?.let { artist.setAlpha(it) }
    }
}

class CompositeArtistBuilder internal constructor(private val context: Context) : ArtistBuilder(context) {
    private val artists = mutableListOf<Artist>()

    private val compositeArtist = CompositeArtist(artists)

    fun pathArtist(init: PathArtistBuilder.() -> Unit) {
        val builder = PathArtistBuilder(context)
        builder.init()
        artists.add(builder.build())
    }

    fun circleArtist(init: CircleArtistBuilder.() -> Unit) {
        val builder = CircleArtistBuilder(context)
        builder.init()
        artists.add(builder.build())
    }

    fun artist(artist: Artist) = artists.add(artist)

    internal fun build(): CompositeArtist {
        return compositeArtist
    }
}


open class CircleArtistBuilder internal constructor(private val context: Context) : ArtistBuilder(context) {
    var radius: Float = -1f

    internal fun build(): CircleArtist {
        return CircleArtist().also { artist ->
            applyToArtist(artist)
            if (radius > 0) artist.setSquareSize(radius * 2)
        }
    }
}

open class PathArtistBuilder internal constructor(private val context: Context) : ArtistBuilder(context) {

    var path = ""

    var trimStart: Float = 0f
    var trimEnd: Float = 1f
    var trimOffset: Float = 0f

    var viewport: RectF? = null

    var viewportSize: Float?
        get() = viewport?.width()
        set(value) {
            onViewport {
                value?.let { size ->
                        left = 0f
                        top = 0f
                        right = size
                        bottom = size
                }
            }
        }

    var viewportWidth: Float?
        get() = viewport?.width()
        set(value) {
            onViewport {
                value?.let { width ->
                    left = 0f
                    right = width
                }
            }
        }

    var viewportHeight: Float?
        get() = viewport?.height()
        set(value) {
            onViewport {
                value?.let { height ->
                    top = 0f
                    bottom = height
                }
            }
        }

    var viewportLeft: Float?
        get() = viewport?.left
        set(value) {
            onViewport {
                value?.let { left = it }
            }
        }

    var viewportRight: Float?
        get() = viewport?.right
        set(value) {
            onViewport {
                value?.let { right = it }
            }
        }

    var viewportTop: Float?
        get() = viewport?.top
        set(value) {
            onViewport {
                value?.let { top = it }
            }
        }

    var viewportBottom: Float?
        get() = viewport?.bottom
        set(value) {
            onViewport {
                value?.let { bottom = it }
            }
        }

    @StringRes
    var pathId: Int = 0
        set(value) {
            field = value
            path = context.resources.getString(value)
        }

    var isMorphable = false

    private inline fun onViewport(init: RectF.() -> Unit) {
        if (viewport == null) {
            viewport = RectF()
        }
        viewport?.init()
    }

    internal fun build(): PathArtist {
        val artist = if (isMorphable) {
            MorphablePathArtist().apply { setPathData(PathParser.createNodesFromPathData(path)) }
        } else {
            PathArtist.forPathData(path)
        }

        applyToArtist(artist)

        artist.setTrim(trimStart, trimEnd, trimOffset)
        viewport?.run { artist.overrideViewPort(left, top, right, bottom) }

        return artist
    }
}

class MorphablePathArtistDrawableBuilder internal constructor(context: Context) : PathArtistBuilder(context) {

    internal fun buildDrawable(): MorphablePathArtistDrawable {
        val drawable = MorphablePathArtistDrawable(path)
        val artist = drawable.artist

        applyToArtist(artist)

        artist.setTrim(trimStart, trimEnd, trimOffset)
        viewport?.run { artist.overrideViewPort(left, top, right, bottom) }

        return drawable
    }
}
