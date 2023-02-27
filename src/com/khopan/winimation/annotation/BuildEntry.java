package com.khopan.winimation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated class is part of Winimation OS build class, for example Display, Fragment,
 * Item, Boundary, Popup, etc.
 * 
 * Most of the time the class is abstract to prevent accidently creating new instance with no context in
 * it, like no fragment, no graphics drawing, etc.
 * @author puthi
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface BuildEntry {

}
