package com.khopan.winimation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.khopan.engine.animation.animation.data.DoubleTransition;
import com.khopan.engine.animation.interpolator.Interpolator;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.draw.Area.Mode;
import com.khopan.winimation.utils.Utils;

public class Wallpaper {
	private Wallpaper() {} 

	private static final List<Runnable> Hook = new ArrayList<>();
	private static boolean Initialized;
	private static BufferedImage WallpaperImage;
	private static float Transparent;
	private static boolean Finished;

	public static void load() {
		if(!Wallpaper.Initialized) {
			Thread thread = new Thread(() -> {
				try {
					Wallpaper.WallpaperImage = Utils.image.blurImage(Utils.image.scaleNoBlank(ImageIO.read(/*new File("C:\\Users\\puthi\\Downloads\\math.png")*/Wallpaper.class.getResource("windows11.png")), Properties.SCREEN_DIMENSION), 10);
					DoubleTransition transition = new DoubleTransition();
					transition.setDuration(Settings.duration(750));
					transition.setValue(0.0d, 1.0d);
					transition.setInterpolator(Interpolator.SINE_EASE_IN_OUT);
					transition.setInvoker(value -> {
						Wallpaper.Transparent = (float) (double) value;

						if(value == 1.0d) {																																																																																												
							Wallpaper.Finished = true;

							for(int i = 0; i < Wallpaper.Hook.size(); i++) {
								Wallpaper.Hook.get(i).run();
							}
						}

						Window.Instance.frame.repaint();
					});

					transition.begin();
				} catch(Throwable Errors) {
					Errors.printStackTrace();
				}
			});

			thread.setName("Wallpaper Loading Thread");
			thread.setPriority(Thread.NORM_PRIORITY);
			thread.start();
			Wallpaper.Initialized = true;
		}
	}

	public static void loadingHook(Runnable onLoad) {
		Wallpaper.Hook.add(onLoad);
	}

	public static boolean isLoading() {
		return Wallpaper.WallpaperImage == null;
	}

	public static void draw(Area area) {
		Area wallpaper = area.create();

		if(!Wallpaper.Finished) {
			wallpaper.color(0x000000);
			wallpaper.mode(Mode.FILL);
			wallpaper.rect(0, 0, Properties.SCREEN_DIMENSION);
			wallpaper.color(0xFFFFFF);
			wallpaper.font(Settings.font(0.102367242d));
			wallpaper.textCenter("Loading Wallpaper...");
		}

		if(Wallpaper.WallpaperImage != null) {
			wallpaper.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Wallpaper.Transparent));
			wallpaper.image(Wallpaper.WallpaperImage);
			Color color = wallpaper.color();
			wallpaper.color(0, 0, 0, 127);
			wallpaper.mode(Mode.FILL);
			wallpaper.rect(0, 0, Properties.SCREEN_DIMENSION);
			wallpaper.color(color);
		}

		wallpaper.done();
	}

	/*public static void draw(Graphics Graphics) {
		Graphics wallpaper = Graphics.create();

		if(!Wallpaper.Finished) {
			wallpaper.setColor(new Color(0x000000));
			wallpaper.fillRect(0, 0, Properties.SCREEN_DIMENSION.width, Properties.SCREEN_DIMENSION.height);
			wallpaper.setColor(new Color(0xFFFFFF));
			wallpaper.setFont(Settings.font(0.102367242d));
			FontMetrics metrics = wallpaper.getFontMetrics();
			String text = "Loading Wallpaper...";
			wallpaper.drawString(text, (Properties.SCREEN_DIMENSION.width - metrics.stringWidth(text)) / 2, (Properties.SCREEN_DIMENSION.height - metrics.getHeight()) / 2 + metrics.getAscent());
		}

		if(Wallpaper.WallpaperImage != null) {
			((Graphics2D) wallpaper).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Wallpaper.Transparent));
			wallpaper.drawImage(Wallpaper.WallpaperImage, 0, 0, null);
			Color color = wallpaper.getColor();
			wallpaper.setColor(new Color(0, 0, 0, 127));
			wallpaper.fillRect(0, 0, Properties.SCREEN_DIMENSION.width, Properties.SCREEN_DIMENSION.height);
			wallpaper.setColor(color);
		}

		wallpaper.dispose();
	}*/
}
