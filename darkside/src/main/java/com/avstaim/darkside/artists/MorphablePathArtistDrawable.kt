@file:Suppress("unused")

package com.avstaim.darkside.artists

import android.animation.Animator
import android.animation.ValueAnimator
import com.avstaim.darkside.cookies.dp

typealias PathData = Array<PathParser.PathDataNode>

/**
 * Drawable, that cam morph paths without xml scam.
 */
class MorphablePathArtistDrawable(path: String = "") : ArtistDrawable<MorphablePathArtist>(MorphablePathArtist()) {

    private var pathData = PathParser.createNodesFromPathData(path)
        set(value) {
            field = value
            artist.setPathData(value)
        }

    private var intrinsicWidth = -1
    private var intrinsicHeight = -1

    init {
        artist.setPathData(pathData)
    }

    fun animatePath(from: PathData? = null,
                    to: PathData,
                    duration: Long = DEFAULT_DURATION
    ): Animator? {
        val realFrom = from ?: pathData
        if (PathParser.canMorph(realFrom, to)) {
            return ValueAnimator.ofObject(PathDataEvaluator(), realFrom, to).apply {
                setDuration(duration)
                setPathDataListener {
                    pathData = it
                    invalidateSelf()
                }
                start()
            }
        }
        pathData = to
        invalidateSelf()
        return null
    }

    fun overrideViewPort(left: Float, top: Float, right: Float, bottom: Float) {
        artist.overrideViewPort(left, top, right, bottom)
        intrinsicWidth = dp(right - left)
        intrinsicHeight = dp(bottom - top)
    }

    override fun getIntrinsicWidth() = intrinsicWidth
    override fun getIntrinsicHeight() = intrinsicHeight

    @Suppress("UNCHECKED_CAST")
    private fun ValueAnimator.setPathDataListener(listener: (PathData) -> Unit) =
        addUpdateListener { listener(animatedValue as PathData) }

    companion object {
        const val DEFAULT_DURATION = 300L
    }
}
