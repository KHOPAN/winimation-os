package com.khopan.winimation.kernel;

public class KernelSystem {
	KernelSystem() {}

	public void sleep() {
		this.command("rundll32.exe powrprof.dll,SetSuspendState Sleep");
	}

	public void shutdown() {
		this.command("shutdown -s -t 0");
	}

	public void restart() {
		this.command("shutdown -r -t 0");
	}

	@SuppressWarnings("deprecation")
	public void command(String command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch(Throwable Errors) {
			Errors.printStackTrace();
		}
	}
}
