@file:Suppress("unused")

package com.avstaim.darkside.dsl.preferences

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceGroup
import com.avstaim.darkside.R
import com.avstaim.darkside.cookies.dp
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.dsl.alert.alert
import com.avstaim.darkside.dsl.views.editText
import com.avstaim.darkside.dsl.views.layouts.linearLayout
import com.avstaim.darkside.dsl.views.onClick
import com.avstaim.darkside.dsl.views.padding
import com.avstaim.darkside.dsl.views.textView

/**
 * Dsl for [EditTextPreference].
 */

fun PreferenceGroup.stringPreference(
    dependencyKey: String? = null,
    init: EditTextPreference.() -> Unit
): EditTextPreference =
    textPreferenceInternal(R.layout.preference_dialog_text_string, init, dependencyKey)

fun PreferenceGroup.multiLinePreference(
    dependencyKey: String? = null,
    init: EditTextPreference.() -> Unit
): EditTextPreference =
    textPreferenceInternal(R.layout.preference_dialog_text_multi_lines, init, dependencyKey)

fun PreferenceGroup.numberPreference(
    dependencyKey: String? = null,
    init: EditTextPreference.() -> Unit
): EditTextPreference =
    textPreferenceInternal(R.layout.preference_dialog_text_number, init, dependencyKey)

fun PreferenceGroup.urlPreference(
    dependencyKey: String? = null,
    init: TextWithSuggestsPreference.() -> Unit
): TextWithSuggestsPreference {
    dependencyKey?.let { dependency = it }
    return TextWithSuggestsPreference(context).apply {
        init()
        addPreference(this)
    }
}

@Suppress("NestedLambdaShadowedImplicitParameter")
class TextWithSuggestsPreference(context: Context) : Preference(context) {
    private lateinit var edit: EditText
    var suggests = emptyArray<String>()

    var setting: Setting<String?>
        get() = noGetter()
        set(value) {
            val initialValue = value.getter()
            summary = initialValue
            onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
                val stringValue = newValue.toString()
                value.setter(stringValue)
                summary = stringValue
                true
            }
        }

    init {
        onClick {
            context.alert {
                title = this@TextWithSuggestsPreference.title ?: "title"
                customView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        padding = dp(8)
                        edit = editText {
                            setText(summary)
                        }
                        for (suggest in suggests) {
                            textView {
                                text = suggest
                                setupSuggestView()
                            }
                        }
                    }
                }
                positiveButton("Ok") { callChangeListener(edit.text.trim()) }
                negativeButton("Cancel") {}
            }.show()
        }
    }

    private fun TextView.setupSuggestView() {
        onClick {
            edit.setText(text)
        }
        padding = dp(1)
    }
}

private fun PreferenceGroup.textPreferenceInternal(
    res: Int,
    init: EditTextPreference.() -> Unit,
    dependencyKey: String?
) =
    with(EditTextPreference(context)) {
        dialogLayoutResource = res

        init()

        if (dialogTitle?.isEmpty() != false) {
            dialogTitle = title
        }

        addPreference(this)
        dependencyKey?.let { dependency = it }
        this
    }

var EditTextPreference.setting: Setting<String?>
    get() = noGetter()
    set(value) {
        val initialValue = value.getter()
        isPersistent = false
        summary = initialValue
        text = initialValue
        onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
            val stringValue = newValue as String
            value.setter(stringValue)
            summary = stringValue
            true
        }
    }

var EditTextPreference.intSetting: Setting<Int>
    get() = noGetter()
    set(value) {
        val initialValue = value.getter().toString()
        isPersistent = false
        summary = initialValue
        text = initialValue
        onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
            val stringValue = newValue as String
            value.setter(stringValue.toInt())
            summary = stringValue
            true
        }
    }

var EditTextPreference.longSetting: Setting<Long>
    get() = noGetter()
    set(value) {
        val initialValue = value.getter().toString()
        isPersistent = false
        summary = initialValue
        text = initialValue
        onPreferenceChangeListener = OnPreferenceChangeListener { _, newValue ->
            val stringValue = newValue as String
            value.setter(stringValue.toLong())
            summary = stringValue
            true
        }
    }
