package com.khopan.winimation.graphics.draw;

import java.awt.Graphics;

public interface Brush {
	public void polygon(Graphics Graphics, int[] xPoints, int[] yPoints, int nPoints);
	public void oval(Graphics Graphics, int x, int y, int width, int height);
}
