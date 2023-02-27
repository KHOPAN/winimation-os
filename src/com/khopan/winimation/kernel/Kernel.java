package com.khopan.winimation.kernel;

import com.khopan.winimation.WinimationOS;

public class Kernel {
	private Kernel() {}

	public static final KernelIO io = new KernelIO();
	public static final KernelSystem system = new KernelSystem();

	public static void load() {
		WinimationOS.LOGGER.info("Loading Kernel");
	}
}
