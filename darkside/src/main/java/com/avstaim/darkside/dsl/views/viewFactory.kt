package com.avstaim.darkside.dsl.views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.SeekBar
import android.widget.Space
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.avstaim.darkside.slab.SlotView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import java.lang.reflect.Constructor

typealias ViewFactory<V> = (context: Context, attrRes: Int, styleRes: Int) -> V

@InternalApi
inline fun <reified V : View> factoryFor(): ViewFactory<V> = ::createView

inline fun < V : View> simpleFactory(crossinline init: (Context) -> V): ViewFactory<V> = { ctx, _, _ -> init(ctx) }

@InternalApi
object UnsupportedViewCreator {

    private val cachedStyledConstructors = mutableMapOf<Class<out View>, Constructor<out View>>()
    private val cachedStyledAttrOnlyConstructors = mutableMapOf<Class<out View>, Constructor<out View>>()
    private val cachedUnstyledConstructors = mutableMapOf<Class<out View>, Constructor<out View>>()
    private val cachedAttrConstructors = mutableMapOf<Class<out View>, Constructor<out View>>()

    fun <V : View> createStyledView(
        clazz: Class<out V>,
        context: Context,
        @AttrRes styleAttr: Int,
        @StyleRes styleRes: Int,
    ): V {
        clazz.getStyledViewConstructor()?.let {
            return it.newInstance(context, null, styleAttr, styleRes)
        }
        clazz.getStyledAttrOnlyViewConstructor()?.let {
            return it.newInstance(context, null, styleAttr)
        }
        clazz.getUnstyledViewConstructor()?.let {
            return it.newInstance(context)
        }
        clazz.getFallbackViewConstructor()?.let {
            return it.newInstance(context, null)
        }
        error("Can't create view $clazz, not suitable constructor is found")
    }

    fun <V : View> createUnstyledView(
        clazz: Class<out V>,
        context: Context,
    ): V {
        clazz.getUnstyledViewConstructor()?.let {
            return it.newInstance(context)
        }
        clazz.getStyledViewConstructor()?.let {
            return it.newInstance(context, null, 0, 0)
        }
        clazz.getStyledAttrOnlyViewConstructor()?.let {
            return it.newInstance(context, null, 0)
        }
        clazz.getFallbackViewConstructor()?.let {
            return it.newInstance(context, null)
        }
        error("Can't create view $clazz, no suitable constructor is found")
    }

    private fun <V : View> Class<out V>.getStyledViewConstructor(): Constructor<out V>? {
        cachedStyledConstructors[this]?.let {
            @Suppress("UNCHECKED_CAST")
            return it as Constructor<V>?
        }
        return try {
            getConstructor(Context::class.java, AttributeSet::class.java, Int::class.java, Int::class.java).also {
                cachedStyledConstructors[this] = it
            }
        } catch (e: NoSuchMethodException) {
            null
        }
    }
    private fun <V : View> Class<out V>.getStyledAttrOnlyViewConstructor(): Constructor<out V>? {
        cachedStyledAttrOnlyConstructors[this]?.let {
            @Suppress("UNCHECKED_CAST")
            return it as Constructor<V>?
        }
        return try {
            getConstructor(Context::class.java, AttributeSet::class.java, Int::class.java).also {
                cachedStyledAttrOnlyConstructors[this] = it
            }
        } catch (e: NoSuchMethodException) {
            null
        }
    }

    private fun <V : View> Class<out V>.getUnstyledViewConstructor(): Constructor<out V>? {
        cachedUnstyledConstructors[this]?.let {
            @Suppress("UNCHECKED_CAST")
            return it as Constructor<V>?
        }
        return try {
            getConstructor(Context::class.java).also {
                cachedUnstyledConstructors[this] = it
            }
        } catch (e: NoSuchMethodException) {
            null
        }
    }

    private fun <V : View> Class<out V>.getFallbackViewConstructor(): Constructor<out V>? {
        cachedAttrConstructors[this]?.let {
            @Suppress("UNCHECKED_CAST")
            return it as Constructor<V>?
        }
        return try {
            getConstructor(Context::class.java, AttributeSet::class.java).also {
                cachedAttrConstructors[this] = it
            }
        } catch (e: NoSuchMethodException) {
            null
        }
    }
}

@InternalApi
inline fun <reified V : View> createView(
    context: Context,
    @AttrRes styleAttr: Int,
    @StyleRes styleRes: Int,
): V =
    if (styleAttr == NO_STYLE_ATTR && styleRes == NO_STYLE_RES) {
        createUnstyledView(context)
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createStyledView(
                context = context,
                styleAttr = styleAttr,
                styleRes = styleRes,
            )
        } else {
            createStyledViewOld(
                context = context,
                styleAttr = styleAttr,
            )
        }

    }

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@InternalApi
inline fun <reified V : View> createStyledView(
    context: Context,
    @AttrRes styleAttr: Int,
    @StyleRes styleRes: Int,
): V = when (V::class.java) {
    TextView::class.java -> TextView(context, null, styleAttr, styleRes)
    AppCompatTextView::class.java -> AppCompatTextView(context, null, styleAttr)
    Button::class.java -> Button(context, null, styleAttr, styleRes)
    ImageView::class.java -> ImageView(context, null, styleAttr, styleRes)
    AppCompatImageView::class.java -> AppCompatImageView(context, null, styleAttr)
    EditText::class.java -> EditText(context, null, styleAttr, styleRes)
    AppCompatEditText::class.java -> AppCompatEditText(context, null, styleAttr)
    Spinner::class.java -> Spinner(context, null, styleAttr, styleRes)
    ImageButton::class.java -> ImageButton(context, null, styleAttr, styleRes)
    AppCompatImageButton::class.java -> AppCompatImageButton(context, null, styleAttr)
    CheckBox::class.java -> CheckBox(context, null, styleAttr, styleRes)
    AppCompatCheckBox::class.java -> AppCompatCheckBox(context, null, styleAttr)
    RadioButton::class.java -> RadioButton(context, null, styleAttr, styleRes)
    AppCompatRadioButton::class.java -> AppCompatRadioButton(context, null, styleAttr)
    CheckedTextView::class.java -> CheckedTextView(context, null, styleAttr, styleRes)
    AutoCompleteTextView::class.java -> AutoCompleteTextView(context, null, styleAttr, styleRes)
    MultiAutoCompleteTextView::class.java -> MultiAutoCompleteTextView(context, null, styleAttr, styleRes)
    RatingBar::class.java -> RatingBar(context, null, styleAttr, styleRes)
    AppCompatRatingBar::class.java -> AppCompatRatingBar(context, null, styleAttr)
    SeekBar::class.java -> SeekBar(context, null, styleAttr, styleRes)
    AppCompatSeekBar::class.java -> AppCompatSeekBar(context, null, styleAttr)
    ProgressBar::class.java -> ProgressBar(context, null, styleAttr, styleRes)
    Space::class.java -> Space(context, null, styleAttr, styleRes)
    RecyclerView::class.java -> RecyclerView(context, null, styleAttr)
    Toolbar::class.java -> Toolbar(context, null, styleAttr)
    View::class.java -> View(context, null, styleAttr, styleRes)
    FloatingActionButton::class.java -> FloatingActionButton(context, null, styleAttr)
    SwitchCompat::class.java -> SwitchMaterial(context, null, styleAttr)
    SlotView::class.java -> SlotView(context, null, styleAttr)
    else -> UnsupportedViewCreator.createStyledView(V::class.java, context, styleAttr, styleRes)
} as V

@InternalApi
inline fun <reified V : View> createStyledViewOld(
    context: Context,
    @AttrRes styleAttr: Int,
): V = when (V::class.java) {
    TextView::class.java -> TextView(context, null, styleAttr)
    AppCompatTextView::class.java -> AppCompatTextView(context, null, styleAttr)
    Button::class.java -> Button(context, null, styleAttr)
    ImageView::class.java -> ImageView(context, null, styleAttr)
    AppCompatImageView::class.java -> AppCompatImageView(context, null, styleAttr)
    EditText::class.java -> EditText(context, null, styleAttr)
    AppCompatEditText::class.java -> AppCompatEditText(context, null, styleAttr)
    Spinner::class.java -> Spinner(context, null, styleAttr)
    ImageButton::class.java -> ImageButton(context, null, styleAttr)
    AppCompatImageButton::class.java -> AppCompatImageButton(context, null, styleAttr)
    CheckBox::class.java -> CheckBox(context, null, styleAttr)
    AppCompatCheckBox::class.java -> AppCompatCheckBox(context, null, styleAttr)
    RadioButton::class.java -> RadioButton(context, null, styleAttr)
    AppCompatRadioButton::class.java -> AppCompatRadioButton(context, null, styleAttr)
    CheckedTextView::class.java -> CheckedTextView(context, null, styleAttr)
    AutoCompleteTextView::class.java -> AutoCompleteTextView(context, null, styleAttr)
    MultiAutoCompleteTextView::class.java -> MultiAutoCompleteTextView(context, null, styleAttr)
    RatingBar::class.java -> RatingBar(context, null, styleAttr)
    AppCompatRatingBar::class.java -> AppCompatRatingBar(context, null, styleAttr)
    SeekBar::class.java -> SeekBar(context, null, styleAttr)
    AppCompatSeekBar::class.java -> AppCompatSeekBar(context, null, styleAttr)
    ProgressBar::class.java -> ProgressBar(context, null, styleAttr)
    Space::class.java -> Space(context, null, styleAttr)
    RecyclerView::class.java -> RecyclerView(context, null, styleAttr)
    Toolbar::class.java -> Toolbar(context, null, styleAttr)
    View::class.java -> View(context, null, styleAttr)
    FloatingActionButton::class.java -> FloatingActionButton(context, null, styleAttr)
    SwitchCompat::class.java -> SwitchMaterial(context, null, styleAttr)
    else -> UnsupportedViewCreator.createStyledView(V::class.java, context, styleAttr, 0)
} as V

@InternalApi
inline fun <reified V : View> createUnstyledView(
    context: Context,
): V = when (V::class.java) {
    TextView::class.java, AppCompatTextView::class.java -> AppCompatTextView(context)
    Button::class.java -> Button(context)
    ImageView::class.java, AppCompatImageView::class.java -> AppCompatImageView(context)
    EditText::class.java, AppCompatEditText::class.java -> AppCompatEditText(context)
    Spinner::class.java -> Spinner(context)
    ImageButton::class.java, AppCompatImageButton::class.java -> AppCompatImageButton(context)
    CheckBox::class.java, AppCompatCheckBox::class.java -> AppCompatCheckBox(context)
    RadioButton::class.java, AppCompatRadioButton::class.java -> AppCompatRadioButton(context)
    RadioGroup::class.java -> RadioGroup(context)
    CheckedTextView::class.java -> CheckedTextView(context)
    AutoCompleteTextView::class.java -> AutoCompleteTextView(context)
    MultiAutoCompleteTextView::class.java -> MultiAutoCompleteTextView(context)
    RatingBar::class.java, AppCompatRatingBar::class.java -> AppCompatRatingBar(context)
    SeekBar::class.java, AppCompatSeekBar::class.java -> AppCompatSeekBar(context)
    ProgressBar::class.java -> ProgressBar(context)
    Space::class.java -> Space(context)
    RecyclerView::class.java -> RecyclerView(context)
    View::class.java -> View(context)
    Toolbar::class.java -> Toolbar(context)
    FloatingActionButton::class.java -> FloatingActionButton(context)
    SwitchCompat::class.java -> SwitchMaterial(context)
    else -> UnsupportedViewCreator.createUnstyledView(V::class.java, context)
} as V

@InternalApi
@AttrRes
inline fun <reified V : View> resolveDefaultStyleAttrForView(): Int  = when (V::class.java) {
    TextView::class.java, AppCompatTextView::class.java -> android.R.attr.textViewStyle
    Button::class.java -> android.R.attr.buttonStyle
    EditText::class.java -> android.R.attr.editTextStyle
    AppCompatEditText::class.java -> androidx.appcompat.R.attr.editTextStyle
    Spinner::class.java -> android.R.attr.spinnerStyle
    ImageButton::class.java -> android.R.attr.imageButtonStyle
    AppCompatImageButton::class.java -> androidx.appcompat.R.attr.imageButtonStyle
    CheckBox::class.java -> android.R.attr.checkboxStyle
    AppCompatCheckBox::class.java -> androidx.appcompat.R.attr.checkboxStyle
    RadioButton::class.java -> android.R.attr.radioButtonStyle
    AppCompatRadioButton::class.java -> androidx.appcompat.R.attr.radioButtonStyle
    CheckedTextView::class.java -> android.R.attr.checkedTextViewStyle
    AutoCompleteTextView::class.java -> android.R.attr.autoCompleteTextViewStyle
    MultiAutoCompleteTextView::class.java -> android.R.attr.autoCompleteTextViewStyle
    RatingBar::class.java -> android.R.attr.ratingBarStyle
    AppCompatRatingBar::class.java -> androidx.appcompat.R.attr.ratingBarStyle
    SeekBar::class.java -> android.R.attr.seekBarStyle
    AppCompatSeekBar::class.java -> androidx.appcompat.R.attr.seekBarStyle
    ProgressBar::class.java -> android.R.attr.progressBarStyle
    RecyclerView::class.java -> androidx.recyclerview.R.attr.recyclerViewStyle
    CoordinatorLayout::class.java -> androidx.coordinatorlayout.R.attr.coordinatorLayoutStyle
    Toolbar::class.java -> androidx.appcompat.R.attr.toolbarStyle
    FloatingActionButton::class.java -> com.google.android.material.R.attr.floatingActionButtonStyle
    SwitchCompat::class.java,
    SwitchMaterial::class.java -> com.google.android.material.R.attr.switchStyle
    else -> NO_STYLE_ATTR
}
