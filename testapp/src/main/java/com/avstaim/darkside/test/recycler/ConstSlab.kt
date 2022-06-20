package com.avstaim.darkside.test.recycler

import android.content.Context
import android.widget.FrameLayout
import com.avstaim.darkside.cookies.ui.showToast
import com.avstaim.darkside.dsl.views.LayoutUi
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layouts.frameLayout
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.textView
import com.avstaim.darkside.slab.BindableSlab

class ConstSlab(private val context: Context) : BindableSlab<FrameLayout, ConstUi, TestRecyclerItem.ConstItem>() {

    override val ui = ConstUi(context)

    override suspend fun performBind(data: TestRecyclerItem.ConstItem) {
        ui.root.onClick {
            context.showToast("CONST")
        }
    }
}

class ConstUi(context: Context) : LayoutUi<FrameLayout>(context) {

    override fun ViewBuilder.layout() =
        frameLayout {
            textView {
                text = "CONST"
            }
        }
}
