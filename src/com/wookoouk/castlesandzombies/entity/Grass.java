package com.wookoouk.castlesandzombies.entity;


public class Grass extends Entity {

	public Grass(float X, float Y, boolean solid) {
		super(X, Y, solid, "grass");
		backgroundOnly = true;
	}

}
