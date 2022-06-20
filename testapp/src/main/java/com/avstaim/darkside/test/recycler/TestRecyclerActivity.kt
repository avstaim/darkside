package com.avstaim.darkside.test.recycler

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.avstaim.darkside.dsl.views.setContentUi

class TestRecyclerActivity : AppCompatActivity() {

    private val ui by lazy { TestRecyclerActivityUi(activity = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentUi(ui)
    }
}