package com.khopan.winimation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.khopan.winimation.api.Handle;

class ApplicationParser {
	private static boolean Initialized;

	private ApplicationParser() {
		WinimationOS.LOGGER.info("Loading Application Initializer");
		List<Method> mainMethodList = new ArrayList<>();

		for(EntryHolder holder : EntryList.ENTRY_LIST) {
			mainMethodList.add(holder.mainMethod);
		}

		int identifier = -1;

		for(Method mainMethod : mainMethodList) {
			identifier++;

			try {
				mainMethod.invoke(null, new Handle(identifier));
			} catch(Throwable Errors) {

			}
		}
	}

	public static void initialize() {
		if(!ApplicationParser.Initialized) {
			new ApplicationParser();
			ApplicationParser.Initialized = true;
		}
	}
}
