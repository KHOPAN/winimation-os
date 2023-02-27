package com.khopan.winimation;

import java.lang.reflect.Method;

class EntryHolder {
	Class<?> applicationClass;
	Method mainMethod;

	EntryHolder(Class<?> applicationClass, Method mainMethod) {
		this.applicationClass = applicationClass;
		this.mainMethod = mainMethod;
	}
}
