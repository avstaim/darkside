@file:Suppress("unused")

package com.avstaim.darkside.slab

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.avstaim.darkside.cookies.ui.views.SimpleViewStub
import com.avstaim.darkside.dsl.views.Ui
import com.avstaim.darkside.dsl.views.ViewBuilder
import com.avstaim.darkside.dsl.views.ui
import com.avstaim.darkside.service.KAssert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch

/**
 * Insert slab just in given scope. After the scope is completed/cancelled slab is removed from slot.
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun SlabSlot.insertOnScope(slab: Slab<*>, scope: CoroutineScope) {
    val job = scope.coroutineContext[Job]
    if (job == null) {
        KAssert.fail { "Can't attach to context without a job" }
        return
    }
    if (!job.isActive) {
        return
    }

    insert(slab)

    GlobalScope.launch(NonCancellable + Dispatchers.Main) {
        try {
            job.join()
        } finally {
            clear()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun SlabSlot.clear() = insert(currentView.context.emptySlab())

inline fun <reified V : View> Context.slab(crossinline init: ViewBuilder.() -> V): Slab<V> {
    return DslUiSlab(ui(ctx = this, init))
}

fun Context.emptySlab(): Slab<SimpleViewStub> = slab { SimpleViewStub(ctx).apply { visibility = View.GONE } }

fun <V : View> V.asSlab(): Slab<V> = ViewSlab(view = this)

private class ViewSlab<V : View>(override val view: V) : Slab<V>()

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun <V : View> Ui<V>.asSlab(): UiSlab<V, Ui<V>> = DslUiSlab(ui = this)

