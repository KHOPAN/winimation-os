package com.khopan.winimation.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.khopan.winimation.api.ColorList;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Color {
	int value() default ColorList.BLACK;
}
