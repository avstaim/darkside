package com.avstaim.darkside.dsl.views.layouts.constraint

import android.view.View
import androidx.annotation.IdRes

data class ViewIdPair(@IdRes val first: Int, @IdRes val second: Int)

infix fun @receiver:IdRes Int.to(@IdRes other: Int) = ViewIdPair(this, other)
infix fun @receiver:IdRes Int.to(other: View) = ViewIdPair(this, other.id)

infix fun View.to(@IdRes other: Int) = ViewIdPair(this.id, other)
infix fun View.to(other: View) = ViewIdPair(this.id, other.id)
