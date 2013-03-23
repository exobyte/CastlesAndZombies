package com.wookoouk.castlesandzombies.entity;


public class Light extends Entity {

	public Light(float X, float Y, boolean solid) {
		super(X, Y, solid, "light");
		setSCALE(4);
		superSolid = true;
	}

}
