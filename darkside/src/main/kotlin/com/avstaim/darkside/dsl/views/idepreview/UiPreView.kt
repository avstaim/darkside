
@file:Suppress("LeakingThis", "unused")

package com.avstaim.darkside.dsl.views.idepreview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StyleRes
import com.avstaim.darkside.R
import com.avstaim.darkside.cookies.dp
import com.avstaim.darkside.cookies.illegalArg
import com.avstaim.darkside.cookies.unsupported
import com.avstaim.darkside.dsl.views.NO_THEME
import com.avstaim.darkside.dsl.views.Ui
import com.avstaim.darkside.dsl.views.backgroundColor
import com.avstaim.darkside.dsl.views.gravityCenterVertical
import com.avstaim.darkside.dsl.views.layouts.frameLayoutParams
import com.avstaim.darkside.dsl.views.matchParent
import com.avstaim.darkside.dsl.views.padding
import com.avstaim.darkside.dsl.views.str
import com.avstaim.darkside.dsl.views.strArray
import com.avstaim.darkside.dsl.views.styledColor
import com.avstaim.darkside.dsl.views.textView
import com.avstaim.darkside.dsl.views.ui
import com.avstaim.darkside.dsl.views.wrapCtxIfNeeded
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * This class is dedicated to previewing `Ui` subclasses in the IDE.
 *
 * You can enable the preview with code or a dedicated xml file.
 *
 * Here's an example in Kotlin:
 *
 * ```kotlin
 * //region IDE preview
 * @Deprecated("For IDE preview only", level = DeprecationLevel.HIDDEN)
 * private class MainUiImplPreview(
 *     context: Context,
 *     attrs: AttributeSet? = null,
 *     defStyleAttr: Int = 0
 * ) : UiPreView(
 *     context = context.withTheme(R.style.AppTheme),
 *     attrs = attrs,
 *     defStyleAttr = defStyleAttr,
 *     createUi = { MainUiImpl(it) }
 * )
 * //endregion
 * ```
 *
 * And here is an example xml layout file that would preview a `MainUi` class in the `main` package:
 *
 * ```xml
 * <com.avstaim.darkside.dsl.views.idepreview.UiPreView
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:app="http://schemas.android.com/apk/res-auto"
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:theme="@style/AppTheme.NoActionBar"
 *     app:class_package_name_relative="main.MainUi"/>
 * ```
 *
 * Note that only the Kotlin version is safe from refactorings (such as renames, package moving…).
 *
 * If you use the xml approach, it's recommended to add it to your debug sources straightaway.
 * For the Kotlin approach, R8 or proguard will see the class is unused and will strip it so long as you
 * have `minifyEnabled = true`.
 *
 * See the sample for complete examples.
 */
open class UiPreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    @StyleRes styleRes: Int = NO_THEME,
    createUi: ((Context) -> Ui<out View>)? = null
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        backgroundColor = styledColor(android.R.attr.colorBackground)
        require(isInEditMode) { "Only intended for use in IDE!" }
        try {
            if (createUi == null) {
                init(this.context, attrs, defStyleAttr)
            } else {
                addView(
                    createUi(this.context.wrapCtxIfNeeded(styleRes)).root, frameLayoutParams(
                        matchParent, matchParent
                    )
                )
            }
        } catch (exception: IllegalArgumentException) {
            backgroundColor = Color.WHITE
            addView(
                ui(context) {
                    textView {
                        text = exception.message ?: exception.toString()
                        gravity = gravityCenterVertical
                        setTextColor(Color.BLUE)
                        padding = dp(16)
                        textSize = 24f
                    }
                }.root,
                frameLayoutParams(matchParent, matchParent)
            )
        }
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val uiClass: Class<out Ui<out View>> = withStyledAttributes(
            attrs = attrs,
            attrsRes = R.styleable.UiPreView,
            defStyleAttr = defStyleAttr,
            defStyleRes = 0
        ) { typedArray ->
            typedArray.getString(R.styleable.UiPreView_class_fully_qualified_name)?.let {
                try {
                    @Suppress("UNCHECKED_CAST")
                    Class.forName(it) as Class<out Ui<out View>>
                } catch (e: ClassNotFoundException) {
                    illegalArg("Did not find the specified class: $it")
                }
            } ?: typedArray.getString(R.styleable.UiPreView_class_package_name_relative)?.let {
                val packageName = context.packageName.removeSuffix(
                    suffix = str(R.string.ui_preview_package_name_suffix)
                )
                try {
                    @Suppress("UNCHECKED_CAST")
                    Class.forName("$packageName.$it") as Class<out Ui<out View>>
                } catch (e: ClassNotFoundException) {
                    val otherPackages = context.strArray(R.array.ui_preview_base_package_names)
                    otherPackages.fold<String, Class<out Ui<out View>>?>(null) { foundOrNull, packageNameHierarchy ->
                        foundOrNull ?: try {
                            @Suppress("UNCHECKED_CAST")
                            Class.forName("$packageNameHierarchy.$it") as Class<out Ui<out View>>
                        } catch (e: ClassNotFoundException) {
                            null
                        }
                    } ?: illegalArg(
                        "Package-name relative class \"$it\" not found!\nDid you make a typo?\n\n" +
                                "Searched in the following root packages:\n" +
                                "- $packageName\n" +
                                otherPackages.joinToString(separator = "\n", prefix = "- ")
                    )
                }
            } ?: illegalArg("No class name attribute provided")
        }
        require(!uiClass.isInterface) { "$uiClass is not instantiable because it's an interface!" }
        require(Ui::class.java.isAssignableFrom(uiClass)) { "$uiClass is not a subclass of Ui!" }
        val ui = try {
            val uiConstructor: Constructor<out Ui<out View>> = uiClass.getConstructor(Context::class.java)
            uiConstructor.newInstance(context)
        } catch (e: NoSuchMethodException) {
            val uiConstructor = uiClass.constructors.firstOrNull {
                it.parameterTypes.withIndex().all { (i, parameterType) ->
                    (i == 0 && parameterType == Context::class.java) || parameterType.isInterface
                }
            } ?: illegalArg(
                "No suitable constructor found. Need one with Context as " +
                        "first parameter, and only interface types for other parameters, if any."
            )

            @Suppress("UNUSED_ANONYMOUS_PARAMETER")
            val parameters = mutableListOf<Any>(context).also { params ->
                uiConstructor.parameterTypes.forEachIndexed { index, parameterType ->
                    if (index != 0) params += when (parameterType) {
                        CoroutineContext::class.java -> EmptyCoroutineContext
                        else -> Proxy.newProxyInstance(
                            parameterType.classLoader,
                            arrayOf(parameterType)
                        ) { proxy: Any?, method: Method, args: Array<out Any>? ->
                            when (method.declaringClass.name) {
                                "kotlinx.coroutines.CoroutineScope" -> EmptyCoroutineContext
                                else -> unsupported("Edit mode: stub implementation.")
                            }
                        }
                    }
                }
            }.toTypedArray()
            uiConstructor.newInstance(*parameters) as Ui<out View>
        }
        addView(ui.root, frameLayoutParams(matchParent, matchParent))
    }
}

private inline fun <R> View.withStyledAttributes(
    attrs: AttributeSet?,
    attrsRes: IntArray,
    defStyleAttr: Int,
    defStyleRes: Int = 0,
    func: (styledAttrs: TypedArray) -> R
): R {
    val styledAttrs = context.obtainStyledAttributes(attrs, attrsRes, defStyleAttr, defStyleRes)
    return func(styledAttrs).also { styledAttrs.recycle() }
}
