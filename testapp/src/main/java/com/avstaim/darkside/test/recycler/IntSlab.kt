package com.avstaim.darkside.test.recycler

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import com.avstaim.darkside.cookies.dp
import com.avstaim.darkside.cookies.ui.showToast
import com.avstaim.darkside.dsl.views.LayoutUi
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.backgroundColor
import com.avstaim.darkside.dsl.views.layouts.frameLayout
import com.avstaim.darkside.dsl.views.layouts.simpleLayoutParams
import com.avstaim.darkside.dsl.views.matchParent
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.textColor
import com.avstaim.darkside.dsl.views.textView
import com.avstaim.darkside.dsl.views.wrapContent
import com.avstaim.darkside.service.KLog
import com.avstaim.darkside.slab.BindableSlab

class IntSlab(private val context: Context) : BindableSlab<FrameLayout, IntUi, TestRecyclerItem.IntItem>() {

    override val ui = IntUi(context)

    override fun FrameLayout.overrideLayoutParams() = simpleLayoutParams(wrapContent, dp(120))

    override suspend fun performBind(data: TestRecyclerItem.IntItem) {
        ui.root.onClick {
            context.showToast(data.i.toString())
        }

        ui.intText.text = data.i.toString()
    }

    override fun onCreate() {
        super.onCreate()
        KLog.d { "onCreate $this" }
    }

    override fun onAttach() {
        super.onAttach()
        KLog.d { "onAttach $this" }
    }

    override fun onStart() {
        super.onStart()
        KLog.d { "onStart $this" }
    }

    override fun onResume() {
        super.onResume()
        KLog.d { "onResume $this" }
    }

    override fun onPause() {
        super.onPause()
        KLog.d { "onPause $this" }
    }

    override fun onStop() {
        super.onStop()
        KLog.d { "onStop $this" }
    }

    override fun onDetach() {
        super.onDetach()
        KLog.d { "onDetach $this" }
    }

    override fun onDestroy() {
        super.onDestroy()
        KLog.d { "onDestroy $this" }
    }
}

class IntUi(context: Context) : LayoutUi<FrameLayout>(context) {

    val intText = textView {
        textColor = Color.BLUE
    }

    override fun ViewBuilder.layout() =
        frameLayout {
            backgroundColor = Color.parseColor("#AAAAAA")
            intText {}
        }
}
