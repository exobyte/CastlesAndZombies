package com.wookoouk.castlesandzombies.guns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
	private Sprite bullet;
	private float rotation;
	private float speed = 100;
//	private Random rand;
//	private boolean badAim = false;
//	private float badAimMultiplier = 0.5f;
	private float angleX;
	private float angleY;
	private float age;
	public float MAXAGE = 5f;
	public static float bulletWidth = 2;
	public static float bulletHeight = 0.8f;

	public Bullet(float playerX, float playerY, float rotation) {
		Texture bulletTexture = new Texture(
				Gdx.files.internal("images/bullet.png"));
		bullet = new Sprite(bulletTexture);
		this.rotation = rotation;

		bullet.setSize(bulletWidth, bulletHeight);
		bullet.setOrigin(bullet.getX() + (bullet.getWidth() / 2), bullet.getY()
				+ (bullet.getHeight() / 2));
//		bullet.setPosition(playerX, playerY);
		bullet.setRotation(rotation);

		angleX = (float) Math.cos(Math.toRadians(rotation))
				* ((speed) * Gdx.graphics.getDeltaTime());
		angleY = (float) Math.sin(Math.toRadians(rotation))
				* ((speed) * Gdx.graphics.getDeltaTime());

		bullet.setPosition(playerX+angleX*3, playerY+angleY*3);
		
//		rand = new Random();

		// if (badAim) {
		// angleX += rand.nextFloat() - rand.nextFloat();
		// angleY += rand.nextFloat() - rand.nextFloat();
		// }

	}

	public void render(SpriteBatch batch) {
		bullet.draw(batch);
	}

	public void update() {
		age += Gdx.graphics.getDeltaTime();
		bullet.translate(angleX, angleY);
	}

	public float getRotation() {
		return rotation;
	}

	public float getAge() {
		return age;
	}

	public Rectangle getBox() {
		return bullet.getBoundingRectangle();
	}
}
