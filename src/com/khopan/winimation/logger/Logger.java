package com.khopan.winimation.logger;

public interface Logger {
	public void info(String text);
	public void warning(String text);
	public void error(String text);
	public void log(String text);
}
