// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.avstaim.darkside.dsl.alert

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import com.avstaim.darkside.cookies.noGetter
import com.avstaim.darkside.dsl.views.SimplifiedAddingViewBuilder
import com.avstaim.darkside.dsl.views.ViewBuilder

/**
 * Simple DSL for alert popup creation.
 */

inline fun Context.alert(style: Int = -1, init: AlertBuilder.() -> Unit): AlertBuilder = AlertBuilder(
    ctx = this,
    style,
).apply { init() }

inline fun Context.showAlert(style: Int = -1, init: AlertBuilder.() -> Unit) =
    alert(style, init).show()

class AlertBuilder(val ctx: Context, style: Int) {

    @PublishedApi
    internal val builder = if (style == -1) {
        AlertDialog.Builder(ctx)
    } else {
        AlertDialog.Builder(ctx, style)
    }

    var title: CharSequence
        get() = noGetter()
        set(value) { builder.setTitle(value) }

    var titleResource: Int
        get() = noGetter()
        set(value) { builder.setTitle(value) }

    var message: CharSequence
        get() = noGetter()
        set(value) { builder.setMessage(value) }

    var messageResource: Int
        get() = noGetter()
        set(value) { builder.setMessage(value) }

    var icon: Drawable
        get() = noGetter()
        set(value) { builder.setIcon(value) }

    var iconResource: Int
        get() = noGetter()
        set(value) { builder.setIcon(value) }

    var customTitle: View
        get() = noGetter()
        set(value) { builder.setCustomTitle(value) }

    var customView: View
        get() = noGetter()
        set(value) { builder.setView(value) }

    var isCancelable: Boolean
        get() = noGetter()
        set(value) { builder.setCancelable(value) }


    inline fun customView(init: ViewBuilder.() -> Unit) {
        AlertViewBuilder(ctx, this).init()
    }

    fun onCancelled(handler: (DialogInterface) -> Unit) {
        builder.setOnCancelListener(handler)
    }

    fun onKeyPressed(handler: (dialog: DialogInterface, keyCode: Int, e: KeyEvent) -> Boolean) {
        builder.setOnKeyListener(handler)
    }

    inline fun positiveButton(buttonText: String, crossinline onClicked: () -> Unit) {
        builder.setPositiveButton(buttonText) { _, _ -> onClicked() }
    }

    inline fun positiveButton(buttonTextResource: Int, crossinline onClicked: () -> Unit) {
        builder.setPositiveButton(buttonTextResource) { _, _ -> onClicked() }
    }

    inline fun negativeButton(buttonText: String, crossinline onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    inline fun negativeButton(buttonTextResource: Int, crossinline onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    inline fun neutralButton(buttonText: String, crossinline onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNeutralButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    inline fun neutralButton(buttonTextResource: Int, crossinline onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNeutralButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun onDismissed(onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setOnDismissListener(onClicked)
    }

    inline fun items(items: List<CharSequence>, crossinline onItemSelected: (dialog: DialogInterface, index: Int) -> Unit) {
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
            onItemSelected(dialog, which)
        }
    }

    inline fun <T> items(items: List<T>, crossinline onItemSelected: (dialog: DialogInterface, item: T, index: Int) -> Unit) {
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
            onItemSelected(dialog, items[which], which)
        }
    }

    fun build(): AlertDialog = builder.create()

    fun show(): AlertDialog = builder.show()

    @PublishedApi
    internal class AlertViewBuilder(
        override val ctx: Context,
        private val alertBuilder: AlertBuilder,
    ) : SimplifiedAddingViewBuilder {

        override fun View.addToParent() {
            alertBuilder.customView = this
        }
    }
}
