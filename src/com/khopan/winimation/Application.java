package com.khopan.winimation;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.khopan.winimation.api.ColorList;
import com.khopan.winimation.api.FormatHelper;
import com.khopan.winimation.api.FormatReader;
import com.khopan.winimation.api.ImageFormatReader;
import com.khopan.winimation.api.annotation.Developer;
import com.khopan.winimation.api.annotation.Format;
import com.khopan.winimation.api.annotation.Icon;
import com.khopan.winimation.api.annotation.IconBackground;
import com.khopan.winimation.api.annotation.IconBackgroundType;
import com.khopan.winimation.api.annotation.Manifest;
import com.khopan.winimation.api.annotation.Name;
import com.khopan.winimation.api.annotation.Version;
import com.khopan.winimation.graphics.Display;
import com.khopan.winimation.utils.Utils;

public abstract class Application {
	static final List<Application> APPLICATION_LIST = new ArrayList<>();

	String name;
	String version;
	String[] developer;
	Image icon;
	Color color;
	Display display;

	public Application() {
		Class<?> applicationClass = this.getClass();
		Field[] fields = applicationClass.getDeclaredFields();
		Image foreground = null;
		Image background = null;
		boolean hasManifest = false;
		int size = 65;

		for(Field field : fields) {
			if(field.isAnnotationPresent(Manifest.class)) {
				try {
					Object value = field.get(null);

					if(value instanceof String path) {
						JsonNode node = new ObjectMapper().readTree(new File(applicationClass.getResource(path).toURI()));

						if(node instanceof ObjectNode objectNode) {
							if(objectNode.has("name")) {
								this.name = objectNode.get("name").toString();
							}

							if(objectNode.has("version")) {
								this.version = objectNode.get("version").toString();
							}

							if(objectNode.has("developer")) {
								JsonNode developerNode = objectNode.get("developer");

								if(developerNode instanceof ArrayNode array) {
									Iterator<JsonNode> nodes = array.elements();
									List<String> developers = new ArrayList<>();

									while(nodes.hasNext()) {
										developers.add(nodes.next().toString());
									}

									this.developer = developers.toArray(new String[developers.size()]);
								} else {
									this.developer = new String[] {developerNode.toString()};
								}
							}
						}
					}

					hasManifest = true;
				} catch(Throwable Errors) {
					Errors.printStackTrace();
				}
			}
		}

		for(Field field : fields) {
			try {
				if(!hasManifest) {
					if(field.isAnnotationPresent(Name.class)) {
						Object value = field.get(null);

						if(value instanceof String name) {
							this.name = name;
						}
					}

					if(field.isAnnotationPresent(Version.class)) {
						Object value = field.get(null);

						if(value instanceof String version) {
							this.version = version;
						}
					}

					if(field.isAnnotationPresent(Developer.class)) {
						Object value = field.get(null);

						if(value instanceof String developer) {
							this.developer = new String[] {developer};
						} else if(value instanceof String[] developers) {
							this.developer = developers;
						}
					}
				}
			} catch(Throwable Errors) {
				Errors.printStackTrace();
			}

			if(field.isAnnotationPresent(Icon.class)) {
				Format formatAnnotation = field.getAnnotation(Format.class);
				Class<? extends FormatReader> format;

				if(formatAnnotation != null) {
					format = formatAnnotation.value();
				} else {
					format = ImageFormatReader.class;
				}

				try {
					//this.icon = Utils.image.makeRoundSquircle(format.getConstructor().newInstance().read(new FormatHelper(new Dimension(65, 65), applicationClass, field)), 65.0d);
					foreground = format.getConstructor().newInstance().read(new FormatHelper(size, applicationClass, field));
				} catch(Throwable Errors) {
					continue;
				}
			}

			IconBackground iconBackground = field.getAnnotation(IconBackground.class);

			if(iconBackground != null) {
				IconBackgroundType type = iconBackground.value();

				if(IconBackgroundType.COLOR.equals(type)) {
					int color = ColorList.BLACK;

					try {
						Object value = field.get(null);

						if(value instanceof Integer colorInt) {
							color = colorInt;
						} else if(value instanceof String colorText) {
							int length = colorText.length();
							boolean nameWay = false;

							if(length == 6 || length == 8) {
								String parseText = colorText;

								if(length == 8) {
									parseText = colorText.substring(2);
								}

								try {
									color = Integer.parseInt(parseText, 16);
								} catch(Throwable Errors) {
									nameWay = true;
								}
							} else {
								nameWay = true;
							}

							if(nameWay) {
								for(Field colorField : ColorList.class.getDeclaredFields()) {
									if(colorField.getName().toLowerCase().equals(colorText.toLowerCase())) {
										color = (int) colorField.get(null);
									}
								}
							}
						}
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}

					BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
					Graphics2D Graphics = image.createGraphics();
					Graphics.setColor(new Color(color));
					Graphics.fillRect(0, 0, size, size);
					Graphics.dispose();
					background = image;
				} else if(IconBackgroundType.GRADIENT.equals(type)) {
					try {
						Object value = field.get(null);

						if(value instanceof String code) {
							if(code.length() == 16) {
								int beginX = 0;
								int beginY = 0;
								Color beginColor = new Color(Integer.parseInt(code.substring(2, 8), 16));
								String beginCode = code.substring(0, 2).toLowerCase();

								if(beginCode.equals("tl")) {
									beginX = 0;
									beginY = 0;
								} else if(beginCode.equals("tr")) {
									beginX = size;
									beginY = 0;
								} else if(beginCode.equals("bl")) {
									beginX = 0;
									beginY = size;
								} else if(beginCode.equals("br")) {
									beginX = size;
									beginY = size;
								}

								int endX = 0;
								int endY = 0;
								Color endColor = new Color(Integer.parseInt(code.substring(10), 16));
								String endCode = code.substring(8, 10).toLowerCase();

								if(endCode.equals("tl")) {
									endX = 0;
									endY = 0;
								} else if(endCode.equals("tr")) {
									endX = size;
									endY = 0;
								} else if(endCode.equals("bl")) {
									endX = 0;
									endY = size;
								} else if(endCode.equals("br")) {
									endX = size;
									endY = size;
								}

								BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
								Graphics2D Graphics = image.createGraphics();
								Graphics.setPaint(new GradientPaint(beginX, beginY, beginColor, endX, endY, endColor));
								Graphics.fillRect(0, 0, size, size);
								Graphics.dispose();
								background = image;
							} else {
								System.err.println("Gradient color must have the length of exactly 16.");
							}

							//try {
							/*//Color topLeft = null;
								//Color topRight = null;
								//Color bottomLeft = null;
								//Color bottomRight = null;
								Point firstLocation = null;
								Point secondLocation = null;
								Color first = null;
								Color second = null;
								int previous = 0;

								for(int i = 8; i <= code.length(); i += 8) {
									String raw = code.substring(previous, i);
									String section = raw.toLowerCase();

									if(section.startsWith("tl")) {
										if(first != null) {
											topLeft = new Color(Integer.parseInt(section.substring(2), 16));
										} else {
											System.err.println("Warning: Gradient color conflict, will use the first color.");
										}
									} else if(section.startsWith("tr")) {
										if(topRight != null) {
											topRight = new Color(Integer.parseInt(section.substring(2), 16));
										} else {
											System.err.println("Warning: Gradient color conflict, will use the first color.");
										}
									} else if(section.startsWith("bl")) {
										if(bottomLeft != null) {
											bottomLeft = new Color(Integer.parseInt(section.substring(2), 16));
										} else {
											System.err.println("Warning: Gradient color conflict, will use the first color.");
										}
									} else if(section.startsWith("br")) {
										if(bottomRight != null) {
											bottomRight = new Color(Integer.parseInt(section.substring(2), 16));
										} else {
											System.err.println("Warning: Gradient color conflict, will use the first color.");
										}
									} else {
										System.err.println("Invalid gradient code: '" + raw + "'");
									}

									previous = i;
								}

								background = GradientParser.parse(size, size, topLeft, topRight, bottomLeft, bottomRight);*/
							//} catch(Throwable Errors) {

							//}
						}
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}
				}
			}
		}

		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D Graphics = image.createGraphics();
		Graphics.drawImage(background, 0, 0, null);
		Graphics.drawImage(foreground, 0, 0, null);
		Graphics.dispose();
		this.icon = Utils.image.makeRoundSquircle(image, 65.0d);
		this.color = Utils.image.getAverageColor(this.icon);
		this.display = this.getDisplay();

		/*IconManager manager = this.getIcon();
		Image iconImage = null;

		if(manager != null) {
			if(manager.isVectorGraphics()) {
				iconImage = Utils.image.readSVG(manager.getIconVector(), 65, 65);
			} else {
				iconImage = manager.getIconImage();
			}
		}

		if(iconImage != null) {
			this.icon = Utils.image.makeRoundSquircle(iconImage, 65.0d);
		}*/
	}

	public Display getDisplay() {
		return null;
	}

	public static <T extends Application> void register(Class<T> applicationClass) {
		Constructor<T> constructor = null;

		try {
			constructor = applicationClass.getConstructor();
		} catch(NoSuchMethodException Exception) {
			return;
		}

		if(constructor != null) {
			try {
				Application.APPLICATION_LIST.add(constructor.newInstance());
			} catch(Throwable Errors) {
				return;
			}
		}
	}
}
