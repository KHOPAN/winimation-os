package com.khopan.winimation.utils;

import java.io.IOException;
import java.io.InputStream;

public class TextInputStream extends InputStream {
	private final String text;
	private final int textLength;
	private int i;

	public TextInputStream(String text) {
		this.text = text;
		this.textLength = this.text.length();
	}

	@Override
	public int read() throws IOException {
		if(this.i >= this.textLength) {
			return -1;
		} else {
			int value = this.text.charAt(this.i);
			this.i += 1;
			return value;
		}
	}
}
