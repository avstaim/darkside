package com.avstaim.darkside.slab

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.avstaim.darkside.dsl.views.Ui
import com.avstaim.darkside.dsl.views.slot

open class SlotUi(context: Context) : Ui<View> {

    final override val ctx: Context = context

    val slot = slot(SlotView(context))

    final override val root: View
        get() = slot.currentView

    inline fun layoutParams(initLp: View.() -> ViewGroup.LayoutParams) {
        val view = root
        val lp = view.initLp()
        view.layoutParams = lp
    }
}