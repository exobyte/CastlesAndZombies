package com.wookoouk.castlesandzombies.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenDamage {

	Texture texture;
	Sprite sprite;

	public ScreenDamage() {
		texture = new Texture(Gdx.files.internal("images/screenDamage.png"));
		sprite = new Sprite(texture);
		sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void render(float x, float y, SpriteBatch batch) {
		sprite.setPosition(x-(Gdx.graphics.getWidth()/2), y-(Gdx.graphics.getHeight()/2));
		sprite.draw(batch);
	}

	public void setTransparency(float a) {
		sprite.setColor(sprite.getColor().r, sprite.getColor().g, sprite.getColor().b, a);
	}

	public void setImage(String name) {
		sprite.setTexture(new Texture(Gdx.files.internal("images/"+name+".png")));
	}

}
