package com.avstaim.darkside.test.recycler

import android.content.Context
import android.graphics.Color
import android.widget.FrameLayout
import com.avstaim.darkside.cookies.ui.showToast
import com.avstaim.darkside.dsl.views.LayoutUi
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.backgroundColor
import com.avstaim.darkside.dsl.views.imageDrawable
import com.avstaim.darkside.dsl.views.imageView
import com.avstaim.darkside.dsl.views.layouts.frameLayout
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.textColor
import com.avstaim.darkside.dsl.views.textView
import com.avstaim.darkside.slab.BindableSlab

class DrawableSlab(private val context: Context) : BindableSlab<FrameLayout, DrawableUi, TestRecyclerItem.DrawableItem>() {

    override val ui = DrawableUi(context)

    override suspend fun performBind(data: TestRecyclerItem.DrawableItem) {
        ui.root.onClick {
            context.showToast("${data.d}")
        }

        ui.image.imageDrawable = data.d
    }
}

class DrawableUi(context: Context) : LayoutUi<FrameLayout>(context) {

    val image = imageView()

    override fun ViewBuilder.layout() =
        frameLayout {
            backgroundColor = Color.GRAY
            image {}
        }
}
