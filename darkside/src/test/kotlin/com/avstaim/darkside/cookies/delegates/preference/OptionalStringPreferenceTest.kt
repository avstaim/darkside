package com.avstaim.darkside.cookies.delegates.preference

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@RunWith(RobolectricTestRunner::class)
class OptionalStringPreferenceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)

    private var underTest by sharedPreferences.optionalStringPreference(
        name = "myOptional",
    )
    
    @Test
    fun `null by default`() {
        expectThat(underTest).isNull()
    }

    @Test
    fun `written value persists`() {
        underTest = "newValue"
        expectThat(underTest).isEqualTo("newValue")
    }

    @Test
    fun `written value is overwritten by null`() {
        underTest = "newValue"
        underTest = null
        expectThat(underTest).isNull()
    }
}