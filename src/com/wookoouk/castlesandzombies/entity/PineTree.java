package com.wookoouk.castlesandzombies.entity;


public class PineTree extends Entity {

	public PineTree(float X, float Y, boolean solid) {
		super(X, Y, solid, "pineTree");
		backgroundOnly = true;
		setSCALE(2);
	}

}
