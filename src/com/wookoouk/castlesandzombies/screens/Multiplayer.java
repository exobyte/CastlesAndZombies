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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.wookoouk.castlesandzombies.json.SaveFile;
import com.wookoouk.castlesandzombies.levels.Level;

public class Multiplayer implements Screen {

	// private Game myGame;
	private SpriteBatch batch;
	private Stage stage;
	private TextButtonStyle tbs;
	private Texture logoTexture;
	private Image logo;
	private TextButton startServer;
	private TextButton joinServer;
	private TextButton back;
	private WindowStyle ws;
	private Window window;
	private NinePatchDrawable buttonPatch;
	private TextField username;

	Multiplayer(final Game myGame) {

		int buttonWidth = Gdx.graphics.getWidth() / 2;

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

		// LOGO

		logoTexture = new Texture(Gdx.files.internal("images/logo.png"));
		logo = new Image(logoTexture);
		logo.setScaling(Scaling.fit);

		// USERNAME BOX

		TextFieldStyle fts = new TextFieldStyle(font, Color.WHITE, buttonPatch,
				buttonPatch, buttonPatch);

		username = new TextField("", fts);

		// username.set

		// Start Server
		startServer = new TextButton("Start Server", tbs);
		startServer.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				// TODO Auto-generated method stub
				super.clicked(event, x, y);

				if (!username.getText().isEmpty()) {
					Level level1 = SaveFile.openQuiet("MultiPlayerTest");
					myGame.setScreen(new GameScreen(myGame, level1, myGame
							.getScreen(), true, true, username.getText()));
				}
			}
		});

		// Join Server
		joinServer = new TextButton("Join Server", tbs);
		joinServer.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);

				if (!username.getText().isEmpty()) {
					Level level1 = SaveFile.openQuiet("MultiPlayerTest");
					myGame.setScreen(new GameScreen(myGame, level1, myGame
							.getScreen(), true, false, username.getText()));
				}
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

		float buttonHeight = Gdx.graphics.getHeight() / 8;

		window = new Window("", ws);
		window.setMovable(false);
		window.setFillParent(true);
		window.add(logo).size(buttonWidth, buttonHeight * 2);
		window.row();
		window.add(username).size(buttonWidth, buttonHeight);
		window.row();
		window.add(startServer).size(buttonWidth, buttonHeight);
		window.row();
		window.add(joinServer).size(buttonWidth, buttonHeight);
		window.row();
		window.add(back).size(buttonWidth, buttonHeight);
		;
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
