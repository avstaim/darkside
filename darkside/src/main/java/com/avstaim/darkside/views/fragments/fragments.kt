// Copyright (c) 2020 Yandex LLC. All rights reserved.
// Author: Alex Sher <avstaim@yandex-team.ru>

@file:Suppress("unused")

package com.avstaim.darkside.views.fragments

import androidx.fragment.app.FragmentActivity
import com.avstaim.darkside.cookies.noGetter

var android.view.ViewGroup.fragment: androidx.fragment.app.Fragment
    get() = noGetter()
    set(fragment) {
        val activity = context as FragmentActivity
        activity.supportFragmentManager.beginTransaction().replace(id, fragment).commit()
    }
