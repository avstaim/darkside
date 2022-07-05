package com.avstaim.darkside.dsl.alert

import android.content.Context
import android.content.DialogInterface

fun Context.selector(
    title: CharSequence? = null,
    items: List<CharSequence>,
    onClick: (DialogInterface, Int) -> Unit
) {
    with(AlertBuilder(ctx = this, style = -1)) {
        if (title != null) {
            this.title = title
        }
        items(items, onClick)
        show()
    }
}
