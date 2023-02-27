package com.khopan.winimation.graphics.bar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import com.khopan.core.color.ColorInterpolator;
import com.khopan.core.property.Property;
import com.khopan.core.property.SimpleProperty;
import com.khopan.engine.animation.animation.data.DoubleTransition;
import com.khopan.engine.animation.interpolator.Interpolator;
import com.khopan.winimation.GraphicsSystem;
import com.khopan.winimation.Properties;
import com.khopan.winimation.Settings;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.draw.Area.Mode;
import com.khopan.winimation.graphics.interact.Mouse;

public class TextElement extends BarElement {
	private static final String DEFAULT_TEXT = "Text Element";

	private final Property<String, TextElement> textProperty = new SimpleProperty<String, TextElement>(() -> this.text, text -> this.text = text, this).nullable().whenNull(TextElement.DEFAULT_TEXT).updater(text -> this.updateText());
	private final Property<TextDirection, TextElement> textDirectionProperty = new SimpleProperty<TextDirection, TextElement>(() -> this.direction, direction -> this.direction = direction, this).nullable().whenNull(TextDirection.RIGHT).updater(direction -> this.updateText());
	private final Property<Runnable, TextElement> actionProperty = new SimpleProperty<Runnable, TextElement>(() -> this.action, action -> this.action = action, this).nullable();

	private final DoubleTransition transition;
	private ColorInterpolator foreground;
	private ColorInterpolator background;
	private String text;
	private Font font;
	private TextDirection direction;
	private Runnable action;
	private boolean pressed;

	public TextElement(String text, TextDirection direction) {
		this.foreground = new ColorInterpolator();
		this.foreground.color = this.foreground.from = this.foreground.to = new Color(0xFFFFFF);
		this.background = new ColorInterpolator();
		this.background.color = this.background.from = this.background.to = new Color(0x333333);
		this.transition = new DoubleTransition();
		this.transition.setDuration(Settings.duration(250));
		this.transition.setFramerate(Properties.DISPLAY_FRAMERATE);
		this.transition.setValue(0.0d, 1.0d);
		this.transition.setInterpolator(Interpolator.SINE_EASE_IN_OUT);
		this.transition.setInvoker(value -> {
			this.foreground.interpolate(value);
			this.background.interpolate(value);
			this.update();
		});

		this.text = text;
		this.font = new Font("Itim", Font.BOLD, 14);
		this.direction = direction == null ? TextDirection.RIGHT : direction;
		this.updateText();
	}

	public TextElement(String text) {
		this(text, TextDirection.RIGHT);
	}

	public TextElement(TextDirection direction) {
		this(TextElement.DEFAULT_TEXT, direction);
	}

	public TextElement() {
		this(TextElement.DEFAULT_TEXT, TextDirection.RIGHT);
	}

	@Override
	public void mouseInteraction(Mouse mouse) {
		if(mouse.type == Mouse.MOUSE_PRESSED) {
			this.pressed = true;
			this.pressed();
		} else if(mouse.type == Mouse.MOUSE_RELEASED) {
			if(this.pressed) {
				if(this.action != null) {
					this.action.run();
				}

				this.pressed = false;
			}

			this.normal();
		} else if(mouse.type == Mouse.MOUSE_EXITED) {
			this.pressed = false;
			this.normal();
		}
	}

	private void normal() {
		this.foreground.from = this.foreground.color;
		this.foreground.to = new Color(0xFFFFFF);
		this.background.from = this.background.color;
		this.background.to = new Color(0x333333);
		this.transition.begin();
	}

	private void pressed() {
		this.foreground.from = this.foreground.color;
		this.foreground.to = new Color(0x000000);
		this.background.from = this.background.color;
		this.background.to = new Color(0xCCCCCC);
		this.transition.begin();
	}

	public Property<String, TextElement> text() {
		return this.textProperty;
	}

	public Property<TextDirection, TextElement> textDirection() {
		return this.textDirectionProperty;
	}

	public Property<Runnable, TextElement> action() {
		return this.actionProperty;
	}

	private void updateText() {
		FontMetrics metrics = GraphicsSystem.getFontMetrics(this.font);
		this.size = metrics.stringWidth(this.text) + metrics.getAscent() * 2;
	}

	@Override
	public void render(Area area) {
		area.color(this.background.color);
		area.mode(Mode.FILL);
		area.rect(0, 0, this.width, this.height);
		area.color(this.foreground.color);
		area.font(this.font);

		if(BarDirection.VERTICAL.equals(super.direction)) {
			area.rotate((TextDirection.RIGHT.equals(this.direction) ? -Math.PI : Math.PI) * 0.5d, ((double) this.width) * 0.5d, ((double) this.height) * 0.5d);
		}

		area.textCenter(this.text);
		//area.drawString(this.text, (int) Math.round((((double) this.width) - ((double) metrics.stringWidth(this.text))) / 2.0d), (int) Math.round(((((double) this.height) - ((double) metrics.getHeight())) / 2.0d) + ((double) metrics.getAscent())));
	}
}
