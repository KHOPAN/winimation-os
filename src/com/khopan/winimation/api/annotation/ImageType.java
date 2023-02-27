package com.khopan.winimation.api.annotation;

public enum ImageType {
	PNG("png", false),
	JPG("jpg", false),
	ICO("ico", false),
	SVG(null, true),
	IMAGE(null, true),
	BUFFERED_IMAGE(null, true);

	private final String name;
	private final boolean specialFormat;

	private ImageType(String name, boolean specialFormat) {
		this.name = name;
		this.specialFormat = specialFormat;
	}

	public String getName() {
		return this.name;
	}
}
