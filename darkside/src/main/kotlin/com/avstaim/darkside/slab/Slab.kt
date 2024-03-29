package com.avstaim.darkside.slab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.cookies.runIfIs
import com.avstaim.darkside.cookies.takeIfIs
import com.avstaim.darkside.service.KAssert
import com.avstaim.darkside.service.KLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

abstract class Slab<V : View> : SlabLifecycle, CoroutineScope, ActivityResultCaller {

    abstract val view: V

    val isAttached: Boolean
        get() = slabController.isAttached

    private val dispatcher = PausableDispatcher(Dispatchers.Main.immediate)
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job

    @Suppress("LeakingThis")
    private val slabController = SlabController(
        targetLifecycle = this,
        delayAttachToLayout = true,
    )

    private var restoredBundle: Bundle? = null
    private var saveStateView: SaveStateView? = null

    private var _uniqueInstanceId: String? = null
    internal val uniqueInstanceId: String
        get() = _uniqueInstanceId
            ?: UUID.randomUUID().toString().also { _uniqueInstanceId = it }

    private var viewWasInitialized = false

    private var activityDestroyListener: () -> Unit = {}

    private val lifecycleOwner = SlabLifecycleOwner()

    private val nextLocalRequestCode = AtomicInteger()

    protected open val activityResultKey: String
        get() = uniqueInstanceId

    /**
     * Called when the view is attached to the window. No activity of this component must start before this event.
     *
     * @param savedState Bundle
     */
    @CallSuper
    open fun onAttach(savedState: Bundle?) = Unit

    /**
     * Called when the view is attached to the window. No activity of this component must start before this event.
     */
    @CallSuper
    override fun onAttach() {
        dispatcher.resume()
        onAttach(restoredBundle)
        restoredBundle = null
        activityDestroyListener = watchForActivityDestroy()
    }

    /**
     * Close to [Activity.onSaveInstanceState] and [Fragment.onSaveInstanceState]
     *
     * See in README.md limitations applied for this mechanism.
     */
    @CallSuper
    open fun onSaveInstanceState(outState: Bundle?) = Unit

    /**
     * Same as [Activity.onResume] or [Fragment.onResume].
     */
    @CallSuper
    override fun onResume() {
        lifecycleOwner.lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    /**
     * Same as [Activity.onPause] or [Fragment.onPause].
     */
    @CallSuper
    override fun onPause() {
        lifecycleOwner.lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    /**
     * Same as [Activity.onStart] or [Fragment.onStart].
     */
    @CallSuper
    override fun onStart() {
        lifecycleOwner.lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    /**
     * Same as [Activity.onStop] or [Fragment.onStop].
     */
    @CallSuper
    override fun onStop() {
        lifecycleOwner.lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    /**
     * Called when the view is detached from the window or activity is destroyed.
     * [Slab] is must be able to be freed by GC after that.
     */
    @CallSuper
    override fun onDetach() {
        activityDestroyListener.invoke()
        dispatcher.pause()
        job.cancelChildren()
    }

    @CallSuper
    override fun onCreate() {
        lifecycleOwner.lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    @CallSuper
    override fun onDestroy() {
        job.cancel()
        dispatcher.reset()
        lifecycleOwner.lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    @CallSuper
    override fun onConfigurationChanged(newConfig: Configuration?) = Unit

    open fun V.overrideLayoutParams(): ViewGroup.LayoutParams? = null

    /**
     * See [Slot.insert].
     */
    fun insertInto(slot: Slot): Slot {
        KAssert.assertMainThread()
        return slot.insert(this)
    }

    fun replaceWith(slab: Slab<*>): Slot {
        // Brick is not attached to the window
        checkNotNull(view.parent)
        slab.replaceWithSelf(view)
        return SlabAsSlot(slab, view)
    }

    internal fun setRestoredInstanceState(instanceId: String, savedState: Bundle) {
        KAssert.assertTrue(_uniqueInstanceId == null || _uniqueInstanceId == instanceId) {
            "uniqueInstanceId initialized before setRestoredInstanceState"
        }
        _uniqueInstanceId = instanceId
        restoredBundle = savedState
    }

    internal fun saveInstanceState(bundle: Bundle?): String {
        onSaveInstanceState(bundle)
        return uniqueInstanceId
    }

    /**
     * Replaces the given [View] with the [View] controlled by this [Slab]. LayoutParams will be applied to the replacement view.
     *
     * @param viewToReplace must be attached to the parent.
     */
    @SuppressLint("ResourceType")
    internal fun replaceWithSelf(viewToReplace: View): View {
        KAssert.assertMainThread()
        val viewParent = viewToReplace.parent
        check(viewParent is ViewGroup) { "viewToReplace must be attached to parent" }

        if (view === viewToReplace) return viewToReplace
        if (!viewWasInitialized) {
            viewWasInitialized = true
            onCreate()
            view.addOnAttachStateChangeListener(slabController)
        }
        val index = viewParent.indexOfChild(viewToReplace)
        viewParent.removeViewInLayout(viewToReplace)
        if (viewToReplace.id != View.NO_ID) { // overridden id
            view.id = viewToReplace.id
        }
        val saveStateContainer = isSaveStateSupported(view)
        if (viewToReplace.id != View.NO_ID && saveStateContainer != null) {
            if (saveStateView == null) {
                saveStateView = SaveStateView(view.context, slab = this).apply {
                    visibility = View.GONE
                    id = viewToReplace.id and 0x00FFFFFF or 0x19000000
                }
                saveStateContainer.addView(
                    saveStateView,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                )
            }
        }
        val layoutParams = view.overrideLayoutParams() ?: viewToReplace.layoutParams
        if (layoutParams != null) {
            viewParent.addView(view, index, layoutParams)
        } else {
            viewParent.addView(view, index)
        }
        return view
    }

    internal fun extractView(): View {
        KAssert.assertMainThread()

        if (!viewWasInitialized) {
            viewWasInitialized = true
            onCreate()
            view.addOnAttachStateChangeListener(slabController)
        }
        val saveStateContainer = isSaveStateSupported(view)
        if (view.id != View.NO_ID && saveStateContainer != null) {
            if (saveStateView == null) {
                saveStateView = SaveStateView(view.context, slab = this).apply {
                    visibility = View.GONE
                    id = view.id and 0x00FFFFFF or 0x19000000
                }
                saveStateContainer.addView(
                    saveStateView,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                )
            }
        }
        view.overrideLayoutParams()?.let { view.layoutParams = it }
        return view
    }

    private fun isSaveStateSupported(view: View): ViewGroup? =
        if (view is ViewGroup) {
            when (view) {
                is RecyclerView -> null
                is ScrollView -> null
                else -> view
            }
        } else null


    // TODO(avstaim): find a way to access context before onAttach to make sure never-attached slabs are also destroyed
    private fun watchForActivityDestroy(): () -> Unit {
        val activityLifecycle = getActivityLifecycle()
        if (activityLifecycle == null) {
            KLog.e {
                "View is not set or not in lifecycle-managed context. onDestroy() will never be called."
            }
            return {}
        }
        val lifecycleEventObserver =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    onDestroy()
                }
            }
        activityLifecycle.addObserver(lifecycleEventObserver)
        return { activityLifecycle.removeObserver(lifecycleEventObserver) }
    }

    private fun getActivityLifecycle(): Lifecycle? =
        view.context.runIfIs<LifecycleOwner, Lifecycle> { lifecycleOwner ->
            lifecycleOwner.lifecycle
        }

    override fun <I : Any?, O : Any?> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>,
    ): ActivityResultLauncher<I> =
        registerForActivityResult(contract, getComponentActivity().activityResultRegistry, callback)

    override fun <I : Any?, O : Any?> registerForActivityResult(
        contract: ActivityResultContract<I, O>,
        registry: ActivityResultRegistry,
        callback: ActivityResultCallback<O>,
    ): ActivityResultLauncher<I> = registry.register(
        "slab_${activityResultKey}_rq#${nextLocalRequestCode.getAndIncrement()}",
        lifecycleOwner,
        contract,
        callback,
    )

    private fun getComponentActivity(): ComponentActivity =
        SlabHooks
            .findActivity(view.context)
            .takeIfIs<ComponentActivity>()
            ?: SlabHooks[view.context].requireActivity()

    private class SlabLifecycleOwner : LifecycleOwner {

        val lifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle = lifecycleRegistry
    }
}
