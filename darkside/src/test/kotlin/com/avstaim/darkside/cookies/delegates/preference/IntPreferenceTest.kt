package com.avstaim.darkside.cookies.delegates.preference

import android.content.Context
import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@RunWith(RobolectricTestRunner::class)
class IntPreferenceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)

    private var underTest by sharedPreferences.intPreference(
        default = -1,
        name = "intPref",
    )

    @Test
    fun `default by default`() {
        expectThat(underTest).isEqualTo(-1)
    }

    @Test
    fun `written value persists`() {
        underTest = 666
        expectThat(underTest).isEqualTo(666)
    }

    @Test
    fun `written is written to shared prefs`() {
        underTest = 42
        expectThat(sharedPreferences.getInt("intPref", -2)).isEqualTo(42)
    }

    @Test
    fun `reading from shared prefs`() {
        sharedPreferences.edit(commit = true) {
            putInt("intPref", 13)
        }
        expectThat(underTest).isEqualTo(13)
    }
}
