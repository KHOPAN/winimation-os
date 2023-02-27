package com.khopan.winimation;

import java.awt.Font;

import com.khopan.winimation.utils.Utils;

public class Settings {
	private Settings() {}

	public static double AnimationDurationMultiplier;
	public static Font DisplayFont;
	public static double FontSizeMultiplier;

	static {
		Settings.AnimationDurationMultiplier = 1.0d;
		Settings.DisplayFont = new Font("Itim", Font.BOLD, 0);
		Settings.FontSizeMultiplier = 1.0d;
	}

	public static int duration(int duration) {
		return (int) Math.round(((double) duration) * Settings.AnimationDurationMultiplier);
	}

	public static Font font(double factor) {
		return Settings.DisplayFont.deriveFont((float) Utils.dimension.sizeDouble(factor));
	}

	public static Font font(double factor, int width, int height) {
		return Settings.DisplayFont.deriveFont((float) (((((double) width) + ((double) height)) * 0.5d) * factor));
	}

	public static Font font(double factor, int size) {
		return Settings.DisplayFont.deriveFont((float) (size * factor));
	}

	public static void update() {
		Window.Instance.pane.repaint();
	}
}
