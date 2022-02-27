// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.avstaim.darkside.dsl.views

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Spinner
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import com.avstaim.darkside.cookies.activity
import com.avstaim.darkside.cookies.coroutineScope
import kotlinx.coroutines.launch

inline fun ViewBuilder.spinner(
    @IdRes id: Int = View.NO_ID,
    @StyleRes themeRes: Int = NO_THEME,
    @AttrRes styleAttr: Int = NO_STYLE_ATTR,
    @StyleRes styleRes: Int = NO_STYLE_RES,
    init: Spinner.() -> Unit = {}
): Spinner  = view(id, themeRes, styleAttr, styleRes, init)

fun <T : Adapter> AdapterView<T>.onItemSelect(itemListener: (AdapterItem?) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            activity.coroutineScope.launch {
                itemListener(
                    view?.let { AdapterItem(it, position, id) }
                )
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            activity.coroutineScope.launch {
                itemListener(null)
            }
        }
    }
}

data class AdapterItem(
    val view: View,
    val position: Int,
    val id: Long,
)