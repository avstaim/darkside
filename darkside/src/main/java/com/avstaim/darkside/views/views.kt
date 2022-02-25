// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.avstaim.darkside.views

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Space
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

inline fun ViewBuilder.textView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: TextView.() -> Unit = {}
): TextView = view(
    factory = ::AppCompatTextView,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.button(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: Button.() -> Unit = {}
): Button = view(
    factory = ::AppCompatButton,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.imageView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: ImageView.() -> Unit = {}
): ImageView = view(
    factory = ::AppCompatImageView,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.editText(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: EditText.() -> Unit = {}
): EditText = view(
    factory = ::AppCompatEditText,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.spinner(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: Spinner.() -> Unit = {}
): Spinner = view(
    factory = ::Spinner,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.imageButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: ImageButton.() -> Unit = {}
): ImageButton = view(
    factory = ::AppCompatImageButton,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.checkBox(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: CheckBox.() -> Unit = {}
): CheckBox = view(
    factory = ::AppCompatCheckBox,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.radioButton(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: RadioButton.() -> Unit = {}
): RadioButton = view(
    factory = ::AppCompatRadioButton,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.radioGroup(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: RadioGroup.() -> Unit = {}
): RadioGroup = view(
    factory = ::RadioGroup,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.checkedTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: CheckedTextView.() -> Unit = {}
): CheckedTextView = view(
    factory = ::CheckedTextView,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.autoCompleteTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: AutoCompleteTextView.() -> Unit = {}
): AutoCompleteTextView = view(
    factory = ::AutoCompleteTextView,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.multiAutoCompleteTextView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: MultiAutoCompleteTextView.() -> Unit = {}
): MultiAutoCompleteTextView = view(
    factory = ::MultiAutoCompleteTextView,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.ratingBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: RatingBar.() -> Unit = {}
): RatingBar = view(
    factory = ::AppCompatRatingBar,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.seekBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: SeekBar.() -> Unit = {}
): SeekBar = view(
    factory = ::AppCompatSeekBar,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.progressBar(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: ProgressBar.() -> Unit = {}
): ProgressBar = view(
    factory = ::ProgressBar,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.space(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: Space.() -> Unit = {}
): Space = view(
    factory = ::Space,
    id = id,
    styleRes = theme,
    initView = init
)

inline fun ViewBuilder.recyclerView(
    @IdRes id: Int = View.NO_ID,
    @StyleRes theme: Int = NO_THEME,
    init: RecyclerView.() -> Unit = {}
): RecyclerView = view(
    factory = ::RecyclerView,
    id = id,
    styleRes = theme,
    initView = init
)
