package com.wookoouk.castlesandzombies.entity;

import com.wookoouk.castlesandzombies.players.*;


public class Ammo extends Entity {

	public Ammo(float X, float Y, boolean solid) {
		super(X, Y, solid, "ammo");
		superSolid = true;
		item = true;
//		setSCALE(1);
	}

	@Override
	public void applyItem(Player player) {
		player.addAmmo(50);
	}
	
}
