package com.khopan.winimation.utils;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.khopan.winimation.AspectRatio;
import com.khopan.winimation.Properties;

public class DimensionUtils {
	private Dimension fullscreen;

	DimensionUtils() {
		this.fullscreen = Toolkit.getDefaultToolkit().getScreenSize();
	}

	public Dimension aspectRatioWidth(AspectRatio ratio, int width) {
		return new Dimension(width, (int) Math.round((((double) width) / ratio.widthRatio) * ratio.heightRatio));
	}

	public Dimension aspectRatioHeight(AspectRatio ratio, int height) {
		return new Dimension((int) Math.round((((double) height) / ratio.heightRatio) * ratio.widthRatio), height);
	}

	public Dimension fullscreen() {
		return this.fullscreen;
	}

	public boolean isFullscreen(Dimension size) {
		if(size.width >= this.fullscreen.width && size.height >= this.fullscreen.height) {
			return true;
		}

		return false;
	}

	public int size(double factor) {
		return (int) Math.round(((((double) Properties.SCREEN_DIMENSION.width) + ((double) Properties.SCREEN_DIMENSION.height)) * 0.5d) * factor);
	}

	public double sizeDouble(double factor) {
		return ((((double) Properties.SCREEN_DIMENSION.width) + ((double) Properties.SCREEN_DIMENSION.height)) * 0.5d) * factor;
	}
}
