package com.khopan.winimation.graphics;

import java.awt.Dimension;

import com.khopan.winimation.annotation.BuildEntry;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.interact.Interactable;

@BuildEntry
public abstract class Popup implements Renderable, Interactable {
	public final int roundness;
	public int width;
	public int height;
	public boolean drawBasePlane;

	public Popup() {
		this.roundness = this.getRoundness();
		this.drawBasePlane = true;
	}

	@Override
	public void render(Area area) {

	}

	public int getRoundness() {
		return 20;
	}

	public Dimension getSize() {
		return new Dimension(100, 100);
	}
}
