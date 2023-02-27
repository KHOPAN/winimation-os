package com.khopan.winimation;

import com.khopan.core.library.LibraryRelease;
import com.khopan.core.library.LibraryVersion;

public class OSVersion {
	private OSVersion() {}

	public static LibraryVersion WINIMATION_OS = new LibraryVersion("1.0.0", LibraryRelease.ALPHA);
	public static LibraryVersion KERNEL = new LibraryVersion("1.0.0", LibraryRelease.BETA);
}
