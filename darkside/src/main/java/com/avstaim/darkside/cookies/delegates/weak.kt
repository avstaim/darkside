package com.avstaim.darkside.cookies.delegates

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Usage:
 *
 * val/var myWeakRef by weak(myStrongRef)
 */

fun <T> weak(obj: T? = null): ReadWriteProperty<Any?, T?> = WeakRef(obj)

private class WeakRef<T>(obj: T? = null): ReadWriteProperty<Any?, T?> {

    private var weakReference : WeakReference<T>?

    init {
        this.weakReference = obj?.let { WeakReference(it) }
    }

    override fun getValue(thisRef:Any? , property: KProperty<*>): T? {
        return weakReference?.get()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = value?.let { WeakReference(it) }
    }
}
