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
class StringPreferenceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)

    private var underTest by sharedPreferences.stringPreference(
        default = "default",
        name = "name",
    )

    @Test
    fun `default by default`() {
        expectThat(underTest).isEqualTo("default")
    }

    @Test
    fun `written value persists`() {
        underTest = "newValue"
        expectThat(underTest).isEqualTo("newValue")
    }

    @Test
    fun `written is written to shared prefs`() {
        underTest = "newValue"
        expectThat(sharedPreferences.getString("name", null)).isEqualTo("newValue")
    }

    @Test
    fun `reading from shared prefs`() {
        sharedPreferences.edit(commit = true) {
            putString("name", "previousValue")
        }
        expectThat(underTest).isEqualTo("previousValue")
    }
}
