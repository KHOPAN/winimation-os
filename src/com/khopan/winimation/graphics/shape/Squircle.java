package com.khopan.winimation.graphics.shape;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Squircle implements Shape {
	private GeneralPath path;
	private double a;
	private double b;
	private double c;
	private double d;
	private double e;

	public Squircle(double a, double b, double c, double d, double e) {
		this.set(a, b, c, d, e);
	}

	public Squircle(double size) {
		this(0.0d, 0.5d * size, 0.125d * size, 0.875d * size, size);
	}

	public Squircle(Squircle instance) {
		this(instance.a, instance.b, instance.c, instance.d, instance.e);
	}

	public Squircle(Squircle instance, double scale) {
		this(instance.a * scale, instance.b * scale, instance.c * scale, instance.d * scale, instance.e * scale);
	}

	public void set(double a, double b, double c, double d, double e) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.path = new GeneralPath();
		this.path.moveTo(this.a, this.b);
		this.path.curveTo(this.a, this.c, this.c, this.a, this.b, this.a);
		this.path.curveTo(this.d, this.a, this.e, this.c, this.e, this.b);
		this.path.curveTo(this.e, this.d, this.d, this.e, this.b, this.e);
		this.path.curveTo(this.c, this.e, this.a, this.d, this.a, this.b);
		this.path.closePath();
	}

	@Override
	public Rectangle getBounds() {
		return this.path.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return this.path.getBounds2D();
	}

	@Override
	public boolean contains(double x, double y) {
		return this.path.contains(x, y);
	}

	@Override
	public boolean contains(Point2D location) {
		return this.path.contains(location);
	}

	@Override
	public boolean intersects(double x, double y, double width, double height) {
		return this.path.intersects(x, y, width, height);
	}

	@Override
	public boolean intersects(Rectangle2D rectangle) {
		return this.path.intersects(rectangle);
	}

	@Override
	public boolean contains(double x, double y, double width, double height) {
		return this.path.contains(x, y, width, height);
	}

	@Override
	public boolean contains(Rectangle2D rectangle) {
		return this.path.contains(rectangle);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform transform) {
		return this.path.getPathIterator(transform);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform transform, double flatness) {
		return this.path.getPathIterator(transform, flatness);
	}
}
