package com.wookoouk.castlesandzombies.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.wookoouk.castlesandzombies.MyGdxGame;
import com.wookoouk.castlesandzombies.effects.*;
import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.guns.*;
import com.wookoouk.castlesandzombies.json.JsonLight;
import com.wookoouk.castlesandzombies.levels.*;
import com.wookoouk.castlesandzombies.mobs.*;
import com.wookoouk.castlesandzombies.players.*;
import com.wookoouk.castlesandzombies.server.*;
import com.wookoouk.castlesandzombies.server.fakes.*;

public class GameScreen implements Screen, InputProcessor {

	boolean renderBox2DHelper = false;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private static Player player;

	private float fontScale = Gdx.graphics.getHeight() / 3000f;

	private final int camScale = 10;

	private boolean MovingLeft;
	private boolean MovingRight;

	private Sound hitEnemySound;

	private Game myGame;

	private Level level;

	private Screen previousScreen;

	private boolean paused = false;

	private boolean useLights = true;

	// private ScreenDamage screenDamage;
	// private boolean dark = false;
	// private boolean showDamage = false;

	private boolean shooting;

	private Stage pauseMenu;
	private WindowStyle ws;
	private Window window;
	private NinePatchDrawable buttonPatch;
	private TextButtonStyle tbs;
	private TextButton resumeButton;
	private TextButton menuButton;

	private Stage androidButtons;

	public GameServer server;
	public static GameClient client;

	private static ArrayList<Mob> mobs;
	private static ArrayList<Death> deaths;

	private boolean multiplayerGame;
	private boolean isServer;

	private clientUpdater clientUpdateThread;

	private ConeLight torch;

	private World world;
	private Box2DDebugRenderer boxRender;
	private RayHandler lighting;

	private String myUsername;

	private BitmapFont text;

	Stage completeionStage;

	public GameScreen(Game game, final Level level, Screen previousScreen,
			boolean b, boolean c, String string) {

		System.out.println("this is " + b + " a multiplayer game and I am " + c
				+ " the server");

		this.myGame = game;
		this.previousScreen = previousScreen;

		this.level = level;
		this.level.processLights();

		myUsername = string;

		multiplayerGame = b;
		isServer = c;

		text = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		text.setUseIntegerPositions(false);
		text.setScale(fontScale);

		if (isServer && multiplayerGame) {
			try {
				server = new GameServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (multiplayerGame) {
			try {
				client = new GameClient("127.0.0.1"); // TODO IP
			} catch (IOException e) {
			}
			clientUpdateThread = new clientUpdater();
		}

		Gdx.input.setInputProcessor(this);

		// PAUSE MENU

		int buttonWidth = Gdx.graphics.getWidth() / 2;

		pauseMenu = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);

		buttonPatch = new NinePatchDrawable(new NinePatch(new Texture(
				Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8));

		ws = new WindowStyle(new BitmapFont(), Color.WHITE, buttonPatch);

		window = new Window("", ws);

		BitmapFont font = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		font.setUseIntegerPositions(false);
		font.setScale(Gdx.graphics.getHeight() / 200);

		tbs = new TextButtonStyle(buttonPatch, buttonPatch, buttonPatch);
		tbs.font = font;
		// tbs = new TextButtonStyle(buttonPatch, buttonPatch, buttonPatch, 0,
		// 0,
		// 0, 0, new BitmapFont(), Color.WHITE, Color.WHITE, Color.WHITE);

		resumeButton = new TextButton("Resume", tbs);
		resumeButton.setWidth(buttonWidth);
		resumeButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				pause();
			}

		});

		menuButton = new TextButton("Exit to Menu", tbs);
		menuButton.setWidth(buttonWidth);
		menuButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);

				if (multiplayerGame) {
					client.disconnect(player.getID());
					if (isServer) {
						server.disconnect();
					}
				}

				myGame.setScreen(new MainMenu(myGame));
			}
		});

		window.setMovable(false);
		window.setFillParent(true);

		window.add(resumeButton)
				.size(buttonWidth, Gdx.graphics.getHeight() / 3);
		window.row();
		window.add(menuButton).size(buttonWidth, Gdx.graphics.getHeight() / 3);
		pauseMenu.addActor(window);

		//

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w / camScale, h / camScale);
		batch = new SpriteBatch();

		player = new Player(level.getStartPoint().getX(), level.getStartPoint()
				.getY(), myUsername);

		// health = new NinePatch(new Texture(
		// Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8);
		//
		// ammo = new NinePatch(new Texture(
		// Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8);
		//
		// grenadeAmmo = new NinePatch(new Texture(
		// Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8);

		deaths = new ArrayList<Death>();

		// screenDamage = new ScreenDamage();

		hitEnemySound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/hitEnemy.wav"));

		mobs = level.getMobs();

		setupBox2D();

		// TODO TEST

		if (isServer && multiplayerGame) {
			server.initMobs(mobs, world);
		}

		if (multiplayerGame) {
			client.setFakeMobs(mobs);
			clientUpdateThread.start();
			client.pushPlayer(player);
		}

		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			setupAndroidButtons();
		}

		completeionStage = new LevelComplete(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true, myGame);

	}

	private void setupBox2D() {
		world = new World(new Vector2(0, -100), true);
		boxRender = new Box2DDebugRenderer();

		player.getBoxBody(world);

		for (Entity e : level.getEntities()) {

			if (e.isSolid() && !e.isItem() && !e.isSign()) {

				BodyDef playerDef = new BodyDef();
				playerDef.type = BodyType.StaticBody;
				playerDef.position.set(e.getX() + (e.getWidth() / 2), e.getY()
						+ (e.getHeight() / 2));

				Body eBody = world.createBody(playerDef);

				PolygonShape poly = new PolygonShape();
				poly.setAsBox(e.getWidth() / 2, e.getHeight() / 2);

				FixtureDef cFix = new FixtureDef();
				cFix.shape = poly;
				cFix.filter.categoryBits = MyGdxGame.CATEGORY_BLOCK;
				cFix.filter.maskBits = MyGdxGame.MASK_BLOCK;

				eBody.createFixture(cFix);
				poly.dispose();
			}
		}

		for (Mob m : mobs) {
			m.setupBox2D(world);
		}

		if (useLights) {

			lighting = new RayHandler(world);

			lighting.setAmbientLight(0.1f);
			lighting.setCulling(true);

			if (level.getLights().isEmpty()) {
				lighting.setAmbientLight(1);
			} else {

				for (JsonLight tl : level.getLights()) {
					new PointLight(lighting, 500, Color.ORANGE, 50, tl.getX(),
							tl.getY());
				}
			}
			torch = new ConeLight(lighting, 500, Color.ORANGE, 30,
					player.getX(), player.getY(), 0, 50);

		}
	}

	public static class clientUpdater extends Thread {
		public void run() {
			while (true) {

				client.requestMobUpdate();

				client.requestPlayerUpdate();

				// remove dead mobs //TODO
				// for (Iterator<Mob> it1 = mobs.iterator(); it1.hasNext();) {
				// Mob m = it1.next();
				//
				// for (Iterator<FakeMob> fm = client.getDeadMobs().iterator();
				// fm
				// .hasNext();) {
				// FakeMob e = fm.next();
				//
				// if (m.getID().equals(e.getID())) {
				// deaths.add(new Death(m.getX() + (m.getWidth() / 2),
				// m.getY() + (m.getHeight() / 2), m));
				// it1.remove();
				// fm.remove();
				// }
				//
				// }
				//
				// }

				client.pushPlayer(player);

				for (FakeMob fm : client.getFakeMobs()) {
					for (Mob m : mobs) {
						if (m.getID().equals(fm.getID())) {
							m.setX(fm.getX());
							m.setY(fm.getY());
							m.setHealth(fm.getHealth());
							m.setFacingRight(fm.isFacingRight());
						}
					}
				}

				try {
					Thread.currentThread();
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void render(float delta) {

		if (!paused) {
			update();

			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

			camera.setToOrtho(false, Gdx.graphics.getWidth() / camScale,
					Gdx.graphics.getHeight() / camScale);
			camera.position.set(player.getX() + (player.getWidth() / 2),
					player.getY() + (player.getHeight() / 2), 1);
			camera.update();
			batch.setProjectionMatrix(camera.combined);

			float camX = camera.position.x;
			float camY = camera.position.y;
			float camW = camera.viewportWidth / 2;
			float camH = camera.viewportHeight / 2;

			batch.begin();

			level.renderBackground(batch, camera);

			// Render Deaths

			for (Death d : deaths) {
				d.render(batch, level.getEntities());
			}


			level.renderForeground(batch, camera);

			for (Mob m : mobs) {
				m.render(batch);
			}

			player.render(batch);
			batch.end();
			batch.begin();

			if (renderBox2DHelper) {
				boxRender.render(world, camera.combined);
			}

			if (useLights) {

				lighting.setCombinedMatrix(camera.combined);
				lighting.updateAndRender();
			}

			batch.end();
			batch.begin();

			// TODO TEXT

			for (Entity e : level.getEntities()) {

				if (e.getBox().overlaps(player.getRec())) {

					if (e.isItem()) {
						e.applyItem(player);
						level.getEntities().remove(e);
						break;
					}

					if (e.isSign()) {

						text.draw(
								batch,
								e.getSignText(),
								e.getX()
										+ (e.getWidth() / 2)
										- (text.getBounds(e.getSignText()).width / 2),
								e.getY() + (e.getHeight()));
					}
				}
			}

			text.draw(
					batch,
					"Health: " + player.getHealth() + " / "
							+ player.getMaxHealth(), camX - camW, camY + camH
							- text.getBounds(player.getHealth() + "").height);
			text.draw(batch, "Ammo: " + player.getAmmo(), camX - camW, camY
					+ camH - (text.getBounds(player.getAmmo() + "").height * 2));
			text.draw(
					batch,
					"Grenades: " + player.getGrenades(),
					camX - camW,
					camY
							+ camH
							- (text.getBounds(player.getGrenades() + "").height * 3));

			text.draw(
					batch,
					"FPS: " + Gdx.graphics.getFramesPerSecond(),
					camX - camW,
					camY
							+ camH
							- (text.getBounds(Gdx.graphics.getFramesPerSecond()
									+ "").height * 4));

			if (multiplayerGame) {
				for (FakePlayer p : client.getFakePlayers()) {

					System.out.println("CLIENT: " + player.getID());

					if (!p.getID().equals(player.getID())) {

						TextureRegion tr = new TextureRegion(new Texture(
								Gdx.files.internal("images/player.png")), 4, 8);
						Sprite sprite = new Sprite(tr);
						sprite.setX(p.getX());
						sprite.setY(p.getY());

						TextBounds tb = text.getBounds(p.getID());

						text.draw(batch, p.getID(),
								p.getX() + (sprite.getWidth() / 2)
										- (tb.width / 2),
								p.getY() + sprite.getHeight() + (tb.height * 2));

						if (!p.facingRight()) {
							sprite.flip(true, false);
						}

						sprite.draw(batch);

					}
				}
				TextBounds tb = text.getBounds(myUsername);

				text.draw(batch, myUsername, player.getX()
						+ (player.getWidth() / 2) - (tb.width / 2),
						player.getY() + player.getHeight() + (tb.height * 2));
			}

			// Android Controls
			if (Gdx.app.getType().equals(ApplicationType.Android)) {
				batch.end();
				batch.begin();
				androidButtons.act();
				androidButtons.draw();
			}

			batch.end();

		} else {
			pauseMenu.act(delta);
			pauseMenu.draw();
		}

		if (mobs.size() == 0) {

			completeionStage.act();
			completeionStage.draw();
		}
	}

	private void update() {

		List<Contact> contacts = world.getContactList();
		for (int i = 0; i < world.getContactCount(); i++) {
			Contact contact = contacts.get(i);
			contact.resetFriction();
		}

		if (shooting) {
			player.shoot();
		}

		if (isServer && multiplayerGame) {
			server.pushMobUpdate();
		}

		if (!multiplayerGame) {
			for (Mob m : mobs) {
				m.update(player);
			}
		}

		System.out.println(Gdx.graphics.getFramesPerSecond());

		controls();

		world.step(Gdx.graphics.getDeltaTime(), 1, 1);

		bulletCollision();

		// loop through mobs

		for (Iterator<Mob> it1 = mobs.iterator(); it1.hasNext();) {
			Mob m = it1.next();

			if (multiplayerGame) {
				if (m.isFacingRight() && !m.isFlipped()) {
					m.flip(true, false);
				}
				if (!m.isFacingRight() && m.isFlipped()) {
					m.flip(true, false);
				}
			}

			// Player Damage
			if (m.getRec().overlaps(player.getRec()) && player.canBeHurt()) {
				player.damage(m.getAttackPower());
				System.out.println("Player hurt " + player.getHealth());
			}

			// Mob Death

			if (m.isDead()) {
				deaths.add(new Death(m.getX() + (m.getWidth() / 2), m.getY()
						+ (m.getHeight() / 2), m));
				it1.remove();
				world.destroyBody(m.getBox2dBody());
			}

			// Explosions

			for (Iterator<Explosion> ex = player.getExplosions().iterator(); ex
					.hasNext();) {
				Explosion e = ex.next();
				for (Iterator<ExplosionParticle> exp = e.getParticles()
						.iterator(); exp.hasNext();) {
					ExplosionParticle ep = exp.next();

					if (m.getHead().overlaps(ep.getBox())) {
						m.hit(true);
						ep.setDead();
					}
					if (m.getBody().overlaps(ep.getBox())) {
						m.hit(false);
						ep.setDead();
					}
				}
			}

			// Bullets
			for (Iterator<Bullet> bul = player.getBullets().iterator(); bul
					.hasNext();) {
				Bullet b = bul.next();
				if (m.getHead().overlaps(b.getBox())) {
					hitEnemySound.play();
					m.hit(true);
					bul.remove();
				} else if (m.getBody().overlaps(b.getBox())) {
					hitEnemySound.play();
					m.hit(false);
					bul.remove();
				}
			}

		}
		player.update(camera, level.getEntities(), world);

		if (useLights) {
			updateTorch();
		}

		if (mobs.size() == 0) {
			System.out.println("LEVEL COMPLETE");

			if (Gdx.input.getInputProcessor() != completeionStage) {

				shooting = false;
				MovingLeft = false;
				MovingRight = false;

				Gdx.input.setInputProcessor(completeionStage);
			}

		}

	}

	private void updateTorch() {

		if (player.isUsingTorch() && !torch.isActive()) {
			torch.setActive(true);
		}
		if (!player.isUsingTorch() && torch.isActive()) {
			torch.setActive(false);
		}

		float torchPosition = player.getX() + (player.getWidth());
		// float torchDirection = 0;

		if (!player.facingRight()) {
			torchPosition = player.getX();
			// torchDirection = 180;
		}
		torch.setDirection(player.getGunRot());
		torch.setPosition(torchPosition, player.getY()
				+ (player.getHeight() / 2));
	}

	private void bulletCollision() {

		for (Iterator<Entity> rec = level.getEntities().iterator(); rec
				.hasNext();) {
			Entity r = rec.next();
			for (Iterator<Bullet> bul = player.getBullets().iterator(); bul
					.hasNext();) {
				Bullet b = bul.next();
				if (r.isSolid() && !r.isSign() && !r.isItem()
						&& r.getBox().overlaps(b.getBox())) {
					bul.remove();
				}
			}
		}
	}

	private void controls() {
		if (!MovingLeft && !MovingRight) {
			player.setFriction(1000f);
		} else {
			player.setFriction(0.2f);
		}

		if (MovingLeft && !MovingRight) {
			player.moveLeft(level.getEntities());
		}
		if (MovingRight && !MovingLeft) {
			player.moveRight(level.getEntities());
		}

		if (player.isJumping()) {
			player.jump(world);
		}

	}

	@Override
	public void resize(int width, int height) {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(w / camScale, h / camScale);
	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {
		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			if (paused) {
				paused = false;
				resume();
			} else {
				Gdx.input.setInputProcessor(pauseMenu);
				paused = true;
			}
		}
	}

	@Override
	public void resume() {
		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void dispose() {
		world.dispose();
		if (multiplayerGame) {
			client.disconnect(player.getID());
			if (isServer) {
				server.disconnect();
			}
		}

	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.A) {
			MovingLeft = true;
		}

		if (keycode == Keys.D) {
			MovingRight = true;
		}

		if (keycode == Keys.SPACE || keycode == Keys.W) {
			player.setJumping(true);
			player.jump(world);
		}

		if (keycode == Keys.T) {
			player.toggleTorch();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {

		if (keycode == Keys.A) {
			MovingLeft = false;
		}

		if (keycode == Keys.D) {
			MovingRight = false;
		}

		if (keycode == Keys.SPACE || keycode == Keys.W) {
			player.setJumping(false);
			player.jump(world);
		}

		if (keycode == Keys.ESCAPE) {
			if (previousScreen instanceof LevelEditor) {
				myGame.setScreen(previousScreen);
			} else {
				pause();
			}
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		System.out.println(pointer + "  " + button);

		if (button == 0) { // LEFT mouse
			shooting = true;
		}
		if (button == 1) { // RIGHT mouse
			player.throwGrenade(world);
		}

		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (shooting) {
			shooting = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	private void setupAndroidButtons() {
		float camX = camera.position.x;
		float camY = camera.position.y;

		float buttonSize = Gdx.graphics.getHeight() / 4;

		androidButtons = new Stage(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), true);

		Table t = new Table();

		t.setPosition(camX, camY);
		t.setWidth(Gdx.graphics.getWidth());
		t.setHeight(Gdx.graphics.getHeight() / 4);

		NinePatchDrawable leftButtonPatch = new NinePatchDrawable(
				new NinePatch(new Texture(
						Gdx.files.internal("images/leftButton.png")), 8, 8, 8,
						8));

		Button left = new Button(leftButtonPatch);
		left.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				MovingLeft = true;
				player.faceLeft();
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				MovingLeft = false;
				super.touchUp(event, x, y, pointer, button);

			}
		});

		NinePatchDrawable rightButtonPatch = new NinePatchDrawable(
				new NinePatch(new Texture(
						Gdx.files.internal("images/rightButton.png")), 8, 8, 8,
						8));

		Button right = new Button(rightButtonPatch);
		right.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				player.faceRight();
				MovingRight = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				MovingRight = false;
				super.touchUp(event, x, y, pointer, button);

			}
		});

		NinePatchDrawable jumpButtonPatch = new NinePatchDrawable(
				new NinePatch(new Texture(
						Gdx.files.internal("images/jumpButton.png")), 8, 8, 8,
						8));

		Button jump = new Button(jumpButtonPatch);
		jump.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				player.jump(world);
			}
		});

		NinePatchDrawable shootButtonPatch = new NinePatchDrawable(
				new NinePatch(new Texture(
						Gdx.files.internal("images/shootButton.png")), 8, 8, 8,
						8));

		Button shoot = new Button(shootButtonPatch);

		shoot.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				shooting = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				shooting = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});

		NinePatchDrawable grenadeButtonPatch = new NinePatchDrawable(
				new NinePatch(new Texture(
						Gdx.files.internal("images/grenadeButton.png")), 8, 8,
						8, 8));

		Button grenade = new Button(grenadeButtonPatch);

		grenade.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				player.throwGrenade(world);
				return super.touchDown(event, x, y, pointer, button);
			}
		});

		Button padder = new Button(buttonPatch, buttonPatch);
		padder.setVisible(false);

		t.add(left).size(buttonSize);
		t.add(right).size(buttonSize);

		t.add(padder).size(buttonSize);

		t.add(grenade).size(buttonSize);
		t.add(jump).size(buttonSize);
		t.add(shoot).size(buttonSize);
		androidButtons.addActor(t);
		// TODO
		Gdx.input.setInputProcessor(androidButtons);
	}
}
