package com.khopan.winimation;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.khopan.winimation.logger.Logger;

public class SimpleLogger implements Logger {
	public static String LOGGER_INFO = "";

	static Runnable OnUpdate;

	private final DateFormat format;
	private final NormalStream normal;
	private final ErrorStream error;

	public SimpleLogger() {
		this.format = new SimpleDateFormat("[dd/MM/yyyy] [HH:mm.ss]");
		this.normal = new NormalStream(System.out);
		this.error = new ErrorStream(System.err);
		System.setOut(this.normal);
		System.setErr(this.error);
	}

	@Override
	public void info(String text) {
		this.normal.info(text);
	}

	@Override
	public void warning(String text) {
		this.error.warning(text);
	}

	@Override
	public void error(String text) {
		this.error.error(text);
	}

	@Override
	public void log(String text) {
		this.normal.println(text);
	}

	private String timeFormat() {
		return this.format.format(Calendar.getInstance().getTime());
	}

	private class NormalStream extends PrintStream {
		private NormalStream(OutputStream output) {
			super(output);
		}

		@Override
		public void print(String text) {
			super.print(SimpleLogger.this.timeFormat() + ": " + text);
		}

		private void info(String text) {
			super.print(SimpleLogger.this.timeFormat() + " [Info]: " + text + "\n");
		}

		@Override
		public void write(byte[] buffer, int offset, int length) {
			SimpleLogger.LOGGER_INFO += new String(buffer, offset, length);
			SimpleLogger.onUpdate();
			super.write(buffer, offset, length);
		}
	}

	private class ErrorStream extends PrintStream {
		private ErrorStream(OutputStream output) {
			super(output);
		}

		@Override
		public void print(String text) {
			super.print(SimpleLogger.this.timeFormat() + ": " + text);
		}

		private void warning(String text) {
			super.print(SimpleLogger.this.timeFormat() + " [Warning]: " + text + "\n");
		}

		private void error(String text) {
			super.print(SimpleLogger.this.timeFormat() + " [Error]: " + text + "\n");
		}

		@Override
		public void write(byte[] buffer, int offset, int length) {
			SimpleLogger.LOGGER_INFO += new String(buffer, offset, length);
			SimpleLogger.onUpdate();
			super.write(buffer, offset, length);
		}
	}

	private static void onUpdate() {
		if(SimpleLogger.OnUpdate != null) {
			SimpleLogger.OnUpdate.run();
		}
	}
}
