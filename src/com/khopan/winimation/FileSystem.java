package com.khopan.winimation;

import java.io.File;
import java.io.IOException;

public class FileSystem {
	private static boolean Initialized;

	public static final File HOME_PATH = new File(System.getenv("LOCALAPPDATA") + File.separator + "Winimation OS");
	public static final File APPLICATION_PATH = new File(FileSystem.HOME_PATH.getAbsolutePath() + File.separator + "application");

	private static final int EXIT_NORMAL = 0;
	private static final int EXIT_ERROR = 1;
	private static final int EXIT_ALREADY_EXIST = 2;

	private FileSystem() {
		WinimationOS.LOGGER.info("Loading File System");
		this.createIfNotExist(FileSystem.HOME_PATH);
		this.createIfNotExist(FileSystem.APPLICATION_PATH);
	}

	private int createIfNotExist(File file) {
		if(!file.exists()) {
			if(file.isFile()) {
				try {
					if(!file.createNewFile()) {
						return FileSystem.EXIT_ERROR;
					} else {
						return FileSystem.EXIT_NORMAL;
					}
				} catch(IOException Exception) {
					return FileSystem.EXIT_ERROR;
				}
			} else if(file.isDirectory()) {
				if(!file.mkdirs()) {
					return FileSystem.EXIT_ERROR;
				} else {
					return FileSystem.EXIT_NORMAL;
				}
			} else {
				return FileSystem.EXIT_ERROR;
			}
		} else {
			return FileSystem.EXIT_ALREADY_EXIST;
		}
	}

	public static void initialize() {
		if(FileSystem.Initialized) {
			new FileSystem();
			FileSystem.Initialized = true;
		}
	}
}
