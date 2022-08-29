package com.avstaim.darkside.test.slotui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.avstaim.darkside.cookies.dp
import com.avstaim.darkside.dsl.views.drawableResource
import com.avstaim.darkside.dsl.views.imageView
import com.avstaim.darkside.dsl.views.layouts.frameLayout
import com.avstaim.darkside.dsl.views.layouts.horizontalLayout
import com.avstaim.darkside.dsl.views.layouts.linearLayout
import com.avstaim.darkside.dsl.views.layouts.linearLayoutParams
import com.avstaim.darkside.dsl.views.layouts.simpleLayoutParams
import com.avstaim.darkside.dsl.views.matchParent
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.progressBar
import com.avstaim.darkside.dsl.views.setContentUi
import com.avstaim.darkside.dsl.views.textView
import com.avstaim.darkside.dsl.views.ui
import com.avstaim.darkside.dsl.views.wrapContent
import com.avstaim.darkside.service.KLog
import com.avstaim.darkside.slab.Slab
import com.avstaim.darkside.slab.asSlab

@SuppressLint("SetTextI18n")
class SlotUiTestActivity : AppCompatActivity() {

    private val activityUi by lazy { SlotUiTestActivityUi(activity = this) }

    private val slab1: Slab<out View> =
        ui(this) {
            textView {
                text = "Slab1"
            }
        }.asSlab()

    private val slab2: Slab<out View> =
        ui(this) {
            horizontalLayout {
                textView {
                    text = "Slab 2 element 1"
                }
                textView {
                    text = "Slab 2 element 2"
                }
                textView {
                    text = "Slab 2 element 3"
                }
                textView {
                    text = "Slab 2 element 4"
                }
            }
        }.asSlab()

    private val slab3: Slab<out View> =
        ui(this) {
            frameLayout {
                imageView {
                    drawableResource = android.R.drawable.btn_star
                }
            }
        }.asSlab()

    private val slab4: Slab<out View> =
        ui(this) {
            progressBar {
                isIndeterminate = true
            }
        }.asSlab()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentUi(activityUi)

        KLog.isEnabled = true
        activityUi.init()
    }

    private fun SlotUiTestActivityUi.init() {
        mapOf(
            button1 to slab1,
            button2 to slab2,
            button3 to slab3,
            button4 to slab4,
        ).forEach { (button, slab) ->
            button.onClick {
                slotUi.slot.insert(slab)
            }
        }

        mapOf<Button, View.() -> ViewGroup.LayoutParams>(
            buttonLayout1 to {
                simpleLayoutParams {
                    width = dp(50)
                    height = dp(50)
                }
            },
            buttonLayout2 to {
                simpleLayoutParams {
                    width = matchParent
                    height = wrapContent
                }
            },
            buttonLayout3 to {
                linearLayoutParams {
                    width = matchParent
                    height = 0
                    weight = 1f
                }
            },
        ).forEach { (button, initLp) ->
            button.onClick {
                slotUi.layoutParams(initLp)
            }
        }
    }
}