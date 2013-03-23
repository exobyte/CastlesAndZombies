package com.wookoouk.castlesandzombies.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.wookoouk.castlesandzombies.json.*;
import com.wookoouk.castlesandzombies.levels.*;

public class MainMenu implements Screen {

	// private Game myGame;
	private SpriteBatch batch;
	private Stage stage;
	private TextButtonStyle tbs;
	private Texture logoTexture;
	private Image logo;
	private TextButton newGame;
	private TextButton loadGame;
	private TextButton multiplayer;
	private TextButton levelEditor;
	private TextButton settings;
	private TextButton exit;
	private WindowStyle ws;
	private Window window;
	private NinePatchDrawable buttonPatch;

	MainMenu(final Game myGame) {

		int buttonWidth = Gdx.graphics.getWidth() / 2;

		batch = new SpriteBatch();

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);

		Gdx.input.setInputProcessor(stage);

		buttonPatch = new NinePatchDrawable(new NinePatch(new Texture(
				Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8));

		tbs = new TextButtonStyle(buttonPatch, buttonPatch, buttonPatch);
		BitmapFont font = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		font.setScale(Gdx.graphics.getHeight()/200);
		font.setUseIntegerPositions(false);
		tbs.font = font;

		// Logo

		logoTexture = new Texture(Gdx.files.internal("images/logo.png"));
		logo = new Image(logoTexture);
		logo.setScaling(Scaling.fit);

		// New Game
		newGame = new TextButton("New Game", tbs);
		newGame.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Level level1 = SaveFile.openQuiet("level1");
				myGame.setScreen(new GameScreen(myGame, level1, myGame
						.getScreen(), false, false, null));
			}
		});

		// Load Game
		loadGame = new TextButton("Load Game", tbs);
		loadGame.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Level l = SaveFile.open();
				if (l != null) {
					myGame.setScreen(new GameScreen(myGame, l, myGame
							.getScreen(), false, false, null));
				}
			}
		});

		// MultiPlayer Game
		multiplayer = new TextButton("Multiplayer", tbs);
		multiplayer.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.setScreen(new Multiplayer(myGame)); // TODO
			}
		});

		// Level Editor
		levelEditor = new TextButton("Level Editor", tbs);
		levelEditor.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.setScreen(new LevelEditor(myGame, myGame.getScreen()));
			}
		});

		// Settings
		settings = new TextButton("Settings", tbs);
		settings.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				myGame.setScreen(new SettingsMenu(myGame));
			}
		});

		// Exit Game
		exit = new TextButton("Exit Game", tbs);
		exit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.exit();
			}
		});

		ws = new WindowStyle(new BitmapFont(), Color.WHITE, buttonPatch);
		
		
		window = new Window("", ws);
		window.setMovable(false);
		window.setFillParent(true);
		
		if(Gdx.app.getType().equals(ApplicationType.Desktop)){
			float buttonHeight = Gdx.graphics.getHeight()/10;
			window.add(logo).size(buttonWidth, buttonHeight*2);
			window.row();
			window.add(newGame).size(buttonWidth, buttonHeight);
			window.row();
			window.add(loadGame).size(buttonWidth, buttonHeight);
			window.row();
			window.add(multiplayer).size(buttonWidth, buttonHeight);
			window.row();
			window.add(levelEditor).size(buttonWidth, buttonHeight);
			window.row();
			window.add(settings).size(buttonWidth, buttonHeight);
			window.row();
			window.add(exit).size(buttonWidth, buttonHeight);
		} else {
			float buttonHeight = Gdx.graphics.getHeight()/6;
			window.add(logo).size(buttonWidth, buttonHeight*2);
			window.row();
			window.add(newGame).size(buttonWidth, buttonHeight);
			window.row();
			window.add(exit).size(buttonWidth, buttonHeight);
		}
		
		
		
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
