package com.wookoouk.castlesandzombies.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class StartPoint {

	private Sprite sprite;
	private boolean placed;
	private Color originalColor;
	public int SCALE = 1;

	public StartPoint(float X, float Y) {
		Texture texture = new Texture(
				Gdx.files.internal("images/startPoint.png"));
		sprite = new Sprite(texture);
		sprite.setPosition(X, Y);
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		originalColor = sprite.getColor();
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void setX(float x) {
		sprite.setX(x);
	}

	public void setY(float y) {
		sprite.setY(y);
	}

	public void setPlaced(boolean put){
		placed = put;
	}
	
	public boolean isPlaced() {
		return placed;
	}

	public void setTransparency(float f) {
		sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, f);
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public Rectangle getBox() {
		return sprite.getBoundingRectangle();
	}
	public void setColorRed() {
		sprite.setColor(Color.RED);
	}

	public void clearColor() {
		sprite.setColor(originalColor);
	}

	public float getHeight() {
		// TODO Auto-generated method stub
		return sprite.getHeight();
	}
}
