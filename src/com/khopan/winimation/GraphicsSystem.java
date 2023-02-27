package com.khopan.winimation;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

public class GraphicsSystem {
	private GraphicsSystem() {}

	public static void update() {
		Window.Instance.frame.repaint();
	}

	@SuppressWarnings("deprecation")
	public static FontMetrics getFontMetrics(Font font) {
		return Toolkit.getDefaultToolkit().getFontMetrics(font);
	}
}
