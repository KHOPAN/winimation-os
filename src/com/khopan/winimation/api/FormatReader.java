package com.khopan.winimation.api;

import java.awt.Image;

public interface FormatReader {
	public Image read(FormatHelper helper) throws Throwable;
}
