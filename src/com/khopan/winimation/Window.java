package com.khopan.winimation;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

class Window {
	private static boolean Initialized;
	static Window Instance;

	final JFrame frame;
	final WindowPane pane;

	private Window() {
		WinimationOS.LOGGER.info("Initializing Graphics Context");
		this.pane = new WindowPane();
		this.frame = new JFrame();
		this.frame.setTitle(Properties.OS_NAME + " " + Properties.OS_VERSION);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		if(Properties.FULLSCREEN) {
			this.frame.setLayout(new BorderLayout());
			this.frame.add(this.pane, BorderLayout.CENTER);
			this.frame.setUndecorated(true);
			this.frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		} else {
			this.frame.add(this.pane);
			this.frame.pack();
		}

		this.frame.setResizable(false);
		this.frame.setLocationRelativeTo(null);
		WinimationOS.LOGGER.info("Winimation OS Startup");
		this.frame.setVisible(true);
	}

	public static void initialize() {
		if(!Window.Initialized) {
			Window.Instance = new Window();
			Window.Initialized = true;
		}
	}
}
