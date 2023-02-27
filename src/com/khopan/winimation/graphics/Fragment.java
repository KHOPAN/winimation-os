package com.khopan.winimation.graphics;

import java.awt.Dimension;

import com.khopan.winimation.graphics.draw.Area;

public abstract class Fragment implements Renderable {
	protected int width;
	protected int height;

	public Fragment() {

	}

	public void size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void size(Dimension size) {
		this.size(size.width, size.height);
	}

	@Override
	public void render(Area area) {

	}
}
