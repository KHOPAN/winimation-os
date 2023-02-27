package com.khopan.winimation.graphics.interact;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.khopan.core.property.Getter;
import com.khopan.winimation.graphics.Boundary;

public class Listener {
	private final Getter<Rectangle> bounds;
	private final Interactable interactable;
	private boolean entered;
	private boolean pressed;

	public Listener(Getter<Rectangle> bounds, Interactable interactable) {
		if(bounds == null) {
			throw new IllegalArgumentException("'bounds' cannot be null.");
		}

		if(interactable == null) {
			throw new IllegalArgumentException("'interactable' cannot be null.");
		}

		this.bounds = bounds;
		this.interactable = interactable;
	}

	public Listener(Boundary<?> boundary, Interactable interactable) {
		this(() -> boundary.getBounds(), interactable);
	}

	public boolean mouseInteraction(Mouse mouse) {
		Rectangle bounds = this.bounds.get();
		boolean entered = bounds.contains(mouse.location);
		boolean press = mouse.type == Mouse.MOUSE_PRESSED || mouse.type == Mouse.MOUSE_RELEASED || mouse.type == Mouse.MOUSE_CLICKED || mouse.type == Mouse.MOUSE_WHEEL_MOVED;

		if(this.entered != entered) {
			if(!press) {
				if(entered) {
					this.sendMouseInteraction(Mouse.MOUSE_ENTERED, mouse.event, bounds);
					this.entered = entered;
					return true;
				} else {
					this.sendMouseInteraction(Mouse.MOUSE_EXITED, mouse.event, bounds);
					this.pressed = false;
					this.entered = entered;
					return true;
				}
			} else {
				this.entered = entered;
			}
		} else {
			if(this.entered) {
				if(!press) {
					if(this.pressed) {
						this.sendMouseInteraction(Mouse.MOUSE_DRAGGED, mouse.event, bounds);
						this.entered = entered;
						return true;
					} else {
						this.sendMouseInteraction(Mouse.MOUSE_MOVED, mouse.event, bounds);
						this.entered = entered;
						return true;
					}
				} else {
					this.entered = entered;
				}
			}
		}

		this.entered = entered;

		if(mouse.type == Mouse.MOUSE_PRESSED || mouse.type == Mouse.MOUSE_RELEASED || mouse.type == Mouse.MOUSE_CLICKED || mouse.type == Mouse.MOUSE_WHEEL_MOVED) {
			if(this.entered) {
				if(mouse.type == Mouse.MOUSE_PRESSED) {
					this.pressed = true;
				} else if(mouse.type == Mouse.MOUSE_RELEASED) {
					this.pressed = false;
				}

				this.sendMouseInteraction(mouse.type, mouse.event, bounds);
				return true;
			}
		}

		this.entered = entered;
		return false;
	}

	private void sendMouseInteraction(int type, MouseEvent Event, Rectangle bounds) {
		this.interactable.mouseInteraction(new Mouse(type, Event, bounds.x, bounds.y));
	}

	public void keyboardInteraction(Keyboard keyboard) {
		this.interactable.keyboardInteraction(keyboard);
	}
}
