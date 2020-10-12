// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.yandex.darkside.views.layouts.constraint

import android.graphics.Rect
import android.view.View
import androidx.transition.*
import com.yandex.darkside.cookies.noGetter

/**
 * Used for fast building a different transitions from [androidx.transition.Transition].
 * Can be used for [applyConstraintsWithTransition] DSL.
 */

object Transitions {

    val AUTO = AutoTransition()

    fun auto(init: AutoTransition.() -> Unit) = AutoTransition().also(init)

    fun explode(init: Explode.() -> Unit) = Explode().also(init)

    fun fade(init: Fade.() -> Unit) = Fade().also(init)

    fun slide(init: Slide.() -> Unit) = Slide().also(init)

    fun transitionSet(init: TransitionSetBuilder.() -> Unit): TransitionSet = TransitionSetBuilder().also(init)
}

var Transition.viewEpicenter: View
    get() = noGetter()
    set(view) {
        epicenterCallback = object : Transition.EpicenterCallback() {
            private val epicenter = Rect()

            override fun onGetEpicenter(transition: Transition): Rect {
                return epicenter.apply {
                    left = view.left
                    top = view.top
                    right = view.right
                    bottom = view.bottom
                }
            }
        }
    }

class TransitionSetBuilder : TransitionSet() {

    fun auto(init: AutoTransition.() -> Unit) = Transitions.auto(init).also { addTransition(it) }

    fun explode(init: Explode.() -> Unit) = Transitions.explode(init).also { addTransition(it) }

    fun fade(init: Fade.() -> Unit) = Transitions.fade(init).also { addTransition(it) }

    fun slide(init: Slide.() -> Unit) = Transitions.slide(init).also { addTransition(it) }
}
