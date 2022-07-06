# Dark Side of Android

*Peace is a lie, there is only passion.
Through passion, I gain strength.
Through strength, I gain power.
Through power, I gain victory.
Through victory, my chains are broken.
The Force shall free me.
__The Sith Code__*

Darkside project is a set of helper tools for android developers using Kotlin benefits.

![Darkside Logo](images/darkside.png)

Darkside offers alternative (**dark**) approach to some of common android approaches:

- Prefer DSL over XML
- Prefer DSL over Builder pattern
- Prefer MVI over MVVM
- Use dedicated Ui abstractions inserad of direct View and Layout access
- Prefer Coroutines/Flow over LiveData and RxJava
- Propagate using `UseCase`'es for single-responsibility tasks insead of multi-responsible God-classes.
- Using `Result<T>` returns instead of throwing exceptions.

![Darth](images/darth.png)

### [Darkside cookies](darkside/src/main/kotlin/com/avstaim/darkside/cookies)

Set of little and usable helpers to make life a little bit sweetier.

### [Darkside artists](darkside/src/main/java/com/avstaim/darkside/artists)

Artists framework provides an Artist interface - a lightweight alternative to `android.graphics.drawable.Drawable` class. The main reasons are `PathArtist` and `MorphablePathArtist` implementations, used to seriously extend the possibilities of vector drawables and vector animations in Android.


### [Darkside animations](darkside/src/main/kotlin/com/avstaim/darkside/animations)

Kotlin DSL to describe animations in an easy-to-write and easy-to-read declarative style. Can be used in a combination with **Darkside artists** framework.

### [Darkside DSL](darkside/src/main/kotlin/com/avstaim/darkside/dsl)

DSL pack for comfortatble Android development.

### [Darkside MVI](darkside/src/main/kotlin/com/avstaim/darkside/mvi)

Darkside-compatible MVI architecture pattern to use in your android projects. 
Based on Coroutines/Flow.

### [Darkside Slabs](darkside/src/main/kotlin/com/avstaim/darkside/slab)

Slabs are small ui bricks and view controllers, designed as lightweight fragments.

![DarthDroid](images/darthdroid.jpg)

## Status

Darkside is currently in a beta stage.
