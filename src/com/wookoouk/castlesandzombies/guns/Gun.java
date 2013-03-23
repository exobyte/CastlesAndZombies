package com.wookoouk.castlesandzombies.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Gun {
	private Sprite gun;
	private float SCALE = 0.5f;
	private int shortenGun = 5;
	private boolean facingRight = true;

	public Gun(float playerX, float playerY) {
		Texture gunTexture = new Texture(Gdx.files.internal("images/gunNoArm.png"));
		gun = new Sprite(new TextureRegion(gunTexture, shortenGun, 0, gunTexture.getWidth()-shortenGun, gunTexture.getHeight()));
		gun.setSize(gun.getWidth() * SCALE, gun.getHeight() * SCALE);
		gun.setOrigin(gun.getX(), gun.getY()+(gun.getHeight()/2));
		gun.setPosition(playerX, playerY);
		
	}

	public void render(SpriteBatch batch) {
		gun.draw(batch);
	}

	public void update(float gunX, float gunY, double angleToTurn) {

		gun.setPosition(gunX, gunY-(gun.getHeight()/2));
		gun.setRotation((float) angleToTurn);
	}

//	public void right() {
//		if (flippedRight) {
//			gun.setOrigin(gun.getX(), gun.getY()+(gun.getHeight()/2)+offset());
//			gun.flip(false, true);
//			flippedRight = false;
//		}
//	}
//
//	public void left() {
//		if (!flippedRight) {
//			gun.flip(false, true);
//			flippedRight = true;
//		}
//	}

	public float getX() {
		return gun.getX();
	}

	public float getY() {
		return gun.getY();
	}

	public float getHeight() {
		return gun.getHeight();
	}

	public float getWidth() {
		return gun.getWidth();
	}


	public void flip() {
		
		facingRight = !facingRight;
		
		
		
		gun.flip(false, true);
		
	}
	
//	public float offset() {
//		return gun.getHeight()/8;
//	}

}
