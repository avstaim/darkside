package com.avstaim.darkside.test.recycler

import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import com.avstaim.darkside.cookies.ui.showToast
import com.avstaim.darkside.dsl.views.LayoutUi
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layouts.frameLayout
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.textColor
import com.avstaim.darkside.dsl.views.textView
import com.avstaim.darkside.slab.BindableSlab

class IntSlab(private val context: Context) : BindableSlab<FrameLayout, IntUi, TestRecyclerItem.IntItem>() {

    override val ui = IntUi(context)

    override suspend fun performBind(data: TestRecyclerItem.IntItem) {
        ui.root.onClick {
            context.showToast(data.i.toString())
        }

        ui.intText.text = data.i.toString()
    }
}

class IntUi(context: Context) : LayoutUi<FrameLayout>(context) {

    val intText = textView {
        textColor = Color.BLUE
    }

    override fun ViewBuilder.layout() =
        frameLayout {
            intText {}
        }
}
