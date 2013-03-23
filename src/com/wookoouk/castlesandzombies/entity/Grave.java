package com.wookoouk.castlesandzombies.entity;


public class Grave extends Entity {

	public Grave(float X, float Y, boolean solid) {
		super(X, Y, solid, "grave");
		backgroundOnly = true;
	}

}
