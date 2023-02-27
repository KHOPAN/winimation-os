package com.khopan.winimation.graphics;

import java.awt.Rectangle;

import com.khopan.winimation.graphics.interact.Node;

@FunctionalInterface
public interface BoundaryNode extends Node {
	public void boundaryUpdate(Rectangle bounds);

	public default void boundaryUpdateCallback(Rectangle bounds) {}
}
