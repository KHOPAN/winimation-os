package com.khopan.winimation.graphics.popup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import com.khopan.winimation.GraphicsSystem;
import com.khopan.winimation.Settings;
import com.khopan.winimation.graphics.Popup;
import com.khopan.winimation.graphics.common.Button3D;
import com.khopan.winimation.graphics.draw.Area;
import com.khopan.winimation.graphics.interact.Keyboard;
import com.khopan.winimation.graphics.interact.Mouse;

public class ShutdownPopup extends Popup {
	private final String text;
	private final Font font;
	private final int spacing;
	private int sizeWidth;
	private int sizeHeight;
	private final Button3D cancelButton;
	private final Button3D sleepButton;
	private final Button3D restartButton;
	private final Button3D shutdownButton;
	private int textY;

	public ShutdownPopup() {
		this.text = "Are you sure you want to shutdown? Maybe you want to sleep or restart?";
		this.font = Settings.font(0.02d);
		this.spacing = 15;
		FontMetrics metrics = GraphicsSystem.getFontMetrics(this.font);
		//int ascent = metrics.getAscent();
		double twoSpacing = ((double) this.spacing) * 2.0d;
		this.sizeWidth = (int) Math.round(((double) metrics.stringWidth(this.text)) + twoSpacing);
		double fontHeight = ((double) metrics.getHeight());
		double buttonHeight = ((double) fontHeight) * 1.5d;
		this.sizeHeight = (int) Math.round(fontHeight + ((double) this.spacing) * 3.0d + buttonHeight);
		this.textY = (int) Math.round(((double) this.spacing) + fontHeight);
		int buttonY = (int) Math.round(((double) this.spacing) * 2.0d + fontHeight);
		int buttonWidth = (int) Math.round((((double) this.sizeWidth) - ((double) this.spacing) * 5.0d) / 4.0d);
		int buttonX = this.spacing;
		int buttonHeightInt = (int) Math.round(buttonHeight);
		Font buttonFont = Settings.font(0.015d);
		this.cancelButton = new Button3D();
		this.cancelButton.text().set("Cancel");
		this.cancelButton.background().set(new Color(0x34D334));
		this.cancelButton.keyBinding().set(new KeyStroke[] {KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.VK_UNDEFINED)});
		this.cancelButton.font().set(buttonFont);
		this.cancelButton.levels().set(7);
		this.cancelButton.autoForeground().enable();
		this.cancelButton.bounds().set(new Rectangle(buttonX, buttonY, buttonWidth, buttonHeightInt));
		buttonX += this.spacing + buttonWidth;
		this.sleepButton = new Button3D();
		this.sleepButton.text().set("Sleep");
		this.sleepButton.background().set(new Color(0x3498DB));
		this.sleepButton.keyBinding().set(new KeyStroke[] {KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.VK_UNDEFINED)});
		this.sleepButton.font().set(buttonFont);
		this.sleepButton.levels().set(7);
		this.sleepButton.autoForeground().enable();
		this.sleepButton.bounds().set(new Rectangle(buttonX, buttonY, buttonWidth, buttonHeightInt));
		buttonX += this.spacing + buttonWidth;
		this.restartButton = new Button3D();
		this.restartButton.text().set("Restart");
		this.restartButton.background().set(new Color(0xD6D635));
		this.restartButton.keyBinding().set(new KeyStroke[] {KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.VK_UNDEFINED)});
		this.restartButton.font().set(buttonFont);
		this.restartButton.levels().set(7);
		this.restartButton.autoForeground().enable();
		this.restartButton.bounds().set(new Rectangle(buttonX, buttonY, buttonWidth, buttonHeightInt));
		buttonX += this.spacing + buttonWidth;
		this.shutdownButton = new Button3D();
		this.shutdownButton.text().set("Shutdown");
		this.shutdownButton.background().set(new Color(0xD83434));
		this.shutdownButton.keyBinding().set(new KeyStroke[] {KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED)});
		this.shutdownButton.font().set(buttonFont);
		this.shutdownButton.levels().set(7);
		this.shutdownButton.autoForeground().enable();
		this.shutdownButton.bounds().set(new Rectangle(buttonX, buttonY, buttonWidth, buttonHeightInt));
		buttonX += this.spacing + buttonWidth;
	}

	@Override
	public void mouseInteraction(Mouse mouse) {
		this.cancelButton.mouseInteraction(mouse);
		this.sleepButton.mouseInteraction(mouse);
		this.restartButton.mouseInteraction(mouse);
		this.shutdownButton.mouseInteraction(mouse);
	}

	@Override
	public void keyboardInteraction(Keyboard keyboard) {
		this.cancelButton.keyboardInteraction(keyboard);
		this.sleepButton.keyboardInteraction(keyboard);
		this.restartButton.keyboardInteraction(keyboard);
		this.shutdownButton.keyboardInteraction(keyboard);
	}

	@Override
	public Dimension getSize() {
		System.out.println(this.sizeWidth + " " + this.sizeHeight);
		return new Dimension(this.sizeWidth, this.sizeHeight);
	}

	@Override
	public void render(Area area) {
		area.font(this.font);
		area.color(0x000000);
		area.textCenterX(this.text, this.textY);
		this.cancelButton.render(area);
		this.sleepButton.render(area);
		this.restartButton.render(area);
		this.shutdownButton.render(area);
	}
}
