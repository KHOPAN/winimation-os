package com.khopan.winimation.graphics.draw;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;

public interface Area {
	public void normal();
	public void smooth();
	public void hint(RenderingHints.Key key, Object value);
	public void color(int color);
	public void color(int red, int green, int blue);
	public void color(int red, int green, int blue, int alpha);
	public void color(Color color);
	public void mode(Mode mode);
	public void draw();
	public void fill();
	public void thickness(int thickness);
	public void font(Font font);
	public void composite(Composite composite);
	public void stroke(Stroke stroke);
	public void paint(Paint paint);

	public Color color();
	public int intColor();
	public int[] separateColor();
	public int[] alphaColor();
	public Mode mode();
	public int thickness();
	public Font font();
	public Composite composite();
	public Stroke stroke();
	public Paint paint();

	public Area create();
	public Area create(int x, int y, int width, int height);
	public Area create(Rectangle bounds);
	public Area create(int x, int y, Dimension size);
	public Area create(Point location, int width, int height);
	public Area create(Point location, Dimension size);
	public void done();
	public void rect(int x, int y, int width, int height);
	public void rect(Rectangle rectangle);
	public void rect(int x, int y, Dimension size);
	public void rect(Point location, int width, int height);
	public void rect(Point location, Dimension size);
	public void square(int x, int y, int size);
	public void square(Point location, int size);
	public void oval(int x, int y, int width, int height);
	public void oval(Rectangle bounds);
	public void oval(int x, int y, Dimension size);
	public void oval(Point location, int width, int height);
	public void oval(Point location, Dimension size);
	public void circle(int x, int y, int radius);
	public void circle(Point location, int radius);
	public void circleDiameter(int x, int y, int diameter);
	public void circleDiameter(Point location, int diameter);
	public void polygon(int[] xPoints, int[] yPoints, int nPoints);
	public void text(String text, int x, int y);
	public void textCenter(String text);
	public void textCenterX(String text, int y);
	public void textCenterY(String text, int x);
	public Point textCenterLocation(String text);
	public int textCenterX(String text);
	public int textCenterY();
	public void image(Image image);
	public void image(Image image, int x, int y);
	public void imageCenter(Image image);
	public void image(Image image, int x, int y, int width, int height);
	public void imageSmooth(Image image, int x, int y, int width, int height);
	public void imageCenter(Image image, int width, int height);
	public void imageSmoothCenter(Image image, int width, int height);
	public void imageFull(Image image);
	public void imageFullSmooth(Image image);
	public void clip(int x, int y, int width, int height);
	public void clip(Rectangle rectangle);
	public void clip(int x, int y, Dimension size);
	public void clip(Point location, int width, int height);
	public void clip(Point location, Dimension size);
	public void clip(Shape shape);
	public void rotate(double theta);
	public void rotate(double theta, double x, double y);
	public void fill(Shape shape);
	public void draw(Shape shape);
	public void translate(int x, int y);
	public void line(int x1, int y1, int x2, int y2);
	public void pixel(int x, int y);
	public FontMetrics metrics();

	public static enum Mode {
		DRAW,
		FILL;
	}
}
