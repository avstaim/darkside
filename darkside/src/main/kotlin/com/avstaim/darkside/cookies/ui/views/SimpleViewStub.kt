package com.avstaim.darkside.cookies.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.avstaim.darkside.cookies.delegates.weak

/**
 * An alternative to [android.view.ViewStub], that does not require xml inflating scam.
 */
class SimpleViewStub @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var replacedId = NO_ID

    @Suppress("MemberVisibilityCanBePrivate")
    var viewSupplier: (() -> View)? = null

    private var replacedViewRef by weak<View>(null)

    init {
        visibility = GONE
        setWillNotDraw(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = setMeasuredDimension(0, 0)

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas) = Unit
    override fun dispatchDraw(canvas: Canvas) = Unit

    override fun setVisibility(visibility: Int) {
        if (replacedViewRef != null) {
            replacedViewRef?.visibility = visibility
        } else {
            super.setVisibility(visibility)
            if (visibility == VISIBLE || visibility == INVISIBLE) {
                replace()
            }
        }
    }

    /**
     * Replaces this StubbedView in its parent by the view supplied in [viewSupplier].
     *
     * @return replaced view.
     */
    fun replace(): View {
        (parent as? ViewGroup)?.let { viewParent ->
            viewSupplier?.invoke()?.let { view ->
                replaceSelfWithView(view, viewParent)
                setId(view)
                replacedViewRef = view
                viewSupplier = null
                return view
            } ?: throw IllegalArgumentException("ViewStub must have a valid viewSupplier")

        } ?: throw IllegalStateException("ViewStub must have a non-null ViewGroup viewParent")
    }

    /**
     * Replaces this StubbedView in its parent by the given view.
     *
     * @param view to replace this stub to
     * @return replaced view.
     */
    fun replace(view: View): View {
        viewSupplier = { view }
        return replace()
    }

    private fun replaceSelfWithView(view: View, parent: ViewGroup) {
        val index = parent.indexOfChild(this)
        parent.removeViewInLayout(this)

        val layoutParams = layoutParams
        if (layoutParams != null) {
            parent.addView(view, index, layoutParams)
        } else {
            parent.addView(view, index)
        }
    }

    private fun setId(view: View) {
        if (replacedId != NO_ID) {
            view.id = replacedId
        } else if (id != NO_ID) {
            view.id = id
            id = NO_ID
        }
    }
}
