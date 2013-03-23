package com.wookoouk.castlesandzombies.entity;

import com.wookoouk.castlesandzombies.players.*;


public class Heart extends Entity {

	public Heart(float X, float Y, boolean solid) {
		super(X, Y, solid, "heart");
		superSolid = true;
		item = true;
		setSCALE(1);
	}

	

	@Override
	public void applyItem(Player player) {
		player.addHealth(30);
	}
	
}
