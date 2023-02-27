package com.khopan.winimation.graphics;

import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Files;

import com.khopan.winimation.Properties;
import com.khopan.winimation.SimpleLogger;
import com.khopan.winimation.WindowPane;
import com.khopan.winimation.graphics.bar.Bar;
import com.khopan.winimation.graphics.bar.BarAnimationDirection;
import com.khopan.winimation.graphics.bar.BarDirection;
import com.khopan.winimation.graphics.bar.TextElement;

public class NavigationBar extends Bar<NavigationBar> {
	private final WindowPane pane;

	public NavigationBar(WindowPane pane) {
		this.pane = pane;
		this.bounds().set(new Rectangle(0, Properties.SCREEN_DIMENSION.height - Properties.BAR_HEIGHT, Properties.SCREEN_DIMENSION.width, Properties.BAR_HEIGHT));
		this.animationDirection().set(BarAnimationDirection.BOTTOM_RIGHT);
		this.direction().set(BarDirection.HORIZONTAL);
		this.transparent().set(0.75f);
		this.element().add(new TextElement("Application Manager").action().set(() -> this.pane.applicationManager()));
		this.element().add(new TextElement("Logger Console").action().set(() -> this.pane.logger()));
		this.element().add(new TextElement("Save Content").action().set(() -> {
			try {
				Files.write(new File("C:\\Users\\puthi\\Downloads\\logger.log").toPath(), SimpleLogger.LOGGER_INFO.getBytes());
			} catch(Throwable Errors) {
				Errors.printStackTrace();
			}
		}));
	}

	@Override
	public void update() {
		this.pane.repaint();
	}
}
