package com.khopan.winimation.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.khopan.winimation.api.FormatReader;
import com.khopan.winimation.api.ImageFormatReader;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Format {
	Class<? extends FormatReader> value() default ImageFormatReader.class;
}
