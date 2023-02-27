package com.khopan.winimation;

public class AspectRatio {
	public final double widthRatio;
	public final double heightRatio;

	public AspectRatio(double widthRatio, double heightRatio) {
		this.widthRatio = widthRatio;
		this.heightRatio = heightRatio;
	}

	public static AspectRatio of(double widthRatio, double heightRatio) {
		return new AspectRatio(widthRatio, heightRatio);
	}
}
