package com.khopan.winimation.api;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GradientParser {
	private final BufferedImage result;

	private GradientParser(int width, int height, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
		int cases = (topLeft != null ? 1 : 0) << 3 | (topRight != null ? 1 : 0) << 2 | (bottomLeft != null ? 1 : 0) << 1 | (bottomRight != null ? 1 : 0);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int max = Math.max(width, height);
		Graphics2D Graphics = image.createGraphics();

		if(cases == 3 || cases == 12) {
			Color first;
			Color second;

			if(cases == 3) {
				first = bottomLeft;
				second = bottomRight;
			} else {
				first = topLeft;
				second = topRight;
			}

			for(int i = 0; i < max; i++) {

			}
		}

		if(cases == 5 || cases == 10) {

		}

		Graphics.dispose();
		this.result = image;
	}

	public static BufferedImage parse(int width, int height, Color topLeft, Color topRight, Color bottomLeft, Color bottomRight) {
		return new GradientParser(width, height, topLeft, topRight, bottomLeft, bottomRight).result;
	}
}
