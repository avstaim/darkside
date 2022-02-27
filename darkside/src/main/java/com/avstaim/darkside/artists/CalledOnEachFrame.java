package com.avstaim.darkside.artists;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation used to indicate the code that is called on each rendering frame of the animation.
 *
 * Avoid memory allocations and/or heavy operations.
 */
@Retention(RetentionPolicy.SOURCE)

@Target({METHOD, CONSTRUCTOR, TYPE})
public @interface CalledOnEachFrame {}
