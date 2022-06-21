@file:Suppress("unused")

package com.avstaim.darkside.dsl.menu

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import com.avstaim.darkside.cookies.noGetter

inline fun Menu.item(
    @IdRes id: Int = Menu.NONE,
    order: Int = Menu.NONE,
    @StringRes titleRes: Int,
    init: MenuItem.() -> Unit = {}
) = add(Menu.NONE, id, order, titleRes).also(init)

inline fun Menu.item(
    @IdRes id: Int = Menu.NONE,
    order: Int = Menu.NONE,
    title: String? = null,
    init: MenuItem.() -> Unit = {}
) = add(Menu.NONE, id, order, title).also(init)

inline var MenuItem.titleRes: Int
    get() = noGetter()
    set(@StringRes value) {
        setTitle(value)
    }

inline var MenuItem.iconRes: Int
    get() = noGetter()
    set(@DrawableRes value) {
        setIcon(value)
    }

inline var MenuItem.showAsAction: MenuShowAsAction
    get() = noGetter()
    set(value) = setShowAsAction(value.value)

enum class MenuShowAsAction(val value: Int) {
    NEVER(MenuItem.SHOW_AS_ACTION_NEVER),
    IF_ROOM(MenuItem.SHOW_AS_ACTION_IF_ROOM),
    ALWAYS(MenuItem.SHOW_AS_ACTION_ALWAYS),
    WITH_TEXT(MenuItem.SHOW_AS_ACTION_WITH_TEXT),
    COLLAPSE_ACTION_VIEW(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW),
}

inline fun MenuItem.onMenuItemClick(crossinline value: () -> Unit) {
    setOnMenuItemClickListener {
        value()
        return@setOnMenuItemClickListener true
    }
}

inline fun Toolbar.menu(init: Menu.() -> Unit): Menu = menu.apply(init)

inline fun popupMenu(anchor: View, init: Menu.() -> Unit): PopupMenu =
    PopupMenu(anchor.context, anchor).also { popupMenu ->
        init(popupMenu.menu)
    }

inline fun showPopupMenu(anchor: View, init: Menu.() -> Unit) =
    PopupMenu(anchor.context, anchor).also { popupMenu ->
        init(popupMenu.menu)
    }.show()
