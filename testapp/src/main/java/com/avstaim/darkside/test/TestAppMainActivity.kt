package com.avstaim.darkside.test

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avstaim.darkside.cookies.intentFor
import com.avstaim.darkside.dsl.views.button
import com.avstaim.darkside.dsl.views.layouts.verticalLayout
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.setContentUi
import com.avstaim.darkside.dsl.views.ui
import com.avstaim.darkside.test.recycler.TestRecyclerActivity
import com.avstaim.darkside.test.slotui.SlotUiTestActivity

class TestAppMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentUi(activityUi)
    }


    @SuppressLint("SetTextI18n")
    private val activityUi = ui(this) {
        verticalLayout {

            button {
                text = "Test Recycler"
                onClick {
                    startActivityFor<TestRecyclerActivity>()
                }
            }

            button {
                text = "Slot Ui Test"
                onClick {
                    startActivityFor<SlotUiTestActivity>()
                }
            }
        }
    }

    private inline fun <reified A : Activity> startActivityFor() = startActivity(intentFor<A>())
}