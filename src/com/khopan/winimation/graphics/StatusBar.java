package com.khopan.winimation.graphics;

import java.awt.Rectangle;

import com.khopan.winimation.Properties;
import com.khopan.winimation.WindowPane;
import com.khopan.winimation.graphics.bar.Bar;
import com.khopan.winimation.graphics.bar.BarAnimationDirection;
import com.khopan.winimation.graphics.bar.BarDirection;
import com.khopan.winimation.graphics.bar.TextElement;

public class StatusBar extends Bar<StatusBar> {
	private final WindowPane pane;

	public StatusBar(WindowPane pane) {
		this.pane = pane;
		this.bounds().set(new Rectangle(0, 0, Properties.SCREEN_DIMENSION.width, Properties.BAR_HEIGHT));
		this.animationDirection().set(BarAnimationDirection.TOP_LEFT);
		this.direction().set(BarDirection.HORIZONTAL);
		this.transparent().set(0.75f);
		this.element().add(new TextElement("Power").action().set(() -> pane.togglePowerBar()));
		this.element().add(new TextElement("Quick Exit").action().set(() -> System.exit(0)));
		this.element().add(new TimeElement());

		//Utils.time.wait(2000, this :: show);
		//Utils.time.wait(2400, this :: hide);
		//Utils.time.wait(2800, this :: show);
		//Utils.time.wait(3500, this :: hide);
	}

	@Override
	public void update() {
		this.pane.repaint();
	}
}
