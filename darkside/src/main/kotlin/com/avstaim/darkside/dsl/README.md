# DSL

*A domain-specific language (DSL) is a computer language specialized to a particular application domain.*

One of the powers of Kotlin language is ability to create DSLs for every purpose you need. 

Darkside presents following DSLs for your purpose:

## [View Dsl](views)

By default, UI in Android is written using XML, inflated in runtime to real views.
That is inconvenient in the following ways:

- It is not typesafe;
- It is not null-safe;
- It forces you to write almost the same code for every layout you make;
- XML is parsed on the device wasting CPU time and battery;
- Most of all, it allows no code reuse.

While you can create UI programmatically, it's hardly done because it's somewhat ugly and hard to maintain. Here's a plain Kotlin version (one in Java is even longer):

```kotlin
val act = this
val layout = LinearLayout(act)
layout.orientation = LinearLayout.VERTICAL
val name = EditText(act)
val button = Button(act)
button.text = "Say Hello"
button.setOnClickListener {
    Toast.makeText(act, "Hello, ${name.text}!", Toast.LENGTH_SHORT).show()
}
layout.addView(name)
layout.addView(button)
```

Darkside allows you to create views by DSL (inspired by **Anko Layouts** and **Splitties Views DSL**).

```kotlin

class HelloUi(context: Context) : LayoutUi<LinearLayout>(context) { 

    val name = editText()
    val sayHelloButton = button {
        onClick {
            showToast("Hello, ${name.text}!")
        }
    }

    override fun ViewBuilder.layout() = verticalLayout {
        name {
            layoutParams = layoutParams {
                width = matchParent
                height = wrapContent
            }
        }
        
        sayHelloButton {
            layoutParams = layoutParams {
                width = wrapContent
                height = wrapContent
            }
        }
    }
}
```
Note that onClick() supports coroutines (accepts suspending lambda) so you can write your asynchronous code without explicit coroutine starting.


## [Alert Dsl](alert)

Alert dsl can replace clumsy `AlertDialog.Builder` calls.

Can be used for very simple dialogs:

```kotlin
requireContext().showAlert {
    title = getString(R.string.accounts_stash)
    message = stashValue ?: "null"
    positiveButton("Ok") {}
}
```

... and very complicated ones as well:

```kotlin
context.showAlert {
    title = "Enter ${flagWithValue.flag.key} value"

    customView {
        verticalLayout {

            val recycler = recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = arrayAdapter

                itemTouchHelper =
                    horizontalSwipeItemTouchHelper<JsonArrayAdapter.ViewHolder> { viewHolder, _ ->
                        arrayAdapter.removeLine(viewHolder.layoutPosition)
                    }

                layoutParams = layoutParams {
                    width = matchParent
                    height = dp(400)
                }
            }
            // Empty editText is required here to be able to show keyboard inside recyclerView
            editText {
                layoutParams = layoutParams {
                    width = 0
                    height = 0
                }
            }
            horizontalLayout {
                button {
                    text = "+"
                    onClick {
                        arrayAdapter.addLine()
                        delay(millis = 100)
                        val vh = recycler.findViewHolderForAdapterPosition(arrayAdapter.itemCount - 1)
                            as? JsonArrayAdapter.ViewHolder
                        vh?.ui?.label?.performClick()
                    }
                }
                button {
                    text = "-"
                    onClick {
                        arrayAdapter.removeLastLine()
                    }
                }
            }
        }
    }

    onShow { dialog ->
        dialog.window
            ?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
            )
    }

    positiveButton("Ok") {
        experimentsOverrides.override(
            key = flagWithValue.flag.key,
            value = flagWithValue.flag.serialize(arrayAdapter.currentData),
        )
        refresh()
    }

    negativeButton("Cancel") { dialog ->
        dialog.cancel()
    }

    neutralButton("Don't override") { dialog ->
        experimentsOverrides.override(flagWithValue.flag.key, null)
        dialog.dismiss()
        refresh()
    }
}
```

## [Json Dsl](json)

TODO: Json dsl goes here.

## [Menu Dsl](menu)

TODO: Menu dsl goes here.

## [Preference Dsl](preferences)

TODO: Preference dsl goes here.


__To be continued...__