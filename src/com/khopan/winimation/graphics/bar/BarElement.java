package com.khopan.winimation.graphics.bar;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.ThreadLocalRandom;

import com.khopan.ui.guitar.core.Repainter;
import com.khopan.winimation.graphics.Renderable;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.draw.Area.Mode;
import com.khopan.winimation.graphics.interact.Interactable;
import com.khopan.winimation.graphics.interact.Listener;

public abstract class BarElement implements Renderable, Interactable {
	final Listener listener;
	Point location;
	protected int width;
	protected int height;
	protected int size;
	protected BarDirection direction;
	protected BarAnimationDirection animationDirection;
	protected Repainter repainter;

	public BarElement() {
		this.location = new Point();
		this.listener = new Listener(() -> new Rectangle(this.location.x, this.location.y, this.width, this.height), this);
	}

	@Override
	public void update() {
		if(this.repainter != null) {
			this.repainter.repaint();
		}
	}

	@Override
	public void render(Area area) {
		area.color(ThreadLocalRandom.current().nextInt(0xFFFFFF + 1));
		area.mode(Mode.FILL);
		area.rect(0, 0, this.width, this.height);
	}
}
