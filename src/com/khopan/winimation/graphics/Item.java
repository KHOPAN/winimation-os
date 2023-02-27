package com.khopan.winimation.graphics;

import com.khopan.winimation.graphics.interact.Interactable;

public abstract class Item<T extends Item<T>> extends Boundary<T> implements Interactable {

}
