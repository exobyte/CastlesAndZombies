package com.wookoouk.castlesandzombies.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.wookoouk.castlesandzombies.players.*;

public class Entity {

	protected Sprite sprite;
	protected boolean solid;
	protected Texture texture;
	protected float SCALE = 1;
	private float originalWidth;
	private float originalHeight;;
	private Color originalColor;
	private Color nonSolidColor = new Color(0.4f, 0.4f, 0.4f, 1);
	protected boolean superSolid; // ALWAYS SOLID
	protected boolean backgroundOnly; // cannot collide
	protected boolean item;
	protected boolean sign;
	protected String signText = "Text Not Set";

	public Entity(Entity e) {
		texture = e.texture;
		sprite = new Sprite(texture);
		originalWidth = sprite.getWidth();
		originalHeight = sprite.getHeight();
		sprite.setPosition(e.getX(), e.getY());
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		this.solid = e.isSolid();
		originalColor = sprite.getColor();
		if (!solid) {
			sprite.setColor(nonSolidColor);
		}
	}

	Entity(float X, float Y, boolean solid, String image) {
		texture = new Texture(Gdx.files.internal("images/" + image + ".png"));
		sprite = new Sprite(texture);
		originalWidth = sprite.getWidth();
		originalHeight = sprite.getHeight();
		sprite.setPosition(X, Y);
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);

		originalColor = sprite.getColor();

		this.solid = solid;

		if (!solid) {
			sprite.setColor(nonSolidColor);
		}
	}

	protected void setSCALE(float f) {
		this.SCALE = f;
		sprite.setSize(originalWidth * SCALE, originalHeight * SCALE);
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public Rectangle getBox() {
		return sprite.getBoundingRectangle();
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public boolean isSolid() {
		if (backgroundOnly) {
			return false;
		}
		if (superSolid) {
			return true;
		}
		return solid;
	}

	public void setSolid(boolean bool) {

		if (!backgroundOnly) {

			solid = bool;

			if (bool == true) {
				sprite.setColor(originalColor);
			}

			else {
				sprite.setColor(nonSolidColor);
			}
		}
	}

	public void setX(float X) {
		sprite.setX(X);
	}

	public void setY(float y) {
		sprite.setY(y);
	}

	public void setTransparency(float f) {
		sprite.setColor(sprite.getColor().r, sprite.getColor().g,
				sprite.getColor().b, f);
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public boolean isItem() {
		return item;
	}

	public void applyItem(Player player) {
		player.addAmmo(10);
	}

	public boolean isSign() {
		return sign;
	}

	public void showSign() {

	}

	public String getSignText() {
		return signText;
	}

	public void setSignText(String string) {
		this.signText = string;
	}

}
