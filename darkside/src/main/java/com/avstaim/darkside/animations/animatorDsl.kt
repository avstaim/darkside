@file:Suppress("unused")

package com.avstaim.darkside.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.FloatRange
import com.avstaim.darkside.artists.Artist
import com.avstaim.darkside.artists.ArtistDrawable
import com.avstaim.darkside.artists.MorphablePathArtist
import com.avstaim.darkside.artists.MorphablePathArtistDrawable
import com.avstaim.darkside.artists.PathArtist
import com.avstaim.darkside.artists.PathDataEvaluator
import com.avstaim.darkside.artists.PathParser
import com.avstaim.darkside.cookies.time.CommonTime
import kotlin.math.roundToInt

fun animator(init: DslAnimatorBuilder.() -> Unit): ValueAnimator {
    return DslAnimatorBuilder().also { it.init() }
}

fun animatorSet(init: AnimatorSet.() -> Unit): AnimatorSet {
    return AnimatorSet().also { it.init() }
}

fun AnimatorSet.cycleAnimations() = addListener(CycleAnimationListener())

fun AnimatorSet.playSequentially(init: AnimatorSetBuilder.() -> Unit) {
    AnimatorSetBuilder().also {
        it.init()
        this.playSequentially(it.animators)
    }
}

fun AnimatorSet.playTogether(init: AnimatorSetBuilder.() -> Unit) {
    AnimatorSetBuilder().also {
        it.init()
        this.playTogether(it.animators)
    }
}

@DslMarker
annotation class AnimatorDslMarker

@AnimatorDslMarker
class DslAnimatorBuilder : DslAnimator() {

    fun targets(init: DslTargetBuilder.() -> Unit) {
        DslTargetBuilder(::accumulate).apply(init)
    }
}

@AnimatorDslMarker
class DslTargetBuilder internal constructor(private val accumulator: (AnimationActor) -> Unit) {

    fun custom(target: (Float) -> Unit) = accumulator.invoke(simpleActor(target))

    fun custom(from: Float, to: Float, target: (Float) -> Unit) = accumulator.onNewValue(from, to, target)

    fun custom(from: Int, to: Int, target: (Int) -> Unit) =
        accumulator.onNewValue(from.toFloat(), to.toFloat()) { target(it.roundToInt()) }

    operator fun View.invoke(init: ViewAnimatorBuilder.() -> Unit) {
        ViewAnimatorBuilder(this, accumulator).apply(init)
    }

    operator fun Artist.invoke(init: ArtistAnimatorBuilder.() -> Unit) {
        ArtistAnimatorBuilder(this, accumulator).apply(init)
    }

    fun PathArtist.asPathArtist(init: PathArtistAnimatorBuilder.() -> Unit) {
        PathArtistAnimatorBuilder(this, accumulator).apply(init)
    }

    fun MorphablePathArtist.asMorphableArtist(init: MorphablePathArtistAnimatorBuilder.() -> Unit) {
        MorphablePathArtistAnimatorBuilder(this, accumulator).apply(init)
    }

    operator fun MorphablePathArtistDrawable.invoke(init: MorphablePathArtistDrawableAnimatorBuilder.() -> Unit) {
        MorphablePathArtistDrawableAnimatorBuilder(this, accumulator).apply(init)
    }

    @JvmName("invokeSimple")
    operator fun ArtistDrawable<Artist>.invoke(init: ArtistAnimatorBuilder.() -> Unit) {
        ArtistAnimatorBuilder(this.artist, accumulator).apply(init)
        accumulator.invoke(simpleActor { this.invalidateSelf() })
    }

    @JvmName("invokePath")
    operator fun ArtistDrawable<PathArtist>.invoke(init: PathArtistAnimatorBuilder.() -> Unit) {
        PathArtistAnimatorBuilder(this.artist, accumulator).apply(init)
        accumulator.invoke(simpleActor { this.invalidateSelf() })
    }

    @JvmName("invokeMorphable")
    operator fun ArtistDrawable<MorphablePathArtist>.invoke(init: MorphablePathArtistAnimatorBuilder.() -> Unit) {
        MorphablePathArtistAnimatorBuilder(this.artist, accumulator).apply(init)
        accumulator.invoke(simpleActor { this.invalidateSelf() })
    }

    fun invalidate(vararg targets: Drawable) =
        accumulator.invoke(simpleActor { targets.forEach { it.invalidateSelf() }})

    fun invalidate(vararg targets: View) =
        accumulator.invoke(simpleActor { targets.forEach { it.invalidate() }})

    fun customColor(from: Int, to: Int, target: (Int) -> Unit) {
        val fA = Color.alpha(from)
        val fR = Color.red(from)
        val fG = Color.green(from)
        val fB = Color.blue(from)

        val tA = Color.alpha(to)
        val tR = Color.red(to)
        val tG = Color.green(to)
        val tB = Color.blue(to)

        accumulator.invoke(
            simpleActor { step ->
                val cA = (fA + (tA - fA) * step).toInt()
                val cR = (fR + (tR - fR) * step).toInt()
                val cG = (fG + (tG - fG) * step).toInt()
                val cB = (fB + (tB - fB) * step).toInt()

                target.invoke(Color.argb(cA, cR, cG, cB))
            }
        )
    }
    fun customColor(pair: Pair<Int, Int>, target: (Int) -> Unit) =
        customColor(from = pair.first, to = pair.second, target = target)
}

@AnimatorDslMarker
class ViewAnimatorBuilder internal constructor(
    private val view: View,
    private val accumulator: (AnimationActor) -> Unit
) {

    fun alpha(from: Float = view.alpha, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> view.alpha = newValue }
    fun alpha(pair: Pair<Float, Float>) = alpha(from = pair.first, to = pair.second)

    fun translationX(from: Float = view.translationX, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> view.translationX = newValue }
    fun translationX(pair: Pair<Float, Float>) = translationX(from = pair.first, to = pair.second)
    fun translationXRelative(relativeX: Float) =
            translationX(to = view.translationX + relativeX)

    fun translationY(from: Float = view.translationY, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> view.translationY = newValue }
    fun translationY(pair: Pair<Float, Float>) = translationY(from = pair.first, to = pair.second)
    fun translationYRelative(relativeY: Float) =
        translationY(to = view.translationY + relativeY)

    fun translationX(from: Int = view.translationX.roundToInt(), to: Int) =
        translationX(from = from.toFloat(), to = to.toFloat())
    @JvmName("translationXI")
    fun translationX(pair: Pair<Int, Int>) = translationX(from = pair.first, to = pair.second)
    fun translationXRelative(relativeX: Int) =
        translationX(to = view.translationX + relativeX)

    fun translationY(from: Int = view.translationY.roundToInt(), to: Int) =
        translationY(from = from.toFloat(), to = to.toFloat())
    @JvmName("translationYI")
    fun translationY(pair: Pair<Int, Int>) = translationY(from = pair.first, to = pair.second)
    fun translationYRelative(relativeY: Int) =
        translationY(to = view.translationY + relativeY)

    fun rotation(from: Float = view.rotation, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> view.rotation = newValue }
    fun rotation(pair: Pair<Float, Float>) = rotation(from = pair.first, to = pair.second)
}

@AnimatorDslMarker
open class ArtistAnimatorBuilder internal constructor(
    private val artist: Artist,
    private val accumulator: (AnimationActor) -> Unit
) {

    fun size(from: Float, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> artist.setSquareSize(newValue) }
    fun size(pair: Pair<Float, Float>) = size(from = pair.first, to = pair.second)

    fun alpha(from: Float, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> artist.setAlpha(newValue) }
    fun alpha(pair: Pair<Float, Float>) = alpha(from = pair.first, to = pair.second)

    fun rotation(from: Float, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> artist.setRotation(newValue) }
    fun rotation(pair: Pair<Float, Float>) = rotation(from = pair.first, to = pair.second)

    fun strokeWidth(from: Float, to: Float) =
        accumulator.onNewValue(from, to) { newValue -> artist.setStrokeWidth(newValue) }
    fun strokeWidth(pair: Pair<Float, Float>) = strokeWidth(from = pair.first, to = pair.second)

    fun translation(x: Pair<Float, Float>, y: Pair<Float, Float>) {
        accumulator.invoke(
            simpleActor { step ->
                val currentX = x.first + (x.second - x.first) * step
                val currentY = y.first + (y.second - y.first) * step
                artist.setTranslation(currentX, currentY)
            }
        )
    }
    @JvmName("translationInt")
    fun translation(x: Pair<Int, Int>, y: Pair<Int, Int>) {
        accumulator.invoke(
            simpleActor { step ->
                val currentX = x.first + (x.second - x.first) * step
                val currentY = y.first + (y.second - y.first) * step
                artist.setTranslation(currentX, currentY)
            }
        )
    }

    fun color(from: Int, to: Int) {
        val fA = Color.alpha(from)
        val fR = Color.red(from)
        val fG = Color.green(from)
        val fB = Color.blue(from)

        val tA = Color.alpha(to)
        val tR = Color.red(to)
        val tG = Color.green(to)
        val tB = Color.blue(to)

        accumulator.invoke(
            simpleActor { step ->
                val cA = (fA + (tA - fA) * step).toInt()
                val cR = (fR + (tR - fR) * step).toInt()
                val cG = (fG + (tG - fG) * step).toInt()
                val cB = (fB + (tB - fB) * step).toInt()

                artist.setColor(Color.argb(cA, cR, cG, cB))
            }
        )
    }
    fun color(pair: Pair<Int, Int>) = color(from = pair.first, to = pair.second)
}

@AnimatorDslMarker
open class PathArtistAnimatorBuilder internal constructor(
    private val artist: PathArtist,
    private val accumulator: (AnimationActor) -> Unit
) : ArtistAnimatorBuilder(artist, accumulator) {

    fun trim(start: Pair<Float, Float> = 0f to 0f,
             end: Pair<Float, Float> = 1f to 1f,
             offset: Pair<Float, Float> = 0f to 0f) {
        accumulator.invoke(
            simpleActor { step ->
                val currentStart = start.first + (start.second - start.first) * step
                val currentEnd = end.first + (end.second - end.first) * step
                val currentOffset = offset.first + (offset.second - offset.first) * step
                artist.setTrim(currentStart, currentEnd, currentOffset)
            }
        )
    }
}

@AnimatorDslMarker
open class MorphablePathArtistAnimatorBuilder internal constructor(
    private val artist: MorphablePathArtist,
    private val accumulator: (AnimationActor) -> Unit
) : PathArtistAnimatorBuilder(artist, accumulator) {

    fun Context.path(pair: Pair<Int, Int>) = path(from = pair.first, to = pair.second)
    fun Context.path(from: Int, to: Int) = path(resources.getString(from), resources.getString(to))

    fun path(pair: Pair<String, String>) = path(from = pair.first, to = pair.second)
    fun path(from: String, to: String) =
        path(PathParser.createNodesFromPathData(from), PathParser.createNodesFromPathData(to))

    fun path(from: Array<PathParser.PathDataNode>, to: Array<PathParser.PathDataNode>) {
        if (PathParser.canMorph(from, to)) {
            val evaluator = PathDataEvaluator()
            accumulator.invoke(
                simpleActor { step -> artist.setPathData(evaluator.evaluate(step, from, to)) }
            )
        } else {
            accumulator.invoke(object : AnimationActor {
                override fun start() = artist.setPathData(from)
                override fun end() = artist.setPathData(to)
            })
        }
    }
}

@AnimatorDslMarker
class MorphablePathArtistDrawableAnimatorBuilder internal constructor(
    drawable: MorphablePathArtistDrawable,
    accumulator: (AnimationActor) -> Unit
) : PathArtistAnimatorBuilder(drawable.artist, accumulator)

abstract class DslAnimator : ValueAnimator() {

    private var externalListener: AnimatorListener? = null
    private var externalUpdateListener: AnimatorUpdateListener? = null

    private lateinit var animationActors: Array<AnimationActor>
    private val animationActorAccumulator = mutableListOf<AnimationActor>()

    private var onStart: (() -> Unit)? = null
    private var onEnd: (() -> Unit)? = null
    private var onCancel: (() -> Unit)? = null
    private var onRepeat: (() -> Unit)? = null

    var durationTime: CommonTime
        get() = CommonTime(millis = duration)
        set(value) {
            duration = value.millis
        }

    init {
        @Suppress("LeakingThis")
        setFloatValues(0f, 1f)
        super.addUpdateListener(::onUpdate)
        super.addListener(AnimatorListenerImpl())
    }

    fun onStart(block: () -> Unit) {
        onStart = block
    }

    fun onEnd(block: () -> Unit) {
        onEnd = block
    }

    fun onCancel(block: () -> Unit) {
        onCancel = block
    }

    fun onRepeat(block: () -> Unit) {
        onRepeat = block
    }

    override fun addUpdateListener(listener: AnimatorUpdateListener?) {
        externalUpdateListener = listener
    }

    override fun addListener(listener: AnimatorListener?) {
        externalListener = listener
    }

    override fun start() {
        animationActors = animationActorAccumulator.toTypedArray()
        super.start()
    }

    protected fun accumulate(actor: AnimationActor) {
        animationActorAccumulator.add(actor)
    }

    private fun onUpdate(animator: ValueAnimator) {
        externalUpdateListener?.onAnimationUpdate(animator)
        val step = animator.animatedValue as Float
        animationActors.forEach { it.frame(step) }
    }

    private inner class AnimatorListenerImpl : AnimatorListener {

        override fun onAnimationStart(animator: Animator?) {
            externalListener?.onAnimationStart(animator)
            onStart?.invoke()
            animationActors.forEach { it.start() }
        }

        override fun onAnimationEnd(animator: Animator?) {
            externalListener?.onAnimationEnd(animator)
            onEnd?.invoke()
            animationActors.forEach { it.end() }
        }

        override fun onAnimationRepeat(animator: Animator?) {
            externalListener?.onAnimationRepeat(animator)
            onRepeat?.invoke()
        }

        override fun onAnimationCancel(animator: Animator?) {
            externalListener?.onAnimationCancel(animator)
            onCancel?.invoke()
        }
    }
}

private fun simpleActor(simple: (Float) -> Unit) = object : AnimationActor {
    override fun frame(step: Float) = simple(step)
}

private inline fun ((AnimationActor) -> Unit).onNewValue(from: Float, to: Float,
                                                         crossinline newValue: (Float) -> Unit) {
    invoke(simpleActor { step -> newValue(from + (to - from) * step) })
}

interface AnimationActor {
    fun start() = Unit
    fun frame(@FloatRange(from = 0.0, to = 1.0) step: Float) = Unit
    fun end() = Unit
}

private class CycleAnimationListener : AnimatorListenerAdapter() {
    private var canceled = false

    override fun onAnimationStart(animation: Animator) {
        canceled = false
    }

    override fun onAnimationCancel(animation: Animator) {
        canceled = true
    }

    override fun onAnimationEnd(animation: Animator) {
        if (!canceled) {
            animation.start()
        }
    }
}

@AnimatorDslMarker
class AnimatorSetBuilder internal constructor() {
    internal val animators = arrayListOf<Animator>()

    fun animator(init: DslAnimatorBuilder.() -> Unit) {
        DslAnimatorBuilder().also {
            it.init()
            animators.add(it)
        }
    }

    @SuppressLint("Recycle")
    fun animatorSet(init: AnimatorSet.() -> Unit) {
        AnimatorSet().also {
            it.init()
            animators.add(it)
        }
    }
}
