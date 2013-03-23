package com.wookoouk.castlesandzombies.effects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RainParticle {

	Texture texture;
	Sprite sprite;
	Random rand;

	final static float width = 1;
	final static float height = 50;

	final static int maxScale = 8;

	public RainParticle(float x) {
		texture = new Texture(Gdx.files.internal("images/whiteSquare.png"));
		// texture = new Texture(Gdx.files.internal("images/icon32.png"));
		sprite = new Sprite(texture);
		sprite.setColor(Color.BLACK);
		rand = new Random();
		float scale = rand.nextInt(maxScale);
		sprite.setSize(width * scale, height * scale);
		sprite.setPosition(x, Gdx.graphics.getHeight() + sprite.getHeight());
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
		update();
	}

	private void update() {
		sprite.translateY(-sprite.getHeight() / 5);
	}

	public boolean notVisible() {
		if (sprite.getY() + sprite.getHeight() < 0) {
			return true;
		}
		return false;
	}

}
