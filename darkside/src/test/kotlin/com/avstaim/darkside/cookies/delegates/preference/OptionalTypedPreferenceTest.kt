package com.avstaim.darkside.cookies.delegates.preference

import android.content.Context
import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

@RunWith(RobolectricTestRunner::class)
class OptionalTypedPreferenceTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)

    private enum class MyEnum {
        VALUE1, VALUE2, VALUE3, VALUE4
    }

    private var underTest: MyEnum? by sharedPreferences.optionalPreference(
        default = null,
        name = "myTypedPref",
        writer = MyEnum::toString,
        reader = MyEnum::valueOf,
    )

    @Test
    fun `default by default`() {
        expectThat(underTest).isEqualTo(null)
    }

    @Test
    fun `written value persists`() {
        underTest = MyEnum.VALUE2
        expectThat(underTest).isEqualTo(MyEnum.VALUE2)
    }

    @Test
    fun `written is written to shared prefs`() {
        underTest = MyEnum.VALUE3
        expectThat(sharedPreferences.getString("myTypedPref", null)).isEqualTo("VALUE3")
    }

    @Test
    fun `reading from shared prefs`() {
        sharedPreferences.edit(commit = true) {
            putString("myTypedPref", "VALUE4")
        }
        expectThat(underTest).isEqualTo(MyEnum.VALUE4)
    }

    @Test
    fun `written value is overwritten by null`() {
        underTest = MyEnum.VALUE4
        underTest = null
        expectThat(underTest).isNull()
    }
}