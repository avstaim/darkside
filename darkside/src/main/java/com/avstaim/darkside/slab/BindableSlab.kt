@file:Suppress("unused")

package com.avstaim.darkside.slab

import android.view.View
import com.avstaim.darkside.cookies.interfaces.Bindable
import com.avstaim.darkside.dsl.views.Ui
import kotlinx.coroutines.launch

abstract class BindableSlab<V : View, U : Ui<V>, B> : UiSlab<V, U>(), Bindable<B> {

    private var pendingData: B? = null

    override fun bind(data: B) {
        pendingData = data
        if (isAttached) launchAndBind()
    }

    override fun onAttach() {
        super.onAttach()
        if (pendingData != null) launchAndBind()
    }

    override fun onDetach() {
        super.onDetach()
        pendingData = null
    }

    abstract suspend fun performBind(data: B)

    private fun launchAndBind() {
        launch {
            pendingData?.let {
                performBind(it)
                pendingData = null
            }
        }
    }
}