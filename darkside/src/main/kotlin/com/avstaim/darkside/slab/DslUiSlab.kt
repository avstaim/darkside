package com.avstaim.darkside.slab

import android.view.View
import com.avstaim.darkside.dsl.views.Ui

open class DslUiSlab <V : View, U : Ui<V>>(override val ui: U) : UiSlab<V, U>()
