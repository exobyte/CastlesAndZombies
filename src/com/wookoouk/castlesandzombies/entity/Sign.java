package com.wookoouk.castlesandzombies.entity;



public class Sign extends Entity {

	public Sign(float X, float Y, boolean solid) {
		super(X, Y, solid, "sign");
		sign = true;
		superSolid = true;
//		signText = "Hello, I am a sign";
		setSCALE(0.6f);
	}

//	@Override
//	public void applyItem(Player player) {
//		player.addAmmo(10);
//	}
	
}
