// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.yandex.darkside.cookies

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.View
import com.yandex.darkside.views.SimplifiedViewBuilder
import com.yandex.darkside.views.ViewBuilder

/**
 * Simple DSL for alert popup creation.
 */

inline fun Context.alert(style: Int = -1, init: AlertBuilder.() -> Unit): AlertBuilder = AlertBuilder(
    ctx = this,
    style,
).apply { init() }

class AlertBuilder(val ctx: Context, style: Int) {
    private val builder = if (style == -1) AlertDialog.Builder(ctx) else AlertDialog.Builder(ctx, style)

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


    fun customView(init: ViewBuilder.() -> Unit) {
        AlertViewBuilder(ctx, this).init()
    }

    fun onCancelled(handler: (DialogInterface) -> Unit) {
        builder.setOnCancelListener(handler)
    }

    fun onKeyPressed(handler: (dialog: DialogInterface, keyCode: Int, e: KeyEvent) -> Boolean) {
        builder.setOnKeyListener(handler)
    }
    fun positiveButton(buttonText: String, onClicked: () -> Unit) {
        builder.setPositiveButton(buttonText) { _, _ -> onClicked() }
    }

    fun positiveButton(buttonTextResource: Int, onClicked: () -> Unit) {
        builder.setPositiveButton(buttonTextResource) { _, _ -> onClicked() }
    }

    fun negativeButton(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    fun negativeButton(buttonTextResource: Int, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNegativeButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    fun neutralPressed(buttonText: String, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNeutralButton(buttonText) { dialog, _ -> onClicked(dialog) }
    }

    fun neutralPressed(buttonTextResource: Int, onClicked: (dialog: DialogInterface) -> Unit) {
        builder.setNeutralButton(buttonTextResource) { dialog, _ -> onClicked(dialog) }
    }

    fun items(items: List<CharSequence>, onItemSelected: (dialog: DialogInterface, index: Int) -> Unit) {
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
            onItemSelected(dialog, which)
        }
    }

    fun <T> items(items: List<T>, onItemSelected: (dialog: DialogInterface, item: T, index: Int) -> Unit) {
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialog, which ->
            onItemSelected(dialog, items[which], which)
        }
    }

    fun build(): AlertDialog = builder.create()

    fun show(): AlertDialog = builder.show()

    private class AlertViewBuilder(
        override val ctx: Context,
        val alertBuilder: AlertBuilder,
    ) : SimplifiedViewBuilder {

        override fun View.addToParent() {
            alertBuilder.customView = this
        }
    }
}
