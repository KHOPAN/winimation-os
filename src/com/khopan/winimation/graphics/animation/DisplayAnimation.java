package com.khopan.winimation.graphics.animation;

import java.awt.AlphaComposite;

import com.khopan.engine.animation.interpolator.Interpolator;
import com.khopan.winimation.graphics.Display;
import com.khopan.winimation.graphics.draw.Area;

public abstract class DisplayAnimation {
	public static final DisplayAnimation BLEND = new DisplayAnimation() {
		private Display oldDisplay;
		private Display display;

		@Override
		public void prepare(Display oldDisplay, Display display, int duration) {
			this.oldDisplay = oldDisplay;
			this.display = display;
		}

		@Override
		public void animate(Area area, double time) {
			if(this.oldDisplay != null) {
				Area oldDisplayArea = area.create();
				oldDisplayArea.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (1.0d - time)));
				this.oldDisplay.render(oldDisplayArea);
			}

			if(this.display != null) {
				Area displayArea = area.create();
				displayArea.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) time));
				this.display.render(displayArea);
			}
		}

		@Override
		public int getRawDuration() {
			return 1200;
		}

		@Override
		public Interpolator getInterpolator() {
			return Interpolator.LINEAR;
		}
	};

	public DisplayAnimation() {

	}

	public abstract void prepare(Display oldDisplay, Display display, int duration);
	public abstract void animate(Area area, double time);
	public abstract int getRawDuration();
	public abstract Interpolator getInterpolator();
}
