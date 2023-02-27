package com.khopan.winimation.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Viewbox {
	int value() default -1;
	int x() default -1;
	int y() default -1;
	int width() default -1;
	int height() default -1;
}
