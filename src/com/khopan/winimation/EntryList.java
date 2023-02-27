package com.khopan.winimation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.khopan.winimation.api.Handle;

class EntryList {
	static final List<EntryHolder> ENTRY_LIST = new ArrayList<>();

	static void addTest(Class<?> entryClass) {
		Method[] methods = entryClass.getDeclaredMethods();
		Method mainMethod = null;

		for(Method method : methods) {
			int modifiers = method.getModifiers();

			if(Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && method.getReturnType().equals(Void.TYPE)) {
				String name = method.getName();
				Class<?>[] parameter = method.getParameterTypes();

				if(parameter.length == 1 && parameter[0].equals(Handle.class) && name.equals("winimationMain")) {
					mainMethod = method;
				}
			}
		}

		/*try {
			mainMethod.invoke(null, new Handle(0));
		} catch(Throwable Errors) {

		}*/

		EntryList.add(new EntryHolder(entryClass, mainMethod));
	}

	static void add(EntryHolder holder) {
		EntryList.ENTRY_LIST.add(holder);
	}
}
