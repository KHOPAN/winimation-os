package com.khopan.winimation.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.PrintStream;
import java.util.Random;

public class ColorUtils {
	ColorUtils() {}

	public Color getBlackWithAlpha(Color Input1, Color Input2) {
		double ClosestValue = Double.MAX_VALUE;
		int ClosestAlpha = 0;

		for(int i = 1; i <= 255; i++) {
			Color Black = new Color(0, 0, 0, i);
			Color Color = this.blend(Input1, Black);

			if(this.isSameColor(Color, Input2)) {
				return Black;
			} else {
				double Average = ((Color.getRed() - Input2.getRed()) + (Color.getGreen() - Input2.getGreen()) + (Color.getBlue() - Input2.getBlue())) / 3.0d;

				if(Average < ClosestValue && Average >= 0.0d) {
					ClosestValue = (int) Math.round(Average);
					ClosestAlpha = i;
				}
			}
		}

		return new Color(0, 0, 0, ClosestAlpha);
	}

	public Color addBlackWithAlpha(Color Color, int AlphaBlack) {
		return this.blend(Color, new Color(0, 0, 0, AlphaBlack));
	}

	public Color blend(Color... Colors) {
		if(Colors == null || Colors.length == 0) {
			return null;
		}

		BufferedImage Image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D Graphics = Image.createGraphics();
		Graphics.setComposite(AlphaComposite.SrcOver);

		for(Color Color : Colors) {
			Graphics.setColor(Color);
			Graphics.fillRect(0, 0, 1, 1);
		}

		Graphics.dispose();
		int Color = Image.getRGB(0, 0);
		Image.flush();
		return new Color(
				(Color >> 16) & 0xFF,
				(Color >> 8) & 0xFF,
				Color & 0xFF,
				(Color >> 24) & 0xFF
				);
	}

	public boolean isSameColor(Color FirstColor, Color SecondColor) {
		return FirstColor.getRed() == SecondColor.getRed() && FirstColor.getGreen() == SecondColor.getGreen() && FirstColor.getBlue() == SecondColor.getBlue();
	}

	public boolean isSameColorWithAlpha(Color FirstColor, Color SecondColor) {
		return this.isSameColor(FirstColor, SecondColor) && FirstColor.getAlpha() == SecondColor.getAlpha();
	}

	public Color random() {
		return new Color(new Random(System.nanoTime()).nextInt(0xFFFFFF) + 1);
	}

	public Color foreground(Color Background, Color Bright, Color Dark) {
		double Brightness = (0.2126d * ((double) Background.getRed())) + (0.7152d * ((double) Background.getGreen())) + (0.0722d * ((double) Background.getBlue()));

		if(Brightness < 128.0d) {
			return Bright;
		} else {
			return Dark;
		}
	}

	public Color foreground(Color Background) {
		return this.foreground(Background, new Color(0xFFFFFF), new Color(0x000000));
	}

	public Color mix(Color... Colors) {
		if(Colors == null || Colors.length == 0) {
			return null;
		}

		long Red = 0L;
		long Green = 0L;
		long Blue = 0L;
		long Alpha = 0L;
		int Count = Colors.length;

		for(int i = 0; i < Count; i++) {
			Red += Colors[i].getRed();
			Green += Colors[i].getGreen();
			Blue += Colors[i].getBlue();
			Alpha += Colors[i].getAlpha();
		}

		return new Color(
				(int) Math.round((((double) Red) / ((double) Count))),
				(int) Math.round((((double) Green) / ((double) Count))),
				(int) Math.round((((double) Blue) / ((double) Count))),
				(int) Math.round((((double) Alpha) / ((double) Count)))
				);
	}

	public int compose(int Red, int Green, int Blue) {
		return (Red << 16) | (Green << 8) | Blue;
	}

	public long composeAlpha(int Red, int Green, int Blue, int Alpha) {
		return (Red << 16) | (Green << 8) | Blue | (Alpha << 24);
	}

	public int[] decompose(int RGB) {
		return new int[] {
				(RGB >> 16) & 0xFF,
				(RGB >> 8) & 0xFF,
				RGB & 0xFF
		};
	}

	public int[] decomposeAlpha(long RGBA) {
		return new int[] {
				(int) (RGBA >> 16) & 0xFF,
				(int) (RGBA >> 8) & 0xFF,
				(int) RGBA & 0xFF,
				(int) (RGBA >> 24) & 0xFF
		};
	}

	public Color getForegroundColor(Color Background) {
		float[] HSB = Color.RGBtoHSB(Background.getRed(), Background.getGreen(), Background.getBlue(), null);

		if(HSB[2] < 0.5f) {
			return Background.brighter().brighter();
		} else {
			return Background.darker().darker();
		}
	}

	public void printColor(PrintStream Stream, Color Color) {
		Stream.println("0x" + String.format("%02x%02x%02x%02x", Color.getAlpha(), Color.getRed(), Color.getGreen(), Color.getBlue()).toUpperCase());
	}
}
