package com.khopan.winimation.graphics.popup.dialog;

import com.khopan.core.property.Property;
import com.khopan.core.property.SimpleProperty;
import com.khopan.winimation.graphics.Popup;

public class DialogPopup extends Popup {
	private final Property<DialogHeader, DialogPopup> headerProperty = new SimpleProperty<DialogHeader, DialogPopup>(() -> this.header, header -> this.header = header, this).nullable();
	private final Property<Integer, DialogPopup> spacingProperty = new SimpleProperty<Integer, DialogPopup>(() -> this.spacing, spacing -> {
		if(spacing < 0) {
			throw new IllegalArgumentException("'spacing' cannot be negative.");
		}

		this.spacing = spacing;
	}, this).nullable().whenNull(0);

	private DialogHeader header;
	private int spacing;

	public DialogPopup() {

	}

	public Property<DialogHeader, DialogPopup> header() {
		return this.headerProperty;
	}

	public Property<Integer, DialogPopup> spacing() {
		return this.spacingProperty;
	}
}
