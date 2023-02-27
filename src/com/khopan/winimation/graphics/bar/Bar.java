package com.khopan.winimation.graphics.bar;

import java.awt.AlphaComposite;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.khopan.core.property.Property;
import com.khopan.core.property.SimpleProperty;
import com.khopan.core.property.state.SimpleState;
import com.khopan.core.property.state.State;
import com.khopan.engine.animation.animation.data.DoubleTransition;
import com.khopan.engine.animation.interpolator.Interpolator;
import com.khopan.ui.guitar.core.Repainter;
import com.khopan.winimation.Properties;
import com.khopan.winimation.Settings;
import com.khopan.winimation.graphics.Boundary;
import com.khopan.winimation.graphics.Renderable;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.interact.Interactable;
import com.khopan.winimation.graphics.interact.Mouse;

@SuppressWarnings("unchecked")
public abstract class Bar<T extends Bar<T>> extends Boundary<T> implements Renderable, Interactable {
	/*private final Property<Integer, Bar> xProperty = new SimpleProperty<Integer, Bar>(() -> this.bounds.x, x -> super.bounds.x = x, this).nullable().whenNull(0).updater(x -> this.boundsUpdateCallback(this.bounds));
	private final Property<Integer, Bar> yProperty = new SimpleProperty<Integer, Bar>(() -> this.bounds.y, y -> super.bounds.y = y, this).nullable().whenNull(0).updater(y -> this.boundsUpdateCallback(this.bounds));
	private final Property<Point, Bar> locationProperty = new SimpleProperty<Point, Bar>(() -> this.bounds.getLocation(), location -> super.bounds.setLocation(location), this).nullable().whenNull(new Point()).updater(location -> this.boundsUpdateCallback(this.bounds));
	private final Property<Integer, Bar> widthProperty = new SimpleProperty<Integer, Bar>(() -> this.bounds.width, width -> super.bounds.width = width, this).nullable().whenNull(0).updater(width -> this.boundsUpdateCallback(this.bounds));
	private final Property<Integer, Bar> heightProperty = new SimpleProperty<Integer, Bar>(() -> this.bounds.height, height -> super.bounds.height = height, this).nullable().whenNull(0).updater(height -> this.boundsUpdateCallback(this.bounds));
	private final Property<Dimension, Bar> sizeProperty = new SimpleProperty<Dimension, Bar>(() -> this.bounds.getSize(), size -> super.bounds.setSize(size), this).nullable().whenNull(new Dimension()).updater(size -> this.boundsUpdateCallback(this.bounds));
	private final Property<Rectangle, Bar> boundsProperty = new SimpleProperty<Rectangle, Bar>(() -> this.bounds, bounds -> super.bounds = bounds, this).nullable().whenNull(new Rectangle()).updater(bounds -> this.boundsUpdateCallback(this.bounds));*/
	private final Property<Repainter, T> repainterProperty = new SimpleProperty<Repainter, T>(() -> this.repainter, repainter -> this.repainter = repainter, (T) this).nullable();
	private final Property<BarDirection, T> directionProperty = new SimpleProperty<BarDirection, T>(() -> this.direction, direction -> this.direction = direction, (T) this).nullable().whenNull(BarDirection.HORIZONTAL).updater(direction -> this.boundsUpdate());
	private final Property<BarAnimationDirection, T> animationDirectionProperty = new SimpleProperty<BarAnimationDirection, T>(() -> this.animationDirection, animationDirection -> this.animationDirection = animationDirection, (T) this).nullable().whenNull(BarAnimationDirection.BOTTOM_RIGHT).updater(animationDirection -> this.boundsUpdate());
	private final Property<Float, T> transparentProperty = new SimpleProperty<Float, T>(() -> this.setTransparent, transparent -> {
		if(transparent < 0.0f || transparent > 1.0f) {
			throw new IllegalArgumentException("'transparent' must be in range >= 0.0f && <= 1.0f");
		}

		this.setTransparent = transparent;
	}, (T) this).nullable().whenNull(0.0f);

	private final State<T> visibleState = new SimpleState<T>(() -> this.visible, visible -> this.visible = visible, (T) this).updater(visible -> {if(visible) this.show(); else this.hide();});

	private final DoubleTransition transition;
	private final List<BarElement> element;
	private Point fromAnimateLocation;
	private BarDirection direction;
	private BarAnimationDirection animationDirection;
	protected Rectangle bounds;
	private Repainter repainter;
	private boolean visible;
	private float transparent;
	private float setTransparent;

	public Bar() {
		this.bounds = super.bounds;
		this.element = new ArrayList<>();
		this.repainter = () -> this.update();
		this.direction = BarDirection.HORIZONTAL;
		this.animationDirection = BarAnimationDirection.BOTTOM_RIGHT;
		this.setTransparent = 1.0f;
		this.transition = new DoubleTransition();
		this.transition.setDuration(Settings.duration(750));
		this.transition.setFramerate(Properties.DISPLAY_FRAMERATE);
		this.transition.setValue(0.0d, 1.0d);
		this.transition.setInterpolator(Interpolator.CUBIC_EASE_IN_OUT);
		this.transition.onlyPlayWhenDone();
		this.transition.noJump();
		this.transition.setInvoker(value -> {
			if(value == 0.0d) {
				this.bounds = new Rectangle();
			} else {
				this.bounds = new Rectangle(
						(int) Math.round(value * (((double) super.bounds.x) - ((double) this.fromAnimateLocation.x)) + ((double) this.fromAnimateLocation.x)),
						(int) Math.round(value * (((double) super.bounds.y) - ((double) this.fromAnimateLocation.y)) + ((double) this.fromAnimateLocation.y)),
						super.bounds.width,
						super.bounds.height
						);
			}

			float transparent = (float) (value * ((double) this.setTransparent));
			this.transparent = transparent > 1.0f ? 1.0f : transparent < 0.0f ? 0.0f : transparent;

			if(this.repainter != null) {
				this.repainter.repaint();
			}
		});
	}

	@Override
	public void mouseInteraction(Mouse mouse) {
		boolean horizontal = BarDirection.HORIZONTAL.equals(this.direction);
		int unit = 0;

		for(int i = 0; i < this.element.size(); i++) {
			BarElement element = this.element.get(i);
			int size = element.size;

			if(element.size <= 0) {
				continue;
			}

			element.width = horizontal ? size : this.bounds.width;
			element.height = horizontal ? this.bounds.height : size;
			element.direction = this.direction;
			element.animationDirection = this.animationDirection;
			element.repainter = this.repainter;
			element.location = new Point(horizontal ? unit : 0, horizontal ? 0 : unit);
			element.listener.mouseInteraction(mouse);
			unit += size;
		}
	}

	public Property<Repainter, T> repainter() {
		return this.repainterProperty;
	}

	public List<BarElement> element() {
		return this.element;
	}

	public void add(BarElement element) {
		this.element.add(element);
	}

	public void show() {
		this.transition.begin();
	}

	public void hide() {
		this.transition.reverseBegin();
	}

	@Override
	protected void boundsUpdate() {
		boolean horizontal = BarDirection.HORIZONTAL.equals(this.direction);
		boolean topLeft = BarAnimationDirection.TOP_LEFT.equals(this.animationDirection);
		this.fromAnimateLocation = new Point(horizontal ? super.bounds.x : topLeft ? super.bounds.x - super.bounds.width : super.bounds.x + super.bounds.width, horizontal ? topLeft ? super.bounds.y - super.bounds.height : super.bounds.y + super.bounds.height : super.bounds.y);
	}

	@Override
	public Rectangle getBounds() {
		return this.bounds;
	}

	/*@Override
	public Property<Integer, Bar> x() {
		return this.xProperty;
	}

	@Override
	public Property<Integer, Bar> y() {
		return this.yProperty;
	}

	@Override
	public Property<Point, Bar> location() {
		return this.locationProperty;
	}

	@Override
	public Property<Integer, Bar> width() {
		return this.widthProperty;
	}

	@Override
	public Property<Integer, Bar> height() {
		return this.heightProperty;
	}

	@Override
	public Property<Dimension, Bar> size() {
		return this.sizeProperty;
	}

	@Override
	public Property<Rectangle, Bar> bounds() {
		return this.boundsProperty;
	}*/

	public Property<BarDirection, T> direction() {
		return this.directionProperty;
	}

	public Property<BarAnimationDirection, T> animationDirection() {
		return this.animationDirectionProperty;
	}

	public Property<Float, T> transparent() {
		return this.transparentProperty;
	}

	public State<T> visible() {
		return this.visibleState;
	}

	@Override
	public void render(Area area) {
		Area bar = area.create();

		if(this.bounds != null) {
			bar.clip(super.bounds);
			bar.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.transparent));
			bar.color(0x000000);
			bar.fill(this.bounds);
			boolean horizontal = BarDirection.HORIZONTAL.equals(this.direction);
			int unit = 0;

			for(int i = 0; i < this.element.size(); i++) {
				BarElement element = this.element.get(i);
				int size = element.size;

				if(element.size <= 0) {
					continue;
				}

				element.width = horizontal ? size : this.bounds.width;
				element.height = horizontal ? this.bounds.height : size;
				element.direction = this.direction;
				element.animationDirection = this.animationDirection;
				element.repainter = this.repainter;
				element.render(bar.create(horizontal ? unit : this.bounds.x, horizontal ? this.bounds.y : unit, element.width, element.height));
				unit += size;
			}
		}
	}
}
