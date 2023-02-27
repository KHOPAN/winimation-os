package com.khopan.winimation.graphics;

import java.awt.Dimension;

import com.khopan.winimation.WindowPane;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.draw.Area.Mode;

public abstract class Display implements Renderable {
	public WindowPane pane;
	protected Fragment fragment;
	protected int width;
	protected int height;

	public Display() {

	}

	public void load() {

	}

	public void size(int width, int height) {
		this.width = width;
		this.height = height;

		if(this.fragment != null) {
			this.fragment.size(this.width, this.height);
		}
	}

	public void size(Dimension size) {
		this.size(size.width, size.height);
	}

	public void setFragment(Fragment fragment) {
		if(fragment == null) {
			throw new NullPointerException("'fragment' cannot be null.");
		}

		this.fragment = fragment;
		this.fragment.size(this.width, this.height);
	}

	@Override
	public void render(Area area) {
		if(this.fragment != null) {
			this.fragment.render(area);
		} else {
			area.color(0x000000);
			area.mode(Mode.FILL);
			area.rect(0, 0, this.width, this.height);
		}
	}
}
