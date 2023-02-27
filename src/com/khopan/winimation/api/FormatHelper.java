package com.khopan.winimation.api;

import java.lang.reflect.Field;

import com.khopan.winimation.api.annotation.Absolute;
import com.khopan.winimation.api.annotation.Relative;
import com.khopan.winimation.api.annotation.Scale;
import com.khopan.winimation.exception.IllegalFieldFormatException;

public class FormatHelper {
	public final int size;
	public final Class<?> declaringClass;
	public final Field field;
	public final boolean isRelativePath;
	public final double scale;
	public final double scalePercentage;

	public FormatHelper(int size, Class<?> declaringClass, Field field) {
		this.size = size;
		this.declaringClass = declaringClass;
		this.field = field;
		boolean relative = this.field.isAnnotationPresent(Relative.class);
		boolean absolute = this.field.isAnnotationPresent(Absolute.class);

		if(relative && absolute) {
			throw new IllegalFieldFormatException("@Relative and @Absolute annotations cannot be used together.");
		}

		if(!relative && !absolute) {
			this.isRelativePath = true;
		} else {
			this.isRelativePath = relative;
		}

		Scale scale = this.field.getAnnotation(Scale.class);

		if(scale != null) {
			this.scale = scale.value();
		} else {
			this.scale = 100.0d;
		}

		this.scalePercentage = this.scale * 0.01d;
	}
}
