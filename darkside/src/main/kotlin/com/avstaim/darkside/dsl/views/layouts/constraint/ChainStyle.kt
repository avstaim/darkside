@file:Suppress("unused")

package com.avstaim.darkside.dsl.views.layouts.constraint

import androidx.constraintlayout.widget.ConstraintLayout

enum class ChainStyle(val value: Int) {
    SPREAD(ConstraintLayout.LayoutParams.CHAIN_SPREAD),
    SPREAD_INSIDE(ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE),
    PACKED(ConstraintLayout.LayoutParams.CHAIN_PACKED),
}