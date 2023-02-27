package com.khopan.winimation.graphics;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.khopan.core.property.Property;
import com.khopan.core.property.SimpleProperty;

@SuppressWarnings("unchecked")
public abstract class Boundary<T extends Boundary<T>> {
	private final Property<Integer, T> xProperty = new SimpleProperty<Integer, T>(() -> this.bounds.x, x -> this.bounds.x = x, (T) this).nullable().whenNull(0).updater(x -> this.boundsUpdateCallback(this.bounds));
	private final Property<Integer, T> yProperty = new SimpleProperty<Integer, T>(() -> this.bounds.y, y -> this.bounds.y = y, (T) this).nullable().whenNull(0).updater(y -> this.boundsUpdateCallback(this.bounds));
	private final Property<Point, T> locationProperty = new SimpleProperty<Point, T>(() -> this.bounds.getLocation(), location -> this.bounds.setLocation(location), (T) this).nullable().whenNull(new Point()).updater(location -> this.boundsUpdateCallback(this.bounds));
	private final Property<Integer, T> widthProperty = new SimpleProperty<Integer, T>(() -> this.bounds.width, width -> this.bounds.width = width, (T) this).nullable().whenNull(0).updater(width -> this.boundsUpdateCallback(this.bounds));
	private final Property<Integer, T> heightProperty = new SimpleProperty<Integer, T>(() -> this.bounds.height, height -> this.bounds.height = height, (T) this).nullable().whenNull(0).updater(height -> this.boundsUpdateCallback(this.bounds));
	private final Property<Dimension, T> sizeProperty = new SimpleProperty<Dimension, T>(() -> this.bounds.getSize(), size -> this.bounds.setSize(size), (T) this).nullable().whenNull(new Dimension()).updater(size -> this.boundsUpdateCallback(this.bounds));
	private final Property<Rectangle, T> boundsProperty = new SimpleProperty<Rectangle, T>(() -> this.bounds, bounds -> this.bounds = bounds, (T) this).nullable().whenNull(new Rectangle()).updater(bounds -> this.boundsUpdateCallback(this.bounds));
	private final Property<Boolean, T> setUpdateModeProperty = new SimpleProperty<Boolean, T>(() -> this.setUpdateMode, setUpdateMode -> this.setUpdateMode = setUpdateMode, (T) this).nullable().whenNull(false);

	protected final List<BoundaryNode> boundaryNodes;
	protected Rectangle bounds;
	protected boolean setUpdateMode;
	private Rectangle before;

	public Boundary() {
		this.boundaryNodes = new ArrayList<>();
		this.bounds = new Rectangle();
	}

	public List<BoundaryNode> boundaryNode() {
		return this.boundaryNodes;
	}

	public Property<Integer, T> x() {
		return this.xProperty;
	}

	public Property<Integer, T> y() {
		return this.yProperty;
	}

	public Property<Point, T> location() {
		return this.locationProperty;
	}

	public Property<Integer, T> width() {
		return this.widthProperty;
	}

	public Property<Integer, T> height() {
		return this.heightProperty;
	}

	public Property<Dimension, T> size() {
		return this.sizeProperty;
	}

	public Property<Rectangle, T> bounds() {
		return this.boundsProperty;
	}

	public Property<Boolean, T> setUpdateMode() {
		return this.setUpdateModeProperty;
	}

	protected void boundsUpdateCallback(Rectangle bounds) {
		for(int i = 0; i < this.boundaryNodes.size(); i++) {
			this.boundaryNodes.get(i).boundaryUpdateCallback(bounds);
		}

		if(this.setUpdateMode) {
			for(int i = 0; i < this.boundaryNodes.size(); i++) {
				this.boundaryNodes.get(i).boundaryUpdate(bounds);
			}

			this.boundsUpdate();
		} else {
			if(this.before == null) {
				for(int i = 0; i < this.boundaryNodes.size(); i++) {
					this.boundaryNodes.get(i).boundaryUpdate(bounds);
				}

				this.boundsUpdate();
			} else {
				if(this.before.x != bounds.x || this.before.y != bounds.y || this.before.width != bounds.width || this.before.height != bounds.height) {
					for(int i = 0; i < this.boundaryNodes.size(); i++) {
						this.boundaryNodes.get(i).boundaryUpdate(bounds);
					}

					this.boundsUpdate();
				}
			}
		}

		this.before = this.bounds;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	protected void boundsUpdate() {

	}
}
