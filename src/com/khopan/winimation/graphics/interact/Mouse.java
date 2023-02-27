package com.khopan.winimation.graphics.interact;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Mouse {
	public static final int MOUSE_CLICKED = 0x00;
	public static final int MOUSE_PRESSED = 0x01;
	public static final int MOUSE_RELEASED = 0x02;
	public static final int MOUSE_ENTERED = 0x03;
	public static final int MOUSE_EXITED = 0x04;
	public static final int MOUSE_DRAGGED = 0x05;
	public static final int MOUSE_MOVED = 0x06;
	public static final int MOUSE_WHEEL_MOVED = 0x07;

	public final MouseEvent event;
	public final int type;
	public final int button;
	public final int clickCount;
	public final int id;
	public final Point locationOnScreen;
	public final int xOnScreen;
	public final int yOnscreen;
	public final int modifiers;
	public final Point location;
	public final int x;
	public final int y;
	public final Object source;
	public final long when;
	public final boolean altDown;
	public final boolean altGraphDown;
	public final boolean consumed;
	public final boolean controlDown;
	public final boolean metaDown;
	public final boolean popupTrigger;
	public final boolean shiftDown;
	public double preciseWheelRotation;
	public int scrollAmount;
	public int scrollType;
	public int unitsToScroll;
	public int wheelRotation;

	public Mouse(int type, MouseEvent Event) {
		this(type, Event, 0, 0);
	}

	Mouse(int type, MouseEvent Event, int x, int y) {
		this.event = Event;
		this.type = type;
		this.button = this.event.getButton();
		this.clickCount = this.event.getClickCount();
		this.id = this.event.getID();
		this.locationOnScreen = this.event.getLocationOnScreen();
		this.modifiers = this.event.getModifiersEx();
		Point location = this.event.getPoint();
		this.location = new Point(location.x - x, location.y - y);
		this.source = this.event.getSource();
		this.when = this.event.getWhen();
		this.x = this.event.getX() - x;
		this.xOnScreen = this.event.getXOnScreen();
		this.y = this.event.getY() - y;
		this.yOnscreen = this.event.getYOnScreen();
		this.altDown = this.event.isAltDown();
		this.altGraphDown = this.event.isAltGraphDown();
		this.consumed = this.event.isConsumed();
		this.controlDown = this.event.isControlDown();
		this.metaDown = this.event.isMetaDown();
		this.popupTrigger = this.event.isPopupTrigger();
		this.shiftDown = this.event.isShiftDown();

		if(this.event instanceof MouseWheelEvent wheelEvent) {
			this.preciseWheelRotation = wheelEvent.getPreciseWheelRotation();
			this.scrollAmount = wheelEvent.getScrollAmount();
			this.scrollType = wheelEvent.getScrollType();
			this.unitsToScroll = wheelEvent.getUnitsToScroll();
			this.wheelRotation = wheelEvent.getWheelRotation();
		}
	}

	public static MouseAdapter create(MouseInteractionCallback callback) {
		if(callback == null) {
			return null;
		}

		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_CLICKED, Event));
			}

			@Override
			public void mousePressed(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_PRESSED, Event));
			}

			@Override
			public void mouseReleased(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_RELEASED, Event));
			}

			@Override
			public void mouseEntered(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_ENTERED, Event));
			}

			@Override
			public void mouseExited(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_EXITED, Event));
			}

			@Override
			public void mouseDragged(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_DRAGGED, Event));
			}

			@Override
			public void mouseMoved(MouseEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_MOVED, Event));
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent Event) {
				callback.mouseInteraction(new Mouse(Mouse.MOUSE_WHEEL_MOVED, Event));
			}
		};
	}

	public static String typeName(int type) {
		return type == Mouse.MOUSE_CLICKED ? "MOUSE_CLICKED" : type == Mouse.MOUSE_PRESSED ? "MOUSE_PRESSED" : type == Mouse.MOUSE_RELEASED ? "MOUSE_RELEASED" : type == Mouse.MOUSE_ENTERED ? "MOUSE_ENTERED" : type == Mouse.MOUSE_EXITED ? "MOUSE_EXITED" : type == Mouse.MOUSE_DRAGGED ? "MOUSE_DRAGGED" : type == Mouse.MOUSE_MOVED ? "MOUSE_MOVED" : type == Mouse.MOUSE_WHEEL_MOVED ? "MOUSE_WHEEL_MOVED" : "";
	}

	public static String typeNameEasy(int type) {
		return type == Mouse.MOUSE_CLICKED ? "Mouse Clicked" : type == Mouse.MOUSE_PRESSED ? "Mouse Pressed" : type == Mouse.MOUSE_RELEASED ? "Mouse Released" : type == Mouse.MOUSE_ENTERED ? "Mouse Entered" : type == Mouse.MOUSE_EXITED ? "Mouse Exited" : type == Mouse.MOUSE_DRAGGED ? "Mouse Dragged" : type == Mouse.MOUSE_MOVED ? "Mouse Moved" : type == Mouse.MOUSE_WHEEL_MOVED ? "Mouse Wheel Moved" : "";
	}

	public static interface MouseInteractionCallback {
		public void mouseInteraction(Mouse mouse);
	}
}
