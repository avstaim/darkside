@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.FontRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.core.content.res.ResourcesCompat
import com.avstaim.darkside.cookies.activity
import com.avstaim.darkside.cookies.coroutineScope
import com.avstaim.darkside.cookies.noGetter
import kotlinx.coroutines.launch

inline fun ViewBuilder.editText(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: EditText.() -> Unit = {}
): EditText = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.textView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: TextView.() -> Unit = {}
): TextView = view(id, themeRes, styleAttr, styleRes, init)

inline fun TextView.onTextChange(useCoroutine: Boolean = true, crossinline textListener: (CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (useCoroutine) {
                activity.coroutineScope.launch {
                    textListener(s)
                }
            } else {
                textListener(s)
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
        override fun afterTextChanged(s: Editable) = Unit
    })
}

inline fun TextView.beforeTextChange(crossinline textListener: (CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)  {
            activity.coroutineScope.launch {
                textListener(s)
            }
        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable) = Unit
    })
}

inline fun TextView.afterTextChange(crossinline editableListener: (Editable) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            activity.coroutineScope.launch {
                editableListener(s)
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
    })
}

inline fun TextView.onEditorAction(crossinline actionListener: (Int, KeyEvent?) -> Boolean) =
    setOnEditorActionListener  { _, actionId, event -> actionListener(actionId, event) }

inline fun TextView.onFocusChange(crossinline focusListener: (Boolean) -> Unit) =
    setOnFocusChangeListener { _, hasFocus -> focusListener(hasFocus) }


inline var TextView.typefaceResource: Int
    get() = noGetter()
    set(@FontRes value) {
        typeface = ResourcesCompat.getFont(context, value)
    }
