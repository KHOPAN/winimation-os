package com.khopan.winimation.api;

public class Resolution {
	public static final Resolution MAXIMUM = new Resolution();

	public int width;
	public int height;
	private boolean maxmimum;

	public Resolution(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private Resolution() {
		this.maxmimum = true;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public boolean isMaximum() {
		return this.maxmimum;
	}
}
