package com.wookoouk.castlesandzombies.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class SettingsMenu implements Screen {

	// private Game myGame;
	private SpriteBatch batch;
	private Stage stage;
	private TextButtonStyle tbs;
	private Texture logoTexture;
	private Image logo;
	private TextButton back;
	private WindowStyle ws;
	private Window window;
	private NinePatchDrawable buttonPatch;
	private SelectBox resolution;
	private SelectBoxStyle sbStyle;
	private CheckBox checkBox;
	private CheckBoxStyle cbStyle;

	// SETTINGS:
	// resolution
	// input

	SettingsMenu(final Game myGame) {

		int buttonWidth = Gdx.graphics.getWidth() / 2;
		float buttonHeight = Gdx.graphics.getHeight() / 6;
		
		batch = new SpriteBatch();

		BitmapFont font = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		font.setScale(Gdx.graphics.getHeight() / 200);
		font.setUseIntegerPositions(false);

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);

		Gdx.input.setInputProcessor(stage);

		buttonPatch = new NinePatchDrawable(new NinePatch(new Texture(
				Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8));

		tbs = new TextButtonStyle(buttonPatch, buttonPatch, buttonPatch);
		tbs.font = font;

		// Logo

		logoTexture = new Texture(Gdx.files.internal("images/logo.png"));
		logo = new Image(logoTexture);
		logo.setScaling(Scaling.fit);

		// CheckBox

		Texture checkOptions = new Texture(
				Gdx.files.internal("images/checkOptions.png"));
		TextureRegionDrawable off = new TextureRegionDrawable(
				new TextureRegion(checkOptions, 0, 0, 8, 8));
		TextureRegionDrawable on = new TextureRegionDrawable(new TextureRegion(
				checkOptions, 8, 0, 8, 8));
		
		off.setMinHeight(font.getLineHeight());
		off.setMinWidth(font.getLineHeight());
		
		on.setMinHeight(font.getLineHeight());
		on.setMinWidth(font.getLineHeight());

		cbStyle = new CheckBoxStyle(off, on, font, Color.WHITE);

		checkBox = new CheckBox("Fullscreen", cbStyle);
		checkBox.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(),
							Gdx.graphics.getHeight(), false);
				} else {
					Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(),
							Gdx.graphics.getHeight(), true);
				}

			}
		});

		// Pick Resolution

		String[] resList = { "640x480", "800x480", "800x600" };

		sbStyle = new SelectBoxStyle(font, Color.WHITE, buttonPatch,
				buttonPatch, buttonPatch);

		resolution = new SelectBox(resList, sbStyle);
		
		resolution.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String[] newRes = resolution.getSelection().split("x");
				
				int width = Integer.parseInt(newRes[0]);
				int height = Integer.parseInt(newRes[1]);
				
				Gdx.graphics.setDisplayMode(width, height, checkBox.isChecked());
				
			}
		});

		// Back to main menu
		back = new TextButton("Back to Main Menu", tbs);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Screen next = new MainMenu(myGame);
				myGame.setScreen(next);
			}
		});

		ws = new WindowStyle(new BitmapFont(), Color.WHITE, buttonPatch);


		window = new Window("", ws);
		window.setMovable(false);
		window.setFillParent(true);
		window.add(logo).size(buttonWidth, buttonHeight);
		window.row();
		window.add(resolution).size(buttonWidth, buttonHeight);
		window.row();
		window.add(checkBox).size(buttonWidth, buttonHeight);
		window.row();
		window.add(back).size(buttonWidth, buttonHeight);
		stage.addActor(window);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		stage.act(delta);
		stage.draw();

		batch.end();

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
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
		batch.dispose();
		stage.dispose();
		logoTexture.dispose();
	}

}
