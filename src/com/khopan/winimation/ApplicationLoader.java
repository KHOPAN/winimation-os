package com.khopan.winimation;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ApplicationLoader {
	private static boolean Initialized;
	private ClassLoader loader;

	private ApplicationLoader(String[] args) {
		WinimationOS.LOGGER.info("Loading Application Loader");

		if(args.length >= 1) {
			for(int i = 0; i < args.length; i++) {
				try {
					EntryList.addTest(Class.forName(args[i]));
				} catch(ClassNotFoundException Exception) {

				}
			}
		}

		File[] list = FileSystem.APPLICATION_PATH.listFiles();
		List<File> applicationList = new ArrayList<>();
		List<URL> urlList = new ArrayList<>();

		for(File file : list) {
			if(file.isFile()) {
				String name = file.getName();

				if(name.endsWith(".wap")) {
					applicationList.add(file);

					try {
						urlList.add(file.toURI().toURL());
					} catch(Throwable Errors) {
						Errors.printStackTrace();
					}
				}
			}
		}

		if(list.length != 0) {
			this.loader = new URLClassLoader(urlList.toArray(new URL[urlList.size()]), ApplicationLoader.class.getClassLoader());

			for(File file : applicationList) {
				this.parseFile(file);
			}
		}
	}

	private void parseFile(File file) {
		try {
			ZipFile zip = new ZipFile(file);

			try {
				Enumeration<? extends ZipEntry> entries = zip.entries();

				while(entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();

					if(!entry.isDirectory()) {
						String name = entry.getName();

						if(name.endsWith(".class")) {
							String rawName = entry.getName().replaceAll("/", ".");
							String className = rawName.substring(0, rawName.lastIndexOf('.'));
							EntryList.addTest(this.loader.loadClass(className));
						}
					}
				}
			} finally {
				zip.close();
			}
		} catch(Throwable Errors) {
			Errors.printStackTrace();
		}
	}

	public static void initialize(String[] args) {
		if(!ApplicationLoader.Initialized) {
			new ApplicationLoader(args);
			ApplicationLoader.Initialized = true;
		}
	}
}
