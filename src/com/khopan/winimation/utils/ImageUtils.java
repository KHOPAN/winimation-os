package com.khopan.winimation.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import com.khopan.winimation.graphics.shape.Squircle;

public class ImageUtils {
	ImageUtils() {}

	public BufferedImage scaleNoBlank(Image image, int targetWidth, int targetHeight) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		double ratio = (double) width / (double) height;
		double targetRatio = (double) targetWidth / (double) targetHeight;

		int resultWidth;
		int resultHeight;

		if(ratio > targetRatio) {
			resultHeight = targetHeight;
			resultWidth = (int) Math.round(((int) resultHeight) * ratio);
		} else if(ratio < targetRatio) {
			resultWidth = targetWidth;
			resultHeight = (int) Math.round(((int) resultWidth) / ratio);
		} else {
			resultWidth = targetWidth;
			resultHeight = targetHeight;
		}

		BufferedImage resultImage = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D Graphics = resultImage.createGraphics();
		Graphics.drawImage(image.getScaledInstance(resultWidth, resultHeight, BufferedImage.SCALE_SMOOTH), (targetWidth - resultWidth) / 2, (targetHeight - resultHeight) / 2, null);
		Graphics.dispose();

		return resultImage;
	}

	public BufferedImage scaleNoBlank(Image image, Dimension size) {
		return this.scaleNoBlank(image, size.width, size.height);
	}

	public BufferedImage scale(Image image, int targetWidth, int targetHeight) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		double ratio = (double) width / (double) height;
		double targetRatio = (double) targetWidth / (double) targetHeight;

		int resultWidth;
		int resultHeight;

		if(ratio == targetRatio) {
			resultWidth = targetWidth;
			resultHeight = targetHeight;
		} else if (targetRatio > ratio) {
			resultWidth = (int) Math.round(((double) targetHeight) * ratio);
			resultHeight = targetHeight;
		} else {
			resultWidth = targetWidth;
			resultHeight = (int) Math.round(((double) targetWidth) / ratio);
		}

		BufferedImage resultImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D Graphics = resultImage.createGraphics();
		Graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		Graphics.drawImage(image.getScaledInstance(resultWidth, resultHeight, BufferedImage.SCALE_SMOOTH), (targetWidth - resultWidth) / 2, (targetHeight - resultHeight) / 2, null);
		Graphics.dispose();

		return resultImage;
	}

	public BufferedImage scale(Image image, Dimension size) {
		return this.scale(image, size.width, size.height);
	}

	public BufferedImage transformImage(BufferedImage fromImage, BufferedImage toImage, double time) {
		int width = fromImage.getWidth(null);
		int height = fromImage.getHeight(null);

		if(toImage.getWidth() != width || toImage.getHeight() != height) {
			return fromImage;
		}

		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] fromPixels = fromImage.getRGB(0, 0, width, height, null, 0, width);
		int[] toPixels = toImage.getRGB(0, 0, width, height, null, 0, toImage.getWidth());

		for(int i = 0; i < fromPixels.length; i++) {
			int alpha = (fromPixels[i] >> 24) & 0xFF;
			int red = (fromPixels[i] >> 16) & 0xFF;
			int green = (fromPixels[i] >> 8) & 0xFF;
			int blue = fromPixels[i] & 0xFF;
			result.setRGB(i % width, i / width, (((int) Math.round(alpha + (((toPixels[i] >> 24) & 0xFF) - alpha) * time)) << 24) | (((int) Math.round(red + (((toPixels[i] >> 16) & 0xFF) - red) * time)) << 16) | ((int) Math.round(green + (((toPixels[i] >> 8) & 0xFF) - green) * time) << 8) | (int) Math.round(blue + ((toPixels[i] & 0xFF) - blue) * time));
		}

		return result;
	}

	public BufferedImage buffered(Image image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D Graphics = result.createGraphics();
		Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics.drawImage(image, 0, 0, null);
		Graphics.dispose();

		return result;
	}

	public BufferedImage makeRound(Image image, double arcWidth, double arcHeight) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D Graphics = result.createGraphics();
		Graphics.setComposite(AlphaComposite.Src);
		Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics.setColor(new Color(0xFFFFFF));
		Graphics.fill(new RoundRectangle2D.Double(0, 0, width, height, arcWidth, arcHeight));
		Graphics.setComposite(AlphaComposite.SrcAtop);
		Graphics.drawImage(image, 0, 0, null);
		Graphics.dispose();

		return result;
	}

	public BufferedImage makeRoundSquircle(Image image, double size) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D Graphics = result.createGraphics();
		Graphics.setComposite(AlphaComposite.Src);
		Graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics.setColor(new Color(0xFFFFFF));
		Graphics.fill(new Squircle(size));
		Graphics.setComposite(AlphaComposite.SrcAtop);
		Graphics.drawImage(image, 0, 0, null);
		Graphics.dispose();

		return result;
	}

	public BufferedImage blurImage(Image image, int blurRadius) {
		float[] kernelData = new float[blurRadius * blurRadius];
		Arrays.fill(kernelData, 1.0f / (float) (blurRadius * blurRadius));
		return new ConvolveOp(new Kernel(blurRadius, blurRadius, kernelData), ConvolveOp.EDGE_NO_OP, null).filter(this.buffered(image), null);
	}

	public BufferedImage fastBlur(Image image, int blurRadius) {
		return this.buffered(image.getScaledInstance(image.getWidth(null) / blurRadius, image.getHeight(null) / blurRadius, BufferedImage.SCALE_SMOOTH).getScaledInstance(image.getWidth(null), image.getHeight(null), BufferedImage.SCALE_FAST));
	}

	public Color getAverageColor(Image image) {
		BufferedImage buffered = this.buffered(image);
		int width = buffered.getWidth();
		int height = buffered.getHeight();
		double factor = ((double) width) * ((double) height);
		long red = 0L;
		long green = 0L;
		long blue = 0L;

		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				int color = buffered.getRGB(x, y);
				red += (color >> 16) & 0xFF;
				green += (color >> 8) & 0xFF;
				blue += color & 0xFF;
			}
		}

		return new Color(
				(int) Math.round(((double) red) / factor),
				(int) Math.round(((double) green) / factor),
				(int) Math.round(((double) blue) / factor)
				);
	}

	public BufferedImage readSVG(InputStream input, int width, int height) {
		int imageWidth = width;
		int imageHeight = height;

		return new ImageTranscoder() {
			private BufferedImage image;

			{
				try {
					this.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) imageWidth);
					this.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) imageHeight);
					this.transcode(new TranscoderInput(input), null);
				} catch(Throwable Errors) {
					Errors.printStackTrace();
				}
			}

			@Override
			public BufferedImage createImage(int width, int height) {
				return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}

			@Override
			public void writeImage(BufferedImage image, TranscoderOutput output) throws TranscoderException {
				this.image = image;
			}
		}.image;
	}

	public BufferedImage readSVG(File file, int width, int height) {
		try {
			return this.readSVG(new FileInputStream(file), width, height);
		} catch(Throwable Errors) {
			throw new RuntimeException(Errors);
		}
	}

	public BufferedImage readSVG(String svg, int width, int height) {
		try {
			return this.readSVG(new TextInputStream(svg), width, height);
		} catch(Throwable Errors) {
			throw new RuntimeException(Errors);
		}
	}
}
