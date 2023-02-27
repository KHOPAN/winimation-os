package com.khopan.winimation.graphics;

import java.awt.Rectangle;

import com.khopan.winimation.Properties;
import com.khopan.winimation.WindowPane;
import com.khopan.winimation.graphics.bar.Bar;
import com.khopan.winimation.graphics.bar.BarAnimationDirection;
import com.khopan.winimation.graphics.bar.BarDirection;
import com.khopan.winimation.graphics.bar.TextElement;

public class PowerBar extends Bar<PowerBar> {
	private final WindowPane pane;

	public PowerBar(WindowPane pane) {
		this.pane = pane;
		this.bounds().set(new Rectangle(0, Properties.BAR_HEIGHT, Properties.SCREEN_DIMENSION.width, Properties.BAR_HEIGHT));
		this.animationDirection().set(BarAnimationDirection.TOP_LEFT);
		this.direction().set(BarDirection.HORIZONTAL);
		this.transparent().set(0.70f);
		this.element().add(new TextElement("Sleep"));
		this.element().add(new TextElement("Shutdown").action().set(this.pane :: shutdownPopup));
		this.element().add(new TextElement("Restart"));
	}

	@Override
	public void update() {
		this.pane.repaint();
	}
}
