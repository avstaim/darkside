@file:Suppress("unused")

package com.avstaim.darkside.cookies

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle

inline fun <reified T: Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
    startActivity(createIntent<T>(params.asBundle()))

inline fun <reified T: Activity> Activity.startActivityForResult(
    requestCode: Int,
    vararg params: Pair<String, Any?>,
) = startActivityForResult(createIntent<T>(params.asBundle()), requestCode)

inline fun <reified T: Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
    createIntent<T>(params.asBundle())

inline fun <reified T: Any> Context.intentForWithExtras(vararg extras: Bundle): Intent =
    createIntent<T>(bundleOf { extras.forEach { putAll(it) } })

inline fun <reified T: Service> Context.startService(vararg params: Pair<String, Any?>) =
    startService(createIntent<T>(params.asBundle()))

inline fun <reified T: Activity> Context.startActivity(replaceExtras: Bundle) =
    startActivity(intentFor<T>().apply { replaceExtras(replaceExtras) })

fun Intent.putBundleExtras(vararg bundles: Bundle): Intent = apply { bundles.forEach(::putExtras) }

inline fun <reified T> Context.createIntent(extras: Bundle): Intent =
    createIntent(T::class.java, extras)

fun <T> Context.createIntent(clazz: Class<out T>, extras: Bundle): Intent =
    Intent(this, clazz).apply {
        replaceExtras(extras)
    }
