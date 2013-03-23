package com.wookoouk.castlesandzombies.screens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wookoouk.castlesandzombies.MyGdxGame;
import com.wookoouk.castlesandzombies.effects.RainParticle;

public class AnimatedSplash implements Screen, InputProcessor {

	ArrayList<RainParticle> rain;
	Game myGdxGame;
	SpriteBatch batch;
	Random rand;
	BitmapFont text;
	boolean finished;
	float textTrans = 1f;
	Music introRain;

	final static int initalDroplets = 60;

	public AnimatedSplash(MyGdxGame myGdxGame) {
		this.myGdxGame = myGdxGame;
		rain = new ArrayList<RainParticle>();
		batch = new SpriteBatch();
		rand = new Random();

		text = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		text.setColor(new Color(0, 0, 0, textTrans));
		text.setScale(10);

		introRain = Gdx.audio.newMusic(Gdx.files
				.internal("sounds/introRain.wav"));
		introRain.setLooping(true);
		introRain.play();
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// if (!finished) {
		// myGdxGame.setScreen(new MainMenu(myGdxGame));
		// }
		finished = true;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// if (!finished) {
		// myGdxGame.setScreen(new MainMenu(myGdxGame));
		// }
		finished = true;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();

		TextBounds bounds = text.getBounds("WookooUK");
		float cx = (Gdx.graphics.getWidth() / 2) - (bounds.width / 2);
		float cy = (Gdx.graphics.getHeight() / 2) + (bounds.height / 2);
		text.draw(batch, "WookooUK", cx, cy);

		float newX = rand.nextInt(Gdx.graphics.getWidth());

		if (!finished) {
			rain.add(new RainParticle(newX));
		} else {

			if (textTrans <= 0f) {
				myGdxGame.setScreen(new MainMenu(myGdxGame));
			}
			introRain.setVolume(textTrans);
			textTrans -= 0.01f;
			text.setColor(new Color(0, 0, 0, textTrans));
		}

		for (Iterator<RainParticle> it1 = rain.iterator(); it1.hasNext();) {
			RainParticle rp = it1.next();

			if (rp.notVisible()) {
				it1.remove();
			}

			rp.render(batch);
		}

		batch.end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		introRain.dispose();
		batch.dispose();
		text.dispose();
	}

}
