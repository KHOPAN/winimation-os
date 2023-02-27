package com.khopan.winimation.graphics.draw;

import java.awt.Color;
import java.awt.Graphics;

public class PaintBrush implements Brush {
	private final Color color;
	private final boolean draw;

	public PaintBrush(int color, BrushMode mode) {
		this.color = new Color((color < 0x000000 || color > 0xFFFFFF) ? 0x000000 : color);
		this.draw = mode == null ? true : BrushMode.DRAW.equals(mode);
	}

	@Override
	public void polygon(Graphics Graphics, int[] xPoints, int[] yPoints, int nPoints) {
		Graphics.setColor(this.color);

		if(this.draw) {
			Graphics.drawPolygon(xPoints, yPoints, nPoints);
		} else {
			Graphics.fillPolygon(xPoints, yPoints, nPoints);
		}
	}

	@Override
	public void oval(Graphics Graphics, int x, int y, int width, int height) {
		Graphics.setColor(this.color);

		if(this.draw) {
			Graphics.drawOval(x, y, width, height);
		} else {
			Graphics.fillOval(x, y, width, height);
		}
	}

	public static enum BrushMode {
		DRAW,
		FILL;
	}
}
