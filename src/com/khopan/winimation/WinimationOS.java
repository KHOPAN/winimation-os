package com.khopan.winimation;

import com.khopan.winimation.logger.Logger;

public class WinimationOS {
	public static final Logger LOGGER = new SimpleLogger();

	public static void main(String[] args) {
		Bootloader.initialize(args);
	}
}
