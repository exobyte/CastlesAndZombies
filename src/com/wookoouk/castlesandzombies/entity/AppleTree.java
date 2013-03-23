package com.wookoouk.castlesandzombies.entity;


public class AppleTree extends Entity {

	public AppleTree(float X, float Y, boolean solid) {
		super(X, Y, solid, "appleTree");
		backgroundOnly = true;
		setSCALE(2);
	}

}
