package com.wookoouk.castlesandzombies.effects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.mobs.*;

public class DeathParticle {

	private float directionX;
	private Texture texture;
	private Sprite sprite;
	private float SIZE = 1f;
	private float bounceAmmount = 0.2f;
	private float AGE;

	private final double gravity = 1; // TODO
	private float yVelocity;

	private float MAXAGE = 3;
	private boolean dead;

	public DeathParticle(float X, float Y, Mob m) {
		Random rand = new Random();

		texture = new Texture(Gdx.files.internal("images/whiteSquare.png"));
		sprite = new Sprite(texture);
		sprite.setSize(SIZE,SIZE);
		sprite.setColor(m.getColor());

		float randHeight = rand.nextInt((int) m.getHeight()
				- rand.nextInt((int) m.getHeight()));
		float randWidth = rand.nextInt((int) m.getWidth()
				- rand.nextInt((int) m.getWidth()));

		float startX = X + randWidth;
		float startY = Y + randHeight;

		sprite.setPosition(startX, startY);
		directionX = (rand.nextFloat() / 2) - (rand.nextFloat() / 2);

		bounceAmmount += (rand.nextFloat() /2);
		MAXAGE += rand.nextFloat();
	}

	public void render(ArrayList<Entity> arrayList, SpriteBatch batch) {
		move(arrayList);

		gravity(arrayList);

		sprite.draw(batch);

		AGE += Gdx.graphics.getDeltaTime();
		if (AGE > MAXAGE) {
			dead = true;
		}
	}

	private void move(ArrayList<Entity> arrayList) {
		sprite.translateX(directionX);
		if (testCollision(arrayList)) {
			sprite.translateX(-directionX);
			directionX = 0;
		}
	}

	private void gravity(ArrayList<Entity> objects) {
		sprite.translateY(yVelocity);

		if (testCollision(objects)) {
			sprite.translateY(-yVelocity);
			yVelocity = bounceAmmount;
			if (bounceAmmount > 0) {
				bounceAmmount -= 1;
			}
		}

		yVelocity -= gravity * Gdx.graphics.getDeltaTime();
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
}
