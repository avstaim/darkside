package com.avstaim.darkside.dsl.views.layouts.constraint

import android.os.Build
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import com.avstaim.darkside.slab.SlabSlot

data class ViewIdPair(@IdRes val first: Int, @IdRes val second: Int)

infix fun @receiver:IdRes Int.to(@IdRes other: Int) = ViewIdPair(this, other)
infix fun @receiver:IdRes Int.to(other: View) = ViewIdPair(this, other.id)
@RequiresApi(Build.VERSION_CODES.KITKAT)
infix fun @receiver:IdRes Int.to(other: SlabSlot) = ViewIdPair(this, other.currentView.id)

infix fun View.to(@IdRes other: Int) = ViewIdPair(this.id, other)
infix fun View.to(other: View) = ViewIdPair(this.id, other.id)
@RequiresApi(Build.VERSION_CODES.KITKAT)
infix fun View.to(other: SlabSlot) = ViewIdPair(this.id, other.currentView.id)

@RequiresApi(Build.VERSION_CODES.KITKAT)
infix fun SlabSlot.to(@IdRes other: Int) = ViewIdPair(this.currentView.id, other)
@RequiresApi(Build.VERSION_CODES.KITKAT)
infix fun SlabSlot.to(other: View) = ViewIdPair(this.currentView.id, other.id)
@RequiresApi(Build.VERSION_CODES.KITKAT)
infix fun SlabSlot.to(other: SlabSlot) = ViewIdPair(this.currentView.id, other.currentView.id)
