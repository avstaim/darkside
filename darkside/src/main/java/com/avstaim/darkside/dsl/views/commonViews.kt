@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.ImageButton
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Space
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.SwitchCompat

inline fun ViewBuilder.button(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: Button.() -> Unit = {}
): Button = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.imageButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ImageButton.() -> Unit = {}
): ImageButton = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.checkBox(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: CheckBox.() -> Unit = {}
): CheckBox = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.radioButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: RadioButton.() -> Unit = {}
): RadioButton = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.checkedTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: CheckedTextView.() -> Unit = {}
): CheckedTextView = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.autoCompleteTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: AutoCompleteTextView.() -> Unit = {}
): AutoCompleteTextView = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.multiAutoCompleteTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: MultiAutoCompleteTextView.() -> Unit = {}
): MultiAutoCompleteTextView = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.ratingBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: RatingBar.() -> Unit = {}
): RatingBar = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.seekBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: SeekBar.() -> Unit = {}
): SeekBar = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.progressBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: ProgressBar.() -> Unit = {}
): ProgressBar = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.space(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: Space.() -> Unit = {}
): Space = view(id, themeRes, styleAttr, styleRes, init)

inline fun ViewBuilder.switch(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: SwitchCompat.() -> Unit = {}
): SwitchCompat = view(id, themeRes, styleAttr, styleRes, init)
