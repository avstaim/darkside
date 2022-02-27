// Copyright (c) 2021 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

package com.avstaim.darkside.dsl.views.layouts.constraint

import android.view.View
import androidx.annotation.IdRes

open class ViewConsumer {

    val viewIds: IntArray get() = consumed.toIntArray()

    protected val consumed = mutableListOf<Int>()

    operator fun View.invoke() = consumed.add(this.id)

    operator fun @receiver:IdRes Int.invoke() = consumed.add(this)
}
