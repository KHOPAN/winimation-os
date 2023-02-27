package com.khopan.winimation.api;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import com.khopan.winimation.api.annotation.Color;
import com.khopan.winimation.api.annotation.Path;
import com.khopan.winimation.api.annotation.Raw;
import com.khopan.winimation.api.annotation.Viewbox;
import com.khopan.winimation.utils.Utils;

public class SVGFormatReader implements FormatReader {
	@Override
	public Image read(FormatHelper helper) throws Throwable {
		boolean raw = helper.field.isAnnotationPresent(Raw.class);
		Object value = helper.field.get(null);

		if(value != null) {
			String text = value.toString();

			if(raw) {
				if(helper.field.isAnnotationPresent(Path.class)) {
					Viewbox viewbox = helper.field.getAnnotation(Viewbox.class);
					int x;
					int y;
					int width;
					int height;

					if(viewbox == null) {
						x = 0;
						y = 0;
						width = 0;
						height = 0;
					} else {
						int viewboxValue = viewbox.value();

						if(viewboxValue != -1) {
							x = 0;
							y = 0;
							width = viewboxValue;
							height = viewboxValue;
						} else {
							x = viewbox.x();
							y = viewbox.y();
							width = viewbox.width();
							height = viewbox.height();
						}
					}

					Color colorAnnotation = helper.field.getAnnotation(Color.class);
					int color;

					if(colorAnnotation != null) {
						color = colorAnnotation.value();
					} else {
						color = ColorList.BLACK;
					}

					text = "<?xml version=\"1.0\" encoding=\"utf-8\"?><svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"#" + String.format("%02x%02x%02x", (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF) + "\" viewBox=\"" + x + " " + y + " " + width + " " + height + "\"><path d=\"" + text + "\"/></svg>";
				}

				int width = (int) Math.round(((double) helper.size) * helper.scalePercentage);
				int height = (int) Math.round(((double) helper.size) * helper.scalePercentage);
				Image image = Utils.image.readSVG(text, width, height);
				BufferedImage result = new BufferedImage(helper.size, helper.size, BufferedImage.TYPE_INT_ARGB);
				Graphics2D Graphics = result.createGraphics();
				Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Graphics.drawImage(image, (int) Math.round((((double) helper.size) - ((double) width)) * 0.5d), (int) Math.round((((double) helper.size) - ((double) width)) * 0.5d), null);
				Graphics.dispose();
				return result;
			} else {
				File file;

				if(helper.isRelativePath) {
					file = new File(helper.declaringClass.getResource(text).toURI());
				} else {
					file = new File(text);
				}

				int width = (int) Math.round(((double) helper.size) * helper.scalePercentage);
				int height = (int) Math.round(((double) helper.size) * helper.scalePercentage);
				Image image = Utils.image.readSVG(file, width, height);
				BufferedImage result = new BufferedImage(helper.size, helper.size, BufferedImage.TYPE_INT_ARGB);
				Graphics2D Graphics = result.createGraphics();
				Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				Graphics.drawImage(image, (int) Math.round((((double) helper.size) - ((double) width)) * 0.5d), (int) Math.round((((double) helper.size) - ((double) width)) * 0.5d), null);
				Graphics.dispose();
				return result;
			}
		} else {
			return null;
		}
	}
}
