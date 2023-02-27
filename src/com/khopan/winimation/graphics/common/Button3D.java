package com.khopan.winimation.graphics.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.khopan.core.property.Property;
import com.khopan.core.property.SimpleProperty;
import com.khopan.core.property.state.SimpleState;
import com.khopan.core.property.state.State;
import com.khopan.engine.animation.animation.data.DoubleTransition;
import com.khopan.engine.animation.interpolator.Interpolator;
import com.khopan.ui.guitar.GuitarLibrary;
import com.khopan.winimation.Settings;
import com.khopan.winimation.graphics.Boundary;
import com.khopan.winimation.graphics.Renderable;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.draw.Area.Mode;
import com.khopan.winimation.graphics.interact.Interactable;
import com.khopan.winimation.graphics.interact.Keyboard;
import com.khopan.winimation.graphics.interact.Mouse;
import com.khopan.winimation.utils.Utils;

public class Button3D extends Boundary<Button3D> implements Renderable, Interactable {
	private final Property<Font, Button3D> fontProperty = new SimpleProperty<Font, Button3D>(() -> this.font, font -> this.font = font, this).nullable().whenNull(GuitarLibrary.DEFAULT_FONT);
	private final Property<String, Button3D> textProperty = new SimpleProperty<String, Button3D>(() -> this.text, text -> this.text = text, this).nullable().whenNull("nlul");
	private final Property<Runnable, Button3D> onClickListenerProperty = new SimpleProperty<Runnable, Button3D>(() -> this.onClickListener, onClickListener -> this.onClickListener = onClickListener, this).nullable();
	private final Property<Integer, Button3D> levelsProperty = new SimpleProperty<Integer, Button3D>(() -> this.levels, levels -> {
		if(this.levels == levels) {
			return;
		}

		this.levels = levels;

		if(this.state == 0) {
			this.currentLevel = this.levels;
		} else if(this.state == 1) {
			this.currentLevel = (int) Math.round(((double) this.levels) * 0.675d);
		} else if(this.state == 2) {
			this.currentLevel = (int) Math.round(((double) this.levels) * 0.2d);
		} else if(this.state == 3) {
			this.currentLevel = (int) Math.round(((double) this.levels) * 0.81d);
		}

		this.update();
	}, this).nullable().whenNull(0);

	private final Property<Color, Button3D> foregroundProperty = new SimpleProperty<Color, Button3D>(() -> this.foreground, foreground -> {
		this.foreground = foreground;
		this.autoForeground = false;
	}, this).nullable().whenNull(new Color(0x000000));

	private final Property<Color, Button3D> backgroundProperty = new SimpleProperty<Color, Button3D>(() -> this.background, background -> {
		this.background = background;
		this.shadowBottom = Utils.color.addBlackWithAlpha(this.background, 95);
		this.shadowRight = Utils.color.addBlackWithAlpha(this.background, 143);
		this.intersectLine = Utils.color.addBlackWithAlpha(this.background, 125);

		if(this.autoForeground) {
			this.foreground = Utils.color.getForegroundColor(this.background);
		}
	}, this).nullable().whenNull(new Color(0x000000));

	private final State<Button3D> autoForegroundProperty = new SimpleState<Button3D>(() -> this.autoForeground, autoForeground -> {
		if(this.autoForeground == autoForeground) {
			return;
		}

		this.autoForeground = autoForeground;
		this.foreground = Utils.color.getForegroundColor(this.background);
	}, this);

	private final Property<KeyStroke[], Button3D> keyBindingProperty = new SimpleProperty<KeyStroke[], Button3D>(() -> this.keyBinding, keyBinding -> this.keyBinding = keyBinding, this).nullable();

	private final DoubleTransition transition;
	private int levels;
	private Color foreground;
	private Color background;
	private Color shadowBottom;
	private Color shadowRight;
	private Color intersectLine;
	private Font font;
	private String text;
	private KeyStroke[] keyBinding;
	private Runnable onClickListener;
	private int currentLevel;
	private int state;
	private boolean isEntered;
	private boolean autoForeground;

	public Button3D() {
		this.foreground().set(new Color(0x000000));
		this.background().set(new Color(0xAAAAAA));
		this.font = Settings.font(1.1246d);
		this.text = "Button";
		this.transition = new DoubleTransition();
		this.transition.setDuration(200);
		this.transition.setFramerate(240);
		this.transition.setInterpolator(Interpolator.SINE_EASE_IN_OUT);
		this.transition.setInvoker(Value -> {
			this.currentLevel = (int) Math.round(Value);
			this.update();
		});
	}

	public Property<Font, Button3D> font() {
		return this.fontProperty;
	}

	public Property<String, Button3D> text() {
		return this.textProperty;
	}

	public Property<KeyStroke[], Button3D> keyBinding() {
		return this.keyBindingProperty;
	}

	public Property<Runnable, Button3D> onClickListener() {
		return this.onClickListenerProperty;
	}

	public Property<Integer, Button3D> levels() {
		return this.levelsProperty;
	}

	public Property<Color, Button3D> foreground() {
		return this.foregroundProperty;
	}

	public Property<Color, Button3D> background() {
		return this.backgroundProperty;
	}

	public State<Button3D> autoForeground() {
		return this.autoForegroundProperty;
	}

	@Override
	public void render(Area area) {
		Area renderer =  area.create();
		renderer.normal();
		renderer.translate(this.bounds.x, this.bounds.y);
		renderer.clip(0, 0, this.bounds.width, this.bounds.height);
		renderer.mode(Mode.FILL);
		int width = this.bounds.width - this.levels;
		int height = this.bounds.height - this.levels;
		int level = this.levels - this.currentLevel;

		for(int i = this.levels; i >= level; i--) {
			renderer.color(this.shadowBottom);
			int y = height + i;
			renderer.line(i + 1, y, i + width - 1, y);
			renderer.color(this.shadowRight);
			int x = width + i;
			renderer.line(x, i + 1, x, i + height - 1);
			renderer.color(this.intersectLine);
			renderer.line(x, y, x, y);
		}

		renderer.color(this.background);
		renderer.rect(level, level, width, height);
		renderer.smooth();
		renderer.color(this.foreground);
		renderer.font(this.font);
		FontMetrics metrics = renderer.metrics();
		renderer.text(this.text, level + (width - metrics.stringWidth(this.text)) / 2, level + (height - metrics.getHeight()) / 2 + metrics.getAscent());
	}

	@Override
	public void mouseInteraction(Mouse mouse) {
		if(mouse.type == Mouse.MOUSE_MOVED) {
			if(this.bounds.contains(mouse.location)) {
				if(!this.isEntered) {
					this.isEntered = true;
					this.entered();
				}
			} else {
				if(this.isEntered) {
					this.isEntered = false;
					this.exit();
				}
			}
		} else if(mouse.type == Mouse.MOUSE_PRESSED) {
			if(this.bounds.contains(mouse.location) && this.isEntered) {
				this.press();
			}
		} else if(mouse.type == Mouse.MOUSE_RELEASED) {
			if(this.bounds.contains(mouse.location)) {
				if(this.isEntered && this.onClickListener != null) {
					this.onClickListener.run();
				}

				this.release();
			}			
		}
	}

	@Override
	public void keyboardInteraction(Keyboard keyboard) {
		if(this.keyBinding != null) {
			if(keyboard.type == Keyboard.KEYBOARD_PRESSED) {
				for(KeyStroke binding : this.keyBinding) {
					if(keyboard.keyCode == binding.getKeyCode() && KeyEvent.getModifiersExText(keyboard.modifiers).equals(KeyEvent.getModifiersExText(binding.getModifiers()))) {
						if(this.onClickListener != null) {
							this.onClickListener.run();
						}

						this.press();
					}
				}
			} else if(keyboard.type == Keyboard.KEYBOARD_RELEASED) {
				for(KeyStroke binding : this.keyBinding) {
					if(keyboard.keyCode == binding.getKeyCode()) {
						this.exit();
					}
				}
			}
		}
	}

	private void entered() {
		this.state = 1;
		this.transition.setValue((double) this.currentLevel, ((double) this.levels) * 0.675d);
		this.transition.begin();
	}

	private void exit() {
		this.state = 0;
		this.transition.setValue((double) this.currentLevel, (double) this.levels);
		this.transition.begin();
	}

	private void press() {
		this.state = 2;
		this.transition.setValue((double) this.currentLevel, ((double) this.levels) * 0.2d);
		this.transition.begin();
	}

	private void release() {
		this.state = 3;
		this.transition.setValue((double) this.currentLevel, ((double) this.levels) * 0.81d);
		this.transition.begin();
	}
}
