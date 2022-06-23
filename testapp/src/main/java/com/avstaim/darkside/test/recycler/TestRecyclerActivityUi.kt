package com.avstaim.darkside.test.recycler

import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.avstaim.darkside.cookies.recycler.adapterChunk
import com.avstaim.darkside.cookies.recycler.chunkedAdapter
import com.avstaim.darkside.dsl.views.LayoutUi
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.layouts.frameLayout
import com.avstaim.darkside.dsl.views.layouts.layoutParams
import com.avstaim.darkside.dsl.views.matchParent
import com.avstaim.darkside.dsl.views.recyclerView
import com.avstaim.darkside.test.R

class TestRecyclerActivityUi(activity: TestRecyclerActivity) : LayoutUi<FrameLayout>(activity) {

    private val recycler = recyclerView {
        layoutManager = LinearLayoutManager(ctx)
        adapter = chunkedAdapter(
            initial = listOf(
                TestRecyclerItem.ConstItem,
                TestRecyclerItem.IntItem(0),
                TestRecyclerItem.IntItem(1),
                TestRecyclerItem.StringItem("AAA"),
                TestRecyclerItem.StringItem("BBB"),
                TestRecyclerItem.DrawableItem(ctx.getDrawable(R.drawable.ic_launcher_foreground)!!),
                TestRecyclerItem.StringItem("kfakjfdnkjsfngskjfgn"),
                TestRecyclerItem.IntItem(666),
                TestRecyclerItem.ConstItem,
                TestRecyclerItem.DrawableItem(ctx.getDrawable(android.R.drawable.ic_menu_slideshow)!!),

                TestRecyclerItem.IntItem(71),
                TestRecyclerItem.IntItem(72),
                TestRecyclerItem.IntItem(73),
                TestRecyclerItem.IntItem(74),
                TestRecyclerItem.IntItem(75),
                TestRecyclerItem.IntItem(76),
                TestRecyclerItem.IntItem(77),
                TestRecyclerItem.IntItem(78),
                TestRecyclerItem.IntItem(79),
                TestRecyclerItem.IntItem(80),
                TestRecyclerItem.IntItem(81),
            ),
            adapterChunk { ConstSlab(ctx) },
            adapterChunk { IntSlab(ctx) },
            adapterChunk { StringSlab(ctx) },
            adapterChunk { DrawableSlab(ctx) },
        )
    }

    override fun ViewBuilder.layout() = frameLayout {
        recycler {
            layoutParams = layoutParams(matchParent, matchParent)
        }
    }
}