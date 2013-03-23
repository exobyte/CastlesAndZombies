package com.wookoouk.castlesandzombies.effects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.wookoouk.castlesandzombies.entity.*;

public class ExplosionParticle {
	private float directionX;
	private float directionY;
	private Texture texture;
	private Sprite sprite;
	private float AGE;
	private float SPEED = 3;
	private final float SCALE = 1f;

	private float MAXAGE = 1;
	private boolean dead;
	private float AGEMULTIPLIER = 5;

	public ExplosionParticle(float X, float Y) {
		Random rand = new Random();

		texture = new Texture(Gdx.files.internal("images/whiteSquare.png"));
		sprite = new Sprite(texture);
		sprite.setSize(SCALE+rand.nextFloat(), SCALE+rand.nextFloat());
		sprite.setColor(1, rand.nextFloat(), 0, 1);
		sprite.setPosition(X, Y);
		directionX = (rand.nextFloat() / 2) - (rand.nextFloat() / 2);
		directionY = (rand.nextFloat() / 2) - (rand.nextFloat() / 2);

		MAXAGE += rand.nextFloat();
	}

	public void render(SpriteBatch batch) {

		sprite.draw(batch);

	}

	private void gravity(ArrayList<Entity> arrayList) {
		sprite.translateY(directionY * SPEED);
		//
		if (testCollision(arrayList)) {
			sprite.translateY(-directionY * SPEED);
			directionY = 0;
		}
		//
		sprite.translateX(directionX * SPEED);
		if (testCollision(arrayList)) {
			sprite.translateX(-directionX * SPEED);
			directionX = 0;
		}
	}

	private boolean testCollision(ArrayList<Entity> arrayList) {

		boolean collision = false;

		for (Entity r : arrayList) {
			if (r.isSolid() && !r.isSign() && !r.isItem()
					&& r.getBox().overlaps(sprite.getBoundingRectangle())) {
				collision = true;
			}
		}
		return collision;
	}

	public boolean isDead() {
		return dead;
	}

	public void update(ArrayList<Entity> arrayList) {
		gravity(arrayList);

		AGE += Gdx.graphics.getDeltaTime() * AGEMULTIPLIER;
		if (AGE > MAXAGE) {
			dead = true;
		}
	}

	public Rectangle getBox() {
		return sprite.getBoundingRectangle();
	}

	public void setDead() {
		dead = true;
	}
}
