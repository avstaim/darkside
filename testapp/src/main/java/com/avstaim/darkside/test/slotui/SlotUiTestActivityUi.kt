package com.avstaim.darkside.test.slotui

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.LinearLayout
import com.avstaim.darkside.cookies.dp
import com.avstaim.darkside.dsl.views.LayoutUi
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.button
import com.avstaim.darkside.dsl.views.include
import com.avstaim.darkside.dsl.views.layouts.horizontalLayout
import com.avstaim.darkside.dsl.views.layouts.simpleLayoutParams
import com.avstaim.darkside.dsl.views.layouts.verticalLayout
import com.avstaim.darkside.dsl.views.space
import com.avstaim.darkside.slab.SlotUi

@SuppressLint("SetTextI18n")
class SlotUiTestActivityUi(activity: Activity) : LayoutUi<LinearLayout>(activity) {

    val slotUi = SlotUi(activity)

    val button1 = button {
        text = "slab1"
    }
    val button2 = button {
        text = "slab2"
    }
    val button3 = button {
        text = "slab3"
    }
    val button4 = button {
        text = "slab4"
    }

    val buttonLayout1  = button {
        text = "layout1"
    }

    val buttonLayout2  = button {
        text = "layout2"
    }

    val buttonLayout3  = button {
        text = "layout3"
    }

    override fun ViewBuilder.layout() =
        verticalLayout {

            horizontalLayout {
                include(view = button1)
                include(view = button2)
                include(view = button3)
                include(view = button4)
            }

            space {
                layoutParams = simpleLayoutParams(0, dp(10))
            }

            horizontalLayout {
                include(view = buttonLayout1)
                include(view = buttonLayout2)
                include(view = buttonLayout3)
            }

            space {
                layoutParams = simpleLayoutParams(0, dp(10))
            }

            include(ui = slotUi)
        }
}