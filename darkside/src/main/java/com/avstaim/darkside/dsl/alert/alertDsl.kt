@file:Suppress("unused")

package com.avstaim.darkside.dsl.alert

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.avstaim.darkside.cookies.ignoreReturnValue
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

    var customViewResource: Int
        get() = noGetter()
        set(value) { builder.setView(value) }

    var isCancelable: Boolean
        get() = noGetter()
        set(value) { builder.setCancelable(value) }

    private var onShowListener: ((dialog: AlertDialog) -> Unit)? = null

    inline fun customView(init: ViewBuilder.() -> Unit) {
        AlertViewBuilder(ctx, this).init()
    }

    inline fun onCancelled(crossinline handler: (AlertDialog) -> Unit) =
        builder.setOnCancelListener { dialogInterface ->
            handler(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    inline fun onKeyPressed(crossinline handler: (dialog: AlertDialog, keyCode: Int, event: KeyEvent) -> Boolean) =
        builder.setOnKeyListener { dialogInterface, keyCode, event ->
            handler(dialogInterface as AlertDialog, keyCode, event)
        }.ignoreReturnValue()

    inline fun positiveButton(buttonText: String, crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setPositiveButton(buttonText) { dialogInterface, _ ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    inline fun positiveButton(buttonTextResource: Int, crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setPositiveButton(buttonTextResource) { dialogInterface, _ ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    inline fun negativeButton(buttonText: String, crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setNegativeButton(buttonText) { dialogInterface, _ ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    inline fun negativeButton(buttonTextResource: Int, crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setNegativeButton(buttonTextResource) { dialogInterface, _ ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    inline fun neutralButton(buttonText: String, crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setNeutralButton(buttonText) { dialogInterface, _ ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    inline fun neutralButton(buttonTextResource: Int, crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setNeutralButton(buttonTextResource) { dialogInterface, _ ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    inline fun onDismissed(crossinline onClicked: (dialog: AlertDialog) -> Unit) =
        builder.setOnDismissListener { dialogInterface ->
            onClicked(dialogInterface as AlertDialog)
        }.ignoreReturnValue()

    fun onShow(onShow: (dialog: AlertDialog) -> Unit) {
        onShowListener = onShow
    }

    inline fun items(items: List<CharSequence>, crossinline onItemSelected: (dialog: AlertDialog, index: Int) -> Unit) =
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialogInterface, which ->
            onItemSelected(dialogInterface as AlertDialog, which)
        }.ignoreReturnValue()

    inline fun <T> items(items: List<T>, crossinline onItemSelected: (dialog: AlertDialog, item: T, index: Int) -> Unit) =
        builder.setItems(Array(items.size) { i -> items[i].toString() }) { dialogInterface, which ->
            onItemSelected(dialogInterface as AlertDialog, items[which], which)
        }.ignoreReturnValue()

    fun build(): AlertDialog = builder.create().also { dialog ->
        onShowListener?.invoke(dialog)
    }

    fun show(): AlertDialog = build().also(AlertDialog::show)

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
