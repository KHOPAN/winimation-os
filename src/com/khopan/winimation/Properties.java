package com.khopan.winimation;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.khopan.winimation.utils.Utils;

public class Properties {
	private Properties() {}

	public static final String OS_NAME = "Winimation OS";
	public static final String OS_VERSION = "1.0.0";
	public static final boolean FULLSCREEN = true;
	public static final AspectRatio SCREEN_RATIO = AspectRatio.of(16, 9);
	public static final int SCREEN_WIDTH = 1000;
	public static final Dimension SCREEN_DIMENSION = Properties.FULLSCREEN ? Utils.dimension.fullscreen() : Utils.dimension.aspectRatioWidth(Properties.SCREEN_RATIO, Properties.SCREEN_WIDTH);
	public static final int DISPLAY_FRAMERATE = Properties.getDisplayFramerate();
	public static final int BAR_HEIGHT = (int) Math.round(((double) Properties.SCREEN_DIMENSION.height) * 0.0520833333d);

	public static int getDisplayFramerate() {
		GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		double buffer = 0.0d;

		for(GraphicsDevice device : devices) {
			buffer += ((double) device.getDisplayMode().getRefreshRate());
		}

		return (int) Math.round(buffer / ((double) devices.length));
	}
}
