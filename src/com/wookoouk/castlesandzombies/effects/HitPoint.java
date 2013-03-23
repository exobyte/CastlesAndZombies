package com.wookoouk.castlesandzombies.effects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HitPoint {

	private TextureRegion tr;
	private int WIDTH = 4;
	private Sprite sprite;
	private float SCALE = 0.5f;
	private final float gravity = 2; // TODO
	private float yVelocity = 0.5f;
	private float xVelocity = 1;
	private Random rand;
	private float transparency = 1;
	private boolean dead;
	private Color damageColor;

	public HitPoint(int hurt, float x, float y, Color damageColor) {
		
		rand = new Random();
		this.damageColor = damageColor;
		
		Texture texture = new Texture(Gdx.files.internal("images/numbers.png"));
		tr = new TextureRegion(texture);
		tr.setRegion(WIDTH * hurt, 0, WIDTH, texture.getHeight());
		sprite = new Sprite(tr);
		sprite.setColor(damageColor);
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		sprite.setPosition(x, y);
		xVelocity = (rand.nextFloat() - rand.nextFloat())/2;
		yVelocity += (rand.nextFloat()/5);
		
	}

	public void update() {
		yVelocity -= gravity * Gdx.graphics.getDeltaTime();
		sprite.translateY(yVelocity);
		sprite.translateX(xVelocity);
		sprite.setColor(damageColor.r, damageColor.g, damageColor.b, transparency);
		transparency -= Gdx.graphics.getDeltaTime();
		if(transparency < 0){
			dead = true;
		}
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public boolean isDead(){
		return dead;
	}
	
}
