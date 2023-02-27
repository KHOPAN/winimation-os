package com.khopan.winimation.api;

import java.awt.Image;

public interface IconManager {
	public default Image getIconImage() {
		return null;
	}

	public default String getIconVector() {
		return null;
	}

	public boolean isVectorGraphics();
}
