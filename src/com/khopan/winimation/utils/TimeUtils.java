package com.khopan.winimation.utils;

public class TimeUtils {
	TimeUtils() {

	}

	public void wait(long duration, Runnable command) {
		if(duration <= 0) {
			command.run();
			return;
		}

		new Thread(() -> {
			try {
				Thread.sleep(duration);
				command.run();
			} catch(Throwable Errors) {
				Errors.printStackTrace();
			}
		}).start();
	}
}
