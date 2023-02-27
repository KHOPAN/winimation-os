package com.khopan.winimation.graphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.khopan.winimation.GraphicsSystem;
import com.khopan.winimation.Settings;
import com.khopan.winimation.graphics.bar.BarElement;
import com.khopan.winimation.graphics.draw.Area;

public class TimeElement extends BarElement {
	private final DateFormat format;
	private final Font font;
	private String time;

	public TimeElement() {
		this.format = new SimpleDateFormat("HH:mm.ss EEEE dd MMMM yyyy");
		this.font = Settings.font(0.02d);
		this.tick();
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this :: tick, 0, 1, TimeUnit.SECONDS);
	}

	private void tick() {
		this.time = this.format.format(Calendar.getInstance().getTime());
		FontMetrics metrics = GraphicsSystem.getFontMetrics(this.font);
		this.size = (int) Math.round(((double) metrics.stringWidth(this.time)) + ((double) metrics.getAscent()) * 2.0d);
		this.update();
	}

	@Override
	public void render(Area area) {
		area.color(0xFFFFFF);
		area.font(this.font);
		area.textCenter(this.time);
	}
}
