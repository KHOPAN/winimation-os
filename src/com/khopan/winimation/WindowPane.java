package com.khopan.winimation;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.khopan.engine.animation.animation.data.DoubleTransition;
import com.khopan.engine.animation.interpolator.Interpolator;
import com.khopan.winimation.graphics.Display;
import com.khopan.winimation.graphics.HomeDisplay;
import com.khopan.winimation.graphics.NavigationBar;
import com.khopan.winimation.graphics.Popup;
import com.khopan.winimation.graphics.PowerBar;
import com.khopan.winimation.graphics.StatusBar;
import com.khopan.winimation.graphics.animation.DisplayAnimation;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.draw.Area.Mode;
import com.khopan.winimation.graphics.draw.GraphicsArea;
import com.khopan.winimation.graphics.interact.Interactable;
import com.khopan.winimation.graphics.interact.Keyboard;
import com.khopan.winimation.graphics.interact.Listener;
import com.khopan.winimation.graphics.interact.Mouse;
import com.khopan.winimation.graphics.popup.ShutdownPopup;
import com.khopan.winimation.graphics.shape.Squircle;
import com.khopan.winimation.utils.Utils;

public class WindowPane extends Component {
	private static final long serialVersionUID = -8631413520049381190L;

	private final DoubleTransition startUpTransition;
	private final DoubleTransition displayTransition;
	private final int barHeight;
	private final StatusBar statusBar;
	private final NavigationBar navigationBar;
	private final PowerBar powerBar;
	private final Listener statusBarListener;
	private final Listener navigationBarListener;
	private final Listener powerBarListener;
	private final List<PopupRenderer> popupList;
	private final Shape letter;
	private final ApplicationManager applicationManager;
	private final LoggerDisplay logger;
	private int startX;
	private int startWidth;
	private float transparent;
	private boolean canShow;
	private boolean finished;
	private Display display;
	private boolean switchingDisplay;
	private double displayProgress;
	private DisplayAnimation displayAnimation;
	private int width;
	private int height;

	@SuppressWarnings("deprecation")
	WindowPane() {
		if(!Properties.FULLSCREEN) {
			this.setPreferredSize(Utils.dimension.aspectRatioWidth(Properties.SCREEN_RATIO, Properties.SCREEN_WIDTH));
		}

		Font font = Settings.font(0.127959053d);
		FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
		FontRenderContext render = new FontRenderContext(null, true, true);
		GlyphVector vector = font.createGlyphVector(render, Properties.OS_NAME);
		double stringWidth = ((double) metrics.stringWidth(Properties.OS_NAME));
		double letterX = ((((double) Properties.SCREEN_DIMENSION.width) - stringWidth) / 2.0d);
		double letterY = ((((double) Properties.SCREEN_DIMENSION.height) - ((double) metrics.getHeight())) / 2.0d + ((double) metrics.getAscent()));

		this.letter = vector.getOutline(
				(float) letterX,
				(float) letterY
				);

		this.popupList = new ArrayList<>();
		this.displayTransition = new DoubleTransition();
		this.displayTransition.setValue(0.0d, 1.0d);
		int duration = Settings.duration(2000);
		this.startUpTransition = new DoubleTransition();
		this.startUpTransition.setDuration(duration);
		this.startUpTransition.setFramerate(Properties.DISPLAY_FRAMERATE);
		this.startUpTransition.setValue(0.0d, 1.0d);
		this.startUpTransition.setInterpolator(Interpolator.CUBIC_EASE_IN_OUT);
		this.startUpTransition.setInvoker(value -> {
			double fadeTime = value < 0.6d ? 0.0d : (value - 0.6d) * 2.5d;
			double currentStringWidth = stringWidth * value;
			this.startX = (int) Math.round(letterX + currentStringWidth);
			this.startWidth = (int) Math.round(stringWidth - currentStringWidth);
			this.transparent = (float) (fadeTime < 0.0d ? 0.0d : fadeTime > 1.0d ? 1.0d : fadeTime);
			this.canShow = true;
			this.repaint();
		});

		this.barHeight = (int) Math.round(Properties.SCREEN_DIMENSION.height * 0.0520833333d);
		this.statusBar = new StatusBar(this);
		this.navigationBar = new NavigationBar(this);
		this.powerBar = new PowerBar(this);
		this.statusBarListener = new Listener(this.statusBar, new Interactable() {
			@Override
			public void mouseInteraction(Mouse mouse) {
				WindowPane.this.statusBar.mouseInteraction(mouse);
			}

			@Override
			public void keyboardInteraction(Keyboard keyboard) {
				WindowPane.this.statusBar.keyboardInteraction(keyboard);
			}
		});

		this.navigationBarListener = new Listener(this.navigationBar, new Interactable() {
			@Override
			public void mouseInteraction(Mouse mouse) {
				WindowPane.this.navigationBar.mouseInteraction(mouse);
			}

			@Override
			public void keyboardInteraction(Keyboard keyboard) {
				WindowPane.this.navigationBar.keyboardInteraction(keyboard);
			}
		});

		this.powerBarListener = new Listener(this.powerBar, new Interactable() {
			@Override
			public void mouseInteraction(Mouse mouse) {
				WindowPane.this.powerBar.mouseInteraction(mouse);
			}

			@Override
			public void keyboardInteraction(Keyboard keyboard) {
				WindowPane.this.powerBar.keyboardInteraction(keyboard);
			}
		});

		this.applicationManager = new ApplicationManager();
		this.logger = new LoggerDisplay();
		this.setFocusable(true);
		this.addKeyListener(Keyboard.create(this :: keyboardInteraction));
		MouseAdapter mouse = Mouse.create(this :: mouseInteraction);
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
		this.addMouseWheelListener(mouse);

		//Utils.time.wait(1000, this.startUpTransition :: begin);
		//Utils.time.wait(duration + 1500, this.startUpTransition :: reverseBegin);
		//Utils.time.wait(duration + duration + 2000, this :: initialize);
		//Utils.time.wait(1000, this :: initialize);
		this.initialize();
	}

	public void popup(Popup popup) {
		if(popup == null) {
			throw new NullPointerException("'popup' cannot be null.");
		}

		this.popupList.add(new PopupRenderer(popup));
		this.repaint();
	}

	public void mouseInteraction(Mouse mouse) {
		if(this.applicationManager.mouseInteraction(mouse)) return;
		if(this.statusBarListener.mouseInteraction(mouse)) return;
		if(this.navigationBarListener.mouseInteraction(mouse)) return;
		if(this.powerBarListener.mouseInteraction(mouse)) return;

		for(int i = this.popupList.size() - 1; i >= 0; i--) {
			if(this.popupList.get(i).mouseInteraction(mouse)) return;
		}
	}

	public void keyboardInteraction(Keyboard keyboard) {
		this.applicationManager.keyboardInteraction(keyboard);
		this.statusBarListener.keyboardInteraction(keyboard);
		this.navigationBarListener.keyboardInteraction(keyboard);
		this.powerBarListener.keyboardInteraction(keyboard);

		for(int i = this.popupList.size() - 1; i >= 0; i--) {
			this.popupList.get(i).keyboardInteraction(keyboard);
		}
	}

	public void setDisplay(Display display) {
		this.setDisplay(display, DisplayAnimation.BLEND);
	}

	public void setDisplay(Display display, DisplayAnimation animation) {
		if(animation == null) {
			this.display = display;
			this.display.pane = this;
			return;
		}

		Display oldDisplay = this.display;
		this.display = display;
		this.display.pane = this;
		this.display.size(Properties.SCREEN_DIMENSION);
		this.displayAnimation = animation;
		int duration = Settings.duration(this.displayAnimation.getRawDuration());
		this.displayTransition.setDuration(duration);
		Interpolator interpolator = this.displayAnimation.getInterpolator();
		this.displayTransition.setInterpolator(interpolator == null ? Interpolator.LINEAR : interpolator);
		this.displayTransition.setInvoker(value -> {
			if(value == 0.0d) {
				this.switchingDisplay = true;
			} else if(value == 1.0d) {
				this.switchingDisplay = false;
				this.display.load();
			}

			this.displayProgress = value;
			this.repaint();
		});

		this.displayAnimation.prepare(oldDisplay, this.display, duration);
		this.displayTransition.begin();
		this.repaint();
	}

	public void showDialog() {

	}

	public void initialize() {
		if(!this.finished) {
			this.finished = true;
			this.setDisplay(new HomeDisplay());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void reshape(int x, int y, int width, int height) {
		super.reshape(x, y, width, height);
		this.width = width;
		this.height = height;
	}

	@Override
	public void paint(Graphics Graphics) {
		Area area = new GraphicsArea(Graphics, this.width, this.height);
		area.smooth();
		area.color(0x000000);
		area.mode(Mode.FILL);
		area.rect(0, 0, Properties.SCREEN_DIMENSION);
		Wallpaper.draw(area);

		if(this.canShow = false) {
			Area letter = area.create();
			Area mask = area.create();
			letter.color(0xFFFFFF);
			letter.draw(this.letter);
			letter.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.transparent));
			letter.color(0xFFFFFF);
			letter.fill(this.letter);
			mask.color(0x000000);
			mask.mode(Mode.FILL);
			mask.rect(this.startX, 0, this.startWidth, Properties.SCREEN_DIMENSION.height);
		}

		if(this.finished = true) {
			if(this.switchingDisplay) {
				this.displayAnimation.animate(area, this.displayProgress);
			} else if(this.display != null) {
				this.display.render(area.create());
			}
		}

		for(int i = 0; i < this.popupList.size(); i++) {
			this.popupList.get(i).render(area);
		}

		this.powerBar.render(area);
		this.statusBar.render(area);
		this.navigationBar.render(area);
		this.applicationManager.render(area);
		this.logger.render(area);

		area.done();

		/*((Graphics2D) Graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics.setColor(new Color(0x000000));
		Graphics.fillRect(0, 0, Properties.SCREEN_DIMENSION.width, Properties.SCREEN_DIMENSION.height);
		Wallpaper.draw(Graphics);

		if(this.canShow = false) {
			Graphics letter = Graphics.create();
			Graphics mask = Graphics.create();
			letter.setColor(new Color(0xFFFFFF));
			((Graphics2D) letter).draw(this.letter);
			((Graphics2D) letter).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.transparent));
			letter.setColor(new Color(0xFFFFFF));
			((Graphics2D) letter).fill(this.letter);
			mask.setColor(new Color(0x000000));
			mask.fillRect(this.startX, 0, this.startWidth, Properties.SCREEN_DIMENSION.height);
		}

		if(this.finished = true) {
			if(this.switchingDisplay) {
				//System.out.println("Animate");
				this.displayAnimation.animate(Graphics, this.displayProgress);
			} else if(this.display != null) {
				this.display.render(Graphics.create());
			}
		}

		//this.animateShape(new RoundRectangle2D.Double(100.0d, 100.0d, 100.0d, 100.0d, 20.0d, 20.0d), Graphics, 0.0d);
		//this.animateShape(this.letter, Graphics, 0.0d);

		//Graphics.setColor(new Color(0x00FF00));
		//Graphics.fillRect(0, 0, Properties.SCREEN_SIZE.width, Properties.SCREEN_SIZE.height);

		for(int i = 0; i < this.popupList.size(); i++) {
			this.popupList.get(i).render(Graphics);
		}

		this.powerBar.render(Graphics);
		this.statusBar.render(Graphics);
		this.navigationBar.render(Graphics);

		Graphics.dispose();*/
	}

	public void applicationManager() {
		this.applicationManager.show();
	}

	public void logger() {
		this.logger.toggle();
	}

	public void togglePowerBar() {
		this.powerBar.visible().toggle();
	}

	public void showBars() {
		this.statusBar.show();
		this.navigationBar.show();
	}

	public void shutdownPopup() {
		if(!this.hasPopup(ShutdownPopup.class)) {
			this.popup(new ShutdownPopup());
		}
	}

	public void refreshApplicationList() {
		this.applicationManager.refreshApplicationList();
	}

	private <T extends Popup> boolean hasPopup(Class<T> type) {
		for(int i = 0; i < this.popupList.size(); i++) {
			if(type.isAssignableFrom(this.popupList.get(i).popup.getClass())) {
				return true;
			}
		}

		return false;
	}

	/*public class NavigationBar {
		private final int y;
		private final Button shutdownButton;

		private NavigationBar() {
			this.y = Properties.SCREEN_DIMENSION.height - WindowPane.this.barHeight;
			this.shutdownButton = new Button();
			this.shutdownButton.bounds().set(new Rectangle(0, this.y, (int) Math.round(((double) WindowPane.this.barHeight) * 2.0d), WindowPane.this.barHeight));
			this.shutdownButton.borderThickness().set((int) Math.round(3.0d));
			this.shutdownButton.text().set("Shutdown");
			this.shutdownButton.font().set(new Font("Itim", Font.BOLD, 12));
			this.shutdownButton.action().set(this :: shutdown);
			this.shutdownButton.target(WindowPane.this);
		}

		private void shutdown() {
			System.out.println("Shutdown");
		}

		private void render(Graphics Graphics) {
			Graphics.setColor(new Color(0x000000));
			Graphics.fillRect(0, this.y, Properties.SCREEN_DIMENSION.width, WindowPane.this.barHeight);
			this.shutdownButton.render(Graphics);
		}
	}*/

	private class ApplicationManager {
		public static final Interpolator PULSE = new Interpolator() {
			private double pulseNormalize = 1.0d;
			private double pulseScale = 4.0d;

			private double pulse(double Time) {
				double result = 0.0d;
				Time *= this.pulseScale;

				if(Time < 1.0d) {
					result = Time - (1.0d - Math.exp(-Time));
				} else {
					double begin = Math.exp(-1.0d);
					result = begin + ((1.0d - Math.exp(-Time + 1.0d)) * (1.0d - begin));
				}

				return result * this.pulseNormalize;
			}

			@Override
			public double interpolate(double Time) {
				if(Time == 0.0d) {
					return 0.0d;
				} else if(Time == 1.0d) {
					return 1.0d;
				}

				if(this.pulseNormalize == 1.0d) {
					this.pulseNormalize /= this.pulse(1.0d);
				}

				return this.pulse(Time);
			}
		};

		private final int iconSize;
		private final DoubleTransition transition;
		private final DoubleTransition applicationTransition;
		/*private final double x;
		private final double y;
		private final double width;
		private final double height;*/
		private double spacing;
		private List<ApplicationIcon> iconList;
		private int transparent;
		private int offset;
		private RoundRectangle2D.Double rectangle;
		private boolean visible;

		private ApplicationIcon application;
		//private Point fromApplicationLocation;
		private Point applicationLocation;
		private RoundRectangle2D.Double applicationRectangle;
		private float iconTransparent;
		private boolean transitionProgress;

		private ApplicationManager() {
			this.iconSize = 65;
			this.spacing = 50.0d;
			double arc = 30.0d;
			double twoSpacing = this.spacing * 2.0d;
			this.rectangle = new RoundRectangle2D.Double(this.spacing, this.spacing, ((double) Properties.SCREEN_DIMENSION.width) - twoSpacing, ((double) Properties.SCREEN_DIMENSION.height) - twoSpacing, arc, arc);
			/*double spacing = 50.0d;
			double twoSpacing = spacing * 2.0d;
			double width = ((double) Properties.SCREEN_DIMENSION.width) - twoSpacing;
			double arc = 30.0d;
			double beginX = -width - spacing;
			double start = spacing - beginX;
			this.x = spacing;
			this.y = spacing;
			this.width = width;
			this.height = ((double) Properties.SCREEN_DIMENSION.height) - twoSpacing;
			this.rectangle = new RoundRectangle2D.Double(beginX, this.y, this.width, this.height, arc, arc);*/
			double begin = -this.rectangle.width - this.spacing;
			this.offset = (int) Math.round(begin);
			this.transition = new DoubleTransition();
			this.transition.setDuration(Settings.duration(1000));
			this.transition.setFramerate(Properties.DISPLAY_FRAMERATE);
			this.transition.setValue(0.0, 1.0d);
			this.transition.setInterpolator(Interpolator.CUBIC_EASE_OUT);
			this.transition.onlyPlayWhenDone();
			this.transition.noJump();
			this.transition.setInvoker(value -> {
				this.transparent = (int) Math.round(value * 200.0d);
				this.offset = (int) Math.round((1.0d - value) * begin);
				//this.rectangle.x = (int) Math.round(value * start + beginX);
				WindowPane.this.repaint();
			});

			double toX = (((double) Properties.SCREEN_DIMENSION.width) - ((double) this.iconSize)) * 0.5d;
			double toY = (((double) Properties.SCREEN_DIMENSION.height) - ((double) this.iconSize)) * 0.5d;
			double half = ((double) this.iconSize) * 0.5d;
			this.applicationTransition = new DoubleTransition();
			this.applicationTransition.setDuration(Settings.duration(1000));
			this.applicationTransition.setFramerate(Properties.DISPLAY_FRAMERATE);
			this.applicationTransition.setValue(0.0d, 1.0d);
			this.applicationTransition.setInterpolator(Interpolator.CUBIC_EASE_IN_OUT);
			this.applicationTransition.setInvoker(value -> {
				if(value == 0.0d) {
					this.transitionProgress = true;
					this.iconTransparent = 1.0f;
				}

				this.applicationLocation = new Point(
						(int) Math.round(value * (toX - ((double) this.application.x)) + ((double) this.application.x)),
						(int) Math.round(value * (toY - ((double) this.application.y)) + ((double) this.application.y))
						);

				double reverse = 1.0d - value;
				double fromX = ((double) this.application.x) + half;
				double fromY = ((double) this.application.y) + half;
				int intermediteArc = (int) Math.round(reverse * ((double) this.iconSize));

				this.applicationRectangle = new RoundRectangle2D.Double(
						(int) Math.round(reverse * fromX),
						(int) Math.round(reverse * fromY),
						(int) Math.round(value * ((double) Properties.SCREEN_DIMENSION.width)),
						(int) Math.round(value * ((double) Properties.SCREEN_DIMENSION.height)),
						intermediteArc,
						intermediteArc
						);

				if(value == 1.0d) {
					DoubleTransition transition = new DoubleTransition();
					transition.setDuration(Settings.duration(500));
					transition.setFramerate(Properties.DISPLAY_FRAMERATE);
					transition.setValue(1.0d, 0.0d);
					transition.setInterpolator(Interpolator.SINE_EASE_IN_OUT);
					transition.setInvoker(valueFade -> {
						this.iconTransparent = (float) (double) valueFade;
						WindowPane.this.repaint();
					});

					transition.begin();
					this.transitionProgress = false;
				}

				WindowPane.this.repaint();
			});

			this.refreshApplicationList();
		}

		private void refreshApplicationList() {
			this.iconList = new ArrayList<>();
			int size = Application.APPLICATION_LIST.size();
			int rows = (int) Math.ceil(Math.sqrt(((double) size)));
			int columns = (int) Math.ceil(((double) size) / ((double) rows));
			double xspacing = (-((double) this.iconSize) * ((double) rows) + this.rectangle.width) / (((double) rows) + 1.0d);
			double yspacing = (-((double) this.iconSize) * ((double) columns) + this.rectangle.height) / (((double) columns) + 1.0d);
			int x = 0;
			int y = 1;

			for(int i = 0; i < size; i++) {
				x++;

				this.iconList.add(new ApplicationIcon(
						(int) Math.round(this.rectangle.x + ((double) x) * xspacing + (((double) x) - 1.0d) * ((double) this.iconSize)),
						(int) Math.round(this.rectangle.y + ((double) y) * yspacing + (((double) y) - 1.0d) * ((double) this.iconSize)),
						Application.APPLICATION_LIST.get(i)
						));

				if(x >= rows) {
					x = 0;
					y++;
				}
			}
		}

		private void open(ApplicationIcon application) {
			if(!this.transitionProgress) {
				WinimationOS.LOGGER.info("Opening '" + application.application.name + "' application.");
				this.application = application;
				this.applicationTransition.begin();
				WindowPane.this.setDisplay(this.application.application.display);
				this.hide();
			}
		}

		private void show() {
			WinimationOS.LOGGER.info("Opening Application Manager");
			this.visible = true;
			this.transition.begin();
		}

		private void hide() {
			WinimationOS.LOGGER.info("Closing Application Manager");
			this.visible = false;
			this.transition.reverseBegin();
		}

		private void render(Area area) {
			Area renderer = area.create();
			renderer.color(0, 0, 0, this.transparent > 255 ? 255 : this.transparent < 0 ? 0 : this.transparent);
			renderer.mode(Mode.FILL);
			renderer.rect(0, 0, Properties.SCREEN_DIMENSION);
			renderer.color(68, 69, 74, 160);
			renderer.translate(this.offset, 0);
			renderer.fill(this.rectangle);

			for(int i = 0; i < this.iconList.size(); i++) {
				this.iconList.get(i).render(renderer);
			}

			if(this.application != null) {
				Area icon = area.create();
				icon.smooth();
				icon.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.iconTransparent));
				icon.color(this.application.application.color);
				icon.fill(this.applicationRectangle);
				icon.translate(this.applicationLocation.x, this.applicationLocation.y);
				icon.image(this.application.application.icon);
			}

			//renderer.rect(20, 20, Properties.SCREEN_DIMENSION.width - 40, Properties.SCREEN_DIMENSION.height - 40);
		}

		private boolean mouseInteraction(Mouse mouse) {
			/*System.out.println("Visible");

			if(this.visible) {
				this.hide();
				return true;
			} else {
				return false;
			}*/

			if(mouse.type == Mouse.MOUSE_RELEASED) {
				if(this.rectangle.contains(mouse.location)) {
					for(int i = 0; i < this.iconList.size(); i++) {
						if(this.iconList.get(i).mouseReleased(mouse)) return true;
					}

					return true;
				} else {
					this.hide();
				}
			} else {
				if(this.visible) {
					return true;
				}
			}

			return false;
		}

		private void keyboardInteraction(Keyboard keyboard) {

		}

		private class ApplicationIcon {
			private final int size;
			private final int x;
			private final int y;
			private Application application;

			private ApplicationIcon(int x, int y, Application application) {
				this.size = ApplicationManager.this.iconSize;
				this.x = x;
				this.y = y;
				this.application = application;
			}

			private boolean mouseReleased(Mouse mouse) {
				AffineTransform transform = new AffineTransform();
				transform.translate(this.x, this.y);

				if(transform.createTransformedShape(new Squircle(this.size)).contains(mouse.location)) {
					ApplicationManager.this.open(this);
					return true;
				}

				return false;
			}

			private void render(Area area) {
				Area renderer = area.create();
				renderer.smooth();
				//renderer.square(this.x, this.y, ApplicationManager.this.iconSize);
				//renderer.fill(new RoundRectangle2D.Double(this.x, this.y, ApplicationManager.this.iconSize, ApplicationManager.this.iconSize, 48, 48));
				renderer.translate(this.x, this.y);
				//renderer.fill(new Squircle(ApplicationManager.this.iconSize));
				renderer.image(this.application.icon);

				/*Color lightBlue = new Color(57, 152, 251);
				Color darkBlue = new Color(35, 82, 170);

				// Create the gradient background
				GradientPaint background = new GradientPaint(0, 0, lightBlue, this.size, this.size, darkBlue);
				renderer.paint(background);
				renderer.fill(new Rectangle2D.Double(0, 0, this.size, this.size));

				renderer.color(Color.WHITE);
				renderer.fill(new Ellipse2D.Double(this.size * 0.15d, this.size * 0.15d, this.size * 0.7d, this.size * 0.7d));

				// Draw the blue circle
				renderer.color(darkBlue);
				renderer.fill(new Ellipse2D.Double(this.size * 0.25d, this.size * 0.25d, this.size * 0.5d, this.size * 0.5d));

				// Draw the white line
				renderer.color(Color.WHITE);
				renderer.stroke(new BasicStroke(5));
				renderer.line(this.size / 2, this.size / 2, this.size / 2, (int) Math.round(this.size * 0.75));*/
			}
		}
	}

	private class PopupRenderer {
		private final Popup popup;
		private final Listener listener;
		private final DoubleTransition transition;
		private BufferedImage fromImage;
		private Rectangle bounds;
		private Rectangle fromBounds;
		private Rectangle toBounds;
		private Dimension beforeSize;
		private Image image;
		private boolean animate;
		private float transparent;
		private Dimension size;
		private boolean back;

		private PopupRenderer(Popup popup) {
			this.popup = popup;
			this.listener = new Listener(() -> this.bounds, this.popup);
			Dimension size = this.popup.getSize();
			this.size = size;
			this.fromImage = new BufferedImage(this.size.width, this.size.height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D graphics = this.fromImage.createGraphics();
			this.popup.width = this.size.width;
			this.popup.height = this.size.height;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.renderPopup(new GraphicsArea(graphics, this.popup.width, this.popup.height));
			graphics.dispose();
			this.toBounds = new Rectangle((Properties.SCREEN_DIMENSION.width - this.size.width) / 2, (Properties.SCREEN_DIMENSION.height - this.size.height) / 2, size.width, size.height);
			this.fromBounds = new Rectangle(this.toBounds.x, Properties.SCREEN_DIMENSION.height, this.size.width, this.size.height);
			this.beforeSize = new Dimension();
			this.transition = new DoubleTransition();
			this.transition.setDuration(Settings.duration(1200));
			this.transition.setFramerate(Properties.DISPLAY_FRAMERATE);
			this.transition.setValue(0.0d, 1.0d);
			this.transition.setInterpolator(Interpolator.CUBIC_EASE_IN_OUT);
			this.transition.setInvoker(value -> {
				this.animate = true;
				float transparent = (float) (double) value;
				this.transparent = transparent < 0.0f ? 0.0f : transparent > 1.0f ? 1.0f : transparent;
				this.bounds = new Rectangle(
						(int) Math.round(value * (((double) this.toBounds.x) - ((double) this.fromBounds.x)) + ((double) this.fromBounds.x)),
						(int) Math.round(value * (((double) this.toBounds.y) - ((double) this.fromBounds.y)) + ((double) this.fromBounds.y)),
						(int) Math.round(value * (((double) this.toBounds.width) - ((double) this.fromBounds.width)) + ((double) this.fromBounds.width)),
						(int) Math.round(value * (((double) this.toBounds.height) - ((double) this.fromBounds.height)) + ((double) this.fromBounds.height))
						);

				Dimension boundsSize = this.bounds.getSize();

				if(this.beforeSize.width != boundsSize.width || boundsSize.height != boundsSize.height) {
					this.image = this.fromImage.getScaledInstance(boundsSize.width, boundsSize.height, BufferedImage.SCALE_SMOOTH);
					this.beforeSize = boundsSize;
				}

				if(value == 1.0d) {
					this.animate = false;
				} else if(value == 0.0d) {
					if(this.back) {
						WindowPane.this.popupList.remove(this);
					}
				}

				WindowPane.this.repaint();
			});

			this.transition.begin();
		}

		private void renderPopup(Area area) {
			if(this.popup.drawBasePlane) {
				area.color(0xFFFFFF);
				area.fill(new RoundRectangle2D.Double(0, 0, this.popup.width, this.popup.height, this.popup.roundness, this.popup.roundness));
			}

			this.popup.render(area);
		}

		private boolean mouseInteraction(Mouse mouse) {
			if(!this.back && mouse.type == Mouse.MOUSE_PRESSED && !new RoundRectangle2D.Double(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, this.popup.roundness, this.popup.roundness).contains(mouse.location)) {
				this.fromImage = new BufferedImage(this.size.width, this.size.height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D graphics = this.fromImage.createGraphics();
				this.popup.width = this.size.width;
				this.popup.height = this.size.height;
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				this.renderPopup(new GraphicsArea(graphics, this.popup.width, this.popup.height));
				this.back = true;
				this.transition.reverseBegin();
				return true;
			}

			return this.listener.mouseInteraction(mouse);
		}

		private void keyboardInteraction(Keyboard keyboard) {
			this.listener.keyboardInteraction(keyboard);
		}

		private void render(Area area) {
			if(this.bounds != null) {
				area.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.transparent));
				area.color(0, 0, 0, 127);
				area.mode(Mode.FILL);
				area.rect(0, 0, Properties.SCREEN_DIMENSION);

				if(this.animate) {
					area.image(this.image, this.bounds.x, this.bounds.y);
				} else {
					this.popup.width = this.bounds.width;
					this.popup.height = this.bounds.height;
					this.renderPopup(area.create(this.bounds));
				}
			}
		}
	}

	private class LoggerDisplay {
		private final DoubleTransition transition;
		private RoundRectangle2D.Double rectangle;
		private boolean visible;
		private int offset;

		private LoggerDisplay() {
			SimpleLogger.OnUpdate = this :: update;
			double width = 500.0d;
			double spacing = 50.0d;
			double arc = 30.0d;

			this.offset = (int) Math.round(width);
			this.rectangle = new RoundRectangle2D.Double(
					((double) Properties.SCREEN_DIMENSION.width) - width,
					spacing,
					width + arc * 0.5d,
					((double) Properties.SCREEN_DIMENSION.height) - spacing * 2.0d,
					arc,
					arc
					);

			this.transition = new DoubleTransition();
			this.transition.setDuration(Settings.duration(1000));
			this.transition.setFramerate(Properties.DISPLAY_FRAMERATE);
			this.transition.setValue(0.0d, 1.0d);
			this.transition.setInterpolator(Interpolator.CUBIC_EASE_IN_OUT);
			this.transition.setInvoker(value -> {
				this.offset = (int) Math.round((1.0d - value) * width);
				/*this.rectangle = new RoundRectangle2D.Double(
						value * (xEnd - ((double) Properties.SCREEN_DIMENSION.width)) + ((double) Properties.SCREEN_DIMENSION.width),
						spacing,
						rectangleWidth,
						height,
						arc,
						arc
						);*/

				WindowPane.this.repaint();
			});
		}

		private void toggle() {
			if(this.visible) {
				WinimationOS.LOGGER.info("Closing Logger Console");
				this.transition.reverseBegin();
				this.visible = false;
			} else {
				WinimationOS.LOGGER.info("Opening Logger Console");
				this.transition.begin();
				this.visible = true;
			}
		}

		private String[] texts;

		private void update() {
			this.texts = SimpleLogger.LOGGER_INFO.split("\n");

			if(this.visible) {
				WindowPane.this.repaint();
			}
		}

		private void render(Area area) {
			Area logger = area.create();
			logger.smooth();
			logger.composite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			logger.color(0xFFFFFF);
			logger.translate(this.offset, 0);

			if(this.rectangle != null) {
				logger.fill(this.rectangle);
				logger.clip(this.rectangle);
				logger.color(0x000000);
				logger.font(new Font("Consolas", Font.BOLD, 9));
				FontMetrics metrics = logger.metrics();
				double ascent = ((double) metrics.getAscent());
				double height = ((double) metrics.getHeight());
				double decrease = height + ascent;
				double y = this.rectangle.y + this.rectangle.height - ascent;
				int intAscent = (int) Math.round(this.rectangle.x + ascent);

				for(int i = this.texts.length - 1; i > 0; i--) {
					if(y + height < this.rectangle.y) {
						break;
					}

					logger.text(this.texts[i], intAscent, (int) Math.round(y));
					y -= decrease;
				}
			}
		}
	}
}
