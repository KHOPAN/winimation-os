package com.khopan.winimation;

import com.khopan.winimation.kernel.Kernel;

public class Bootloader {
	private static boolean Initialized;

	private Bootloader(String[] args) {
		WinimationOS.LOGGER.info("Loading Bootloader");
		Kernel.load();
		FileSystem.initialize();
		ApplicationLoader.initialize(args);
		ApplicationParser.initialize();
		Window.initialize();
	}

	public static void initialize(String[] args) {
		if(!Bootloader.Initialized) {
			new Bootloader(args);
			Bootloader.Initialized = true;
		}
	}
}
