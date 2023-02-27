package com.khopan.winimation.graphics.interact;

import com.khopan.winimation.graphics.interact.Keyboard.KeyboardInteractionCallback;
import com.khopan.winimation.graphics.interact.Mouse.MouseInteractionCallback;

public interface Interactable extends MouseInteractionCallback, KeyboardInteractionCallback {
	@Override
	public default void mouseInteraction(Mouse mouse) {

	}

	@Override
	public default void keyboardInteraction(Keyboard keyboard) {

	}
}
