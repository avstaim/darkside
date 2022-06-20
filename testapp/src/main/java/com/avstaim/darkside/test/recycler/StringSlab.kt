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

class StringSlab(private val context: Context) : BindableSlab<FrameLayout, StringUi, TestRecyclerItem.StringItem>() {

    override val ui = StringUi(context)

    override suspend fun performBind(data: TestRecyclerItem.StringItem) {
        ui.root.onClick {
            context.showToast(data.s)
        }

        ui.stringText.text = data.s
    }
}

class StringUi(context: Context) : LayoutUi<FrameLayout>(context) {

    val stringText = textView {
        textColor = Color.GREEN
    }

    override fun ViewBuilder.layout() =
        frameLayout {
            stringText {}
        }
}
