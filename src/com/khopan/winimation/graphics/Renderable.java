package com.khopan.winimation.graphics;

import com.khopan.winimation.GraphicsSystem;
import com.khopan.winimation.graphics.draw.Area;

public interface Renderable {
	public void render(Area area);

	public default void update() {
		GraphicsSystem.update();
	}
}
