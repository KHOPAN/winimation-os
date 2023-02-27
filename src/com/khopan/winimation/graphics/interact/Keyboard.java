package com.khopan.winimation.graphics.interact;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard {
	public static final int KEYBOARD_TYPED = 0x08;
	public static final int KEYBOARD_PRESSED = 0x09;
	public static final int KEYBOARD_RELEASED = 0x0A;

	public final KeyEvent event;
	public final int type;
	public final int extendedKeyCode;
	public final int id;
	public final char keyChar;
	public final int keyCode;
	public final int keyLocation;
	public final int modifiers;
	public final Object source;
	public final long when;
	public final boolean actionKey;
	public final boolean altDown;
	public final boolean altGraphDown;
	public final boolean consumed;
	public final boolean controlDown;
	public final boolean metaDown;
	public final boolean shiftDown;

	public Keyboard(int type, KeyEvent Event) {
		this.event = Event;
		this.type = type;
		this.extendedKeyCode = this.event.getExtendedKeyCode();
		this.id = this.event.getID();
		this.keyChar = this.event.getKeyChar();
		this.keyCode = this.event.getKeyCode();
		this.keyLocation = this.event.getKeyLocation();
		this.modifiers = this.event.getModifiersEx();
		this.source = this.event.getSource();
		this.when = this.event.getWhen();
		this.actionKey = this.event.isActionKey();
		this.altDown = this.event.isAltDown();
		this.altGraphDown = this.event.isAltGraphDown();
		this.consumed = this.event.isConsumed();
		this.controlDown = this.event.isControlDown();
		this.metaDown = this.event.isMetaDown();
		this.shiftDown = this.event.isShiftDown();
	}

	public static KeyAdapter create(KeyboardInteractionCallback callback) {
		if(callback == null) {
			return null;
		}

		return new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent Event) {
				callback.keyboardInteraction(new Keyboard(Keyboard.KEYBOARD_TYPED, Event));
			}

			@Override
			public void keyPressed(KeyEvent Event) {
				callback.keyboardInteraction(new Keyboard(Keyboard.KEYBOARD_PRESSED, Event));
			}

			@Override
			public void keyReleased(KeyEvent Event) {
				callback.keyboardInteraction(new Keyboard(Keyboard.KEYBOARD_RELEASED, Event));
			}
		};
	}

	public static String typeName(int type) {
		return type == Keyboard.KEYBOARD_TYPED ? "KEYBOARD_TYPED" : type == Keyboard.KEYBOARD_PRESSED ? "KEYBOARD_PRESSED" : type == Keyboard.KEYBOARD_RELEASED ? "KEYBOARD_RELEASED" : "";
	}

	public static String typeNameEasy(int type) {
		return type == Keyboard.KEYBOARD_TYPED ? "Keyboard Typed" : type == Keyboard.KEYBOARD_PRESSED ? "Keyboard Pressed" : type == Keyboard.KEYBOARD_RELEASED ? "Keyboard Released" : "";
	}

	public static interface KeyboardInteractionCallback {
		public void keyboardInteraction(Keyboard keyboard);
	}
}
