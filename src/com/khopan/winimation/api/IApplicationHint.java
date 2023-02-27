package com.khopan.winimation.api;

import com.khopan.winimation.graphics.Display;

public interface IApplicationHint {
	public default void initialize() {}
	public default Display getDisplay() {return null;}
	public default IconManager getIcon() {return null;}
}
