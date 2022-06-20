package com.avstaim.darkside.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.avstaim.darkside.cookies.intentFor
import com.avstaim.darkside.test.recycler.TestRecyclerActivity

class TestAppMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(intentFor<TestRecyclerActivity>())
    }
}