package com.avstaim.darkside.slab

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.AbsSavedState
import android.view.View

@SuppressLint("ViewConstructor")
internal class SaveStateView(
    context: Context,
    private val slab: Slab<*>,
) : View(context) {

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        val id = slab.saveInstanceState(bundle)
        val clsName = slab.javaClass.name
        return SavedState(super.onSaveInstanceState(), clsName, id, bundle)
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) =
        if (parcelable is SavedState) {
            // View state can be restored into the same version of the code only so
            // there are no reasons to worry about obfuscation.
            if (parcelable.slabClassName == slab.javaClass.name) {
                slab.setRestoredInstanceState(parcelable.instanceId, parcelable.bundle)
            }
            super.onRestoreInstanceState(parcelable.superState)
        } else {
            super.onRestoreInstanceState(parcelable)
        }

    internal class SavedState : AbsSavedState {

        val slabClassName: String
        val instanceId: String
        val bundle: Bundle

        internal constructor(inParcel: Parcel) : super(inParcel) {
            slabClassName = inParcel.readString() ?: error("Can't read slabClassName")
            instanceId = inParcel.readString() ?: error("Can't read instanceId")
            bundle = inParcel.readBundle(javaClass.classLoader) ?: error("Can't read bundle")
        }

        internal constructor(superState: Parcelable?, clsName: String, instanceId: String, bundle: Bundle) : super(superState) {
            this.slabClassName = clsName
            this.instanceId = instanceId
            this.bundle = bundle
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.apply {
                writeString(slabClassName)
                writeString(instanceId)
                writeBundle(bundle)
            }
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(inParcel: Parcel) = SavedState(inParcel)
                override fun newArray(size: Int) = arrayOfNulls<SavedState>(size)
            }
        }
    }
}
