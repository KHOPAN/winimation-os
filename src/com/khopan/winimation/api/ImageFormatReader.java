package com.khopan.winimation.api;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageFormatReader implements FormatReader {
	@Override
	public Image read(FormatHelper helper) throws Throwable {
		Object value = helper.field.get(null);

		if(value != null) {
			String text = value.toString();
			File file;

			if(helper.isRelativePath) {
				file = new File(helper.declaringClass.getResource(text).toURI());
			} else {
				file = new File(text);
			}

			return ImageIO.read(file);
		}

		return null;
	}
}
