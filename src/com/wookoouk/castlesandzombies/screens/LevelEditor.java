package com.wookoouk.castlesandzombies.screens;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.json.*;
import com.wookoouk.castlesandzombies.levels.*;
import com.wookoouk.castlesandzombies.mobs.*;

public class LevelEditor implements Screen, InputProcessor {

	private SpriteBatch batch;

	private OrthographicCamera camera;

	private ArrayList<Entity> entities;
	private ArrayList<Mob> mobs;

	private Entity example;

	private Mob mobExample;

	private StartPoint startPoint;
	private StartPoint startPointExample;

	private int resetPosition = -9000;

	private boolean exampleVisable = true;
	private Boolean exampleSelceted = true;

	private boolean exampleMobVisable = false;
	private Boolean exampleMobSelceted = false;

	private boolean startPointSelected = false;
	private boolean startPointVisable = false;

	private Game myGame;
	private Screen previousScreen;

	private int camMoveSpeed = 1;

	private boolean workingInForground = true;

	private BitmapFont text;

	private String currentTool = "Crate";

	private Rectangle mouseRec;

	private int toolNum = 2; // HAS TO BE SET TO DEFAULT TOOL (CRATE)
	private int amountOfTools = 22;
	
	private int roundNumber = 2;

	LevelEditor(Game myGame, Screen previousScreen) {

		this.myGame = myGame;

		Gdx.input.setInputProcessor(this);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w / 10, h / 10);

		this.previousScreen = previousScreen;

		batch = new SpriteBatch();

		entities = new ArrayList<Entity>();
		mobs = new ArrayList<Mob>();

		text = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		text.setUseIntegerPositions(false);
		text.setScale(0.3f);

		mouseRec = new Rectangle(0, 0, 0.1f, 0.1f);

		example = new Crate(resetPosition, resetPosition, workingInForground);

		mobExample = new Zombie(resetPosition, resetPosition);

		startPoint = new StartPoint(resetPosition, resetPosition);
		startPointExample = new StartPoint(resetPosition, resetPosition);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		camControls();

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		renderBackgroundEntities();

		if (exampleVisable && !example.isSolid()) {
			example.render(batch);
		}

		renderForgroundEntities();

		for (Mob m : mobs) {
			m.render(batch);
		}

		if (startPoint.isPlaced()) {
			startPoint.render(batch);
		}

		if (startPointVisable) {
			startPointExample.render(batch);
		}

		if (exampleMobVisable) {
			if (checkMobBoxCollision(mobExample)) {
				mobExample.setColorRed();
			} else {
				mobExample.clearColor();
			}
			mobExample.render(batch);
		}

		if (exampleVisable && example.isSolid()) {
			example.render(batch);
		}

		// LAST

		String foreground = "background";
		if (workingInForground) {
			foreground = "forground";
		}

		text.draw(batch, foreground, camera.position.x
				- (camera.viewportWidth / 2),
				camera.position.y - text.getBounds(foreground).height
						+ (camera.viewportHeight / 2));

		text.draw(batch, currentTool, camera.position.x
				- (camera.viewportWidth / 2), camera.position.y
				+ (camera.viewportHeight / 2));

		batch.end();
	}

	private void renderBackgroundEntities() {
		for (Entity c : entities) {
			if (!c.isSolid()) {
				c.render(batch);
			}
		}

	}

	private void renderForgroundEntities() {
		for (Entity c : entities) {

			if (c.isSolid()) {
				c.render(batch);
			}
		}

	}

	private void camControls() {
		if (Gdx.input.isKeyPressed(Keys.W)) {
			System.out.println("move UP");
			camera.translate(0, camMoveSpeed);

		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			camera.translate(-camMoveSpeed, 0);
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			camera.translate(0, -camMoveSpeed);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			camera.translate(camMoveSpeed, 0);
		}
	}

	public void genLevel() {

		if (startPoint.isPlaced()) {
			Level level = new Level();
			level.addEntities(entities);
			level.setStartPoint(startPoint);
			level.setMobs(mobs);
			GameScreen gs = new GameScreen(myGame, level, myGame.getScreen(),
					false, false, null);
			myGame.setScreen(gs);

		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
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

	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.TAB) {
			if (workingInForground) {
				workingInForground = false;
				System.out.println("Working in back");
			} else {
				workingInForground = true;
				System.out.println("Working in front");
			}
		}

		if (keycode == Keys.ESCAPE) {
			myGame.setScreen(previousScreen);
		}

		if (keycode == Keys.ENTER) {
			genLevel();
		}

		if (keycode == Keys.F1) {
			save();
		}
		if (keycode == Keys.F2) {
			open();
		}
		return false;
	}

	private void nextTool() {

		if (toolNum == amountOfTools) {
			toolNum = 1;
		} else {
			toolNum++;
		}
		getTool();
	}

	private void previousTool() {

		if (toolNum == 1) {
			toolNum = amountOfTools;
		} else {
			toolNum--;
		}
		getTool();
	}

	private void getTool() {
		if (toolNum == 1) {
			exampleSelceted = false;
			startPointSelected = true;
			exampleMobSelceted = false;
			currentTool = "Start Point";
		}

		if (toolNum == 2) {
			example = new Crate(example.getX(), example.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Crate";
		}

		if (toolNum == 3) {
			example = new Plank(example.getX(), example.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Plank";
		}

		if (toolNum == 4) {
			mobExample = new Zombie(mobExample.getX(), mobExample.getY());
			exampleMobSelceted = true;
			exampleSelceted = false;
			startPointSelected = false;
			currentTool = "Zombie";

		}

		if (toolNum == 5) {
			mobExample = new Skeleton(mobExample.getX(), mobExample.getY());
			exampleMobSelceted = true;
			exampleSelceted = false;
			startPointSelected = false;
			currentTool = "Skeleton";

		}

		if (toolNum == 6) {
			example = new Grass(example.getX(), example.getY(), false);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Grass";

		}

		if (toolNum == 7) {
			example = new Wood(example.getX(), example.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Wood";

		}

		if (toolNum == 8) {
			mobExample = new Blob(mobExample.getX(), mobExample.getY());
			exampleMobSelceted = true;
			exampleSelceted = false;
			startPointSelected = false;
			currentTool = "Blob";

		}

		if (toolNum == 9) {
			mobExample = new Devil(mobExample.getX(), mobExample.getY());
			exampleMobSelceted = true;
			exampleSelceted = false;
			startPointSelected = false;
			currentTool = "Devil";

		}

		if (toolNum == 10) {
			mobExample = new FireGhost(mobExample.getX(), mobExample.getY());
			exampleMobSelceted = true;
			exampleSelceted = false;
			startPointSelected = false;
			currentTool = "FireGhost";

		}
		if (toolNum == 11) {
			example = new AppleTree(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "AppleTree";
		}

		if (toolNum == 12) {
			example = new Cactus1(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Cactus 1";
		}
		if (toolNum == 13) {
			example = new Cactus2(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Cactus 2";
		}
		if (toolNum == 14) {
			example = new Cactus3(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Cactus 3";
		}
		if (toolNum == 15) {
			example = new Grave(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Grave";
		}
		if (toolNum == 16) {
			example = new PineTree(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "PineTree";
		}
		if (toolNum == 17) {
			example = new Stone(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Stone";
		}
		if (toolNum == 18) {
			example = new Trellis(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Trellis";
		}
		if (toolNum == 19) {
			example = new Heart(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Heart";
		}
		if (toolNum == 20) {
			example = new Ammo(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Ammo";
		}
		if (toolNum == 21) {
			example = new Sign(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Sign";
		}
		if (toolNum == 22) {
			example = new Light(mobExample.getX(), mobExample.getY(),
					workingInForground);
			exampleSelceted = true;
			startPointSelected = false;
			exampleMobSelceted = false;
			currentTool = "Light";
		}
	}

	private void open() {
		Level l = SaveFile.open();

		if (l != null) {
			entities = l.getEntities();
			mobs = l.getMobs();
			startPoint = l.getStartPoint();
		}

	}

	private void save() {
		Level level = new Level();
		level.addEntities(entities);
		level.setStartPoint(startPoint);
		level.setMobs(mobs);

		SaveFile.save(level);
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {

		if (button == 0) {

			Vector3 mouse = snapToGrid();

			if (exampleMobSelceted) {
				exampleMobVisable = true;
				mobExample.setX(mouse.x - (mobExample.getWidth() / 2));
				mobExample.setY(mouse.y- (mobExample.getHeight() / 2));
			}

			if (exampleSelceted) {
				exampleVisable = true;
				example.setX(mouse.x - (example.getWidth() / 2));
				example.setY(mouse.y - (example.getHeight() / 2));
			}

			if (startPointSelected) {
				startPoint.setPlaced(false);
				startPointVisable = true;
				startPointExample.setX(mouse.x
						- (startPointExample.getWidth() / 2));
				startPointExample.setY(mouse.y - (startPointExample.getHeight() / 2));
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {

		Vector3 mouse = snapToGrid();

		if (button == 1) {
			mouseRec.setX(mouse.x);
			mouseRec.setY(mouse.y);
			removeObject();
		} else {

			if (exampleSelceted) {

				Entity xyz = null;
				Class<?> cl;
				try {
					cl = Class.forName(example.getClass().getName());

					Constructor<?> con = cl.getConstructor(float.class,
							float.class, boolean.class);
					xyz = (Entity) con.newInstance(example.getX(),
							example.getY(), example.isSolid());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

				entities.add((xyz));
				exampleVisable = false;
				example.setX(resetPosition);
				example.setY(resetPosition);
			}

			if (exampleMobSelceted) {
				if (!checkMobBoxCollision(mobExample)) {

					Mob xyz = null;
					Class<?> cl;
					try {
						cl = Class.forName(mobExample.getClass().getName());

						Constructor<?> con = cl.getConstructor(float.class,
								float.class);
						xyz = (Mob) con.newInstance(mobExample.getX(),
								mobExample.getY());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

					mobs.add(xyz);

				}

				exampleMobVisable = false;
				mobExample.setX(resetPosition);
				mobExample.setY(resetPosition);
			}

			if (startPointSelected) {
				startPointVisable = false;
				startPoint.setX(startPointExample.getX());
				startPoint.setY(startPointExample.getY());
				startPoint.setPlaced(true);
				startPointExample.setX(resetPosition);
				startPointExample.setY(resetPosition);
			}

		}
		return false;
	}

	private boolean checkMobBoxCollision(Mob single) {

		for (Entity e : entities) {
			if (e.isSolid() && single.getRec().overlaps(e.getBox())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {

		Vector3 mouse = snapToGrid();
		if (exampleSelceted) {
			setExampleColor();
			example.setX(mouse.x - (example.getWidth() / 2));
			example.setY(mouse.y - (example.getHeight() / 2));
		}

		if (exampleMobSelceted) {
			mobExample.setX(mouse.x - (mobExample.getWidth() / 2));
			mobExample.setY(mouse.y - (mobExample.getHeight() / 2));
		}

		if (startPointSelected) {
			startPointExample
					.setX(mouse.x - (startPointExample.getWidth() / 2));
			startPointExample.setY(mouse.y
					- (startPointExample.getHeight() / 2));
		}

		return false;
	}

	private void setExampleColor() {
		if (workingInForground && !example.isSolid()) {
			example.setSolid(true);
		}
		if (!workingInForground && example.isSolid()) {
			example.setSolid(false);
		}
	}

	private Vector3 snapToGrid() {

		Vector3 mouse = new Vector3();
		camera.unproject(mouse.set(Gdx.input.getX(), Gdx.input.getY(), 1));

		mouse.x = round(Math.round(mouse.x));

		mouse.y = round(Math.round(mouse.y));

		return mouse;
	}

	int round(int num) {

		int temp = num % roundNumber;
		if (temp < (roundNumber / 2))
			return num - temp;
		else
			return num + roundNumber - temp;
	}

	@Override
	public boolean scrolled(int amount) {

		if (amount < 0) {
			previousTool();
		}

		if (amount > 0) {
			nextTool();
		}

		return false;
	}

	public void removeObject() {
		for (Iterator<Mob> mbs = mobs.iterator(); mbs.hasNext();) {
			Mob m = mbs.next();
			if (m.getBody().overlaps(mouseRec)) {
				mbs.remove();
			}
		}

		for (Iterator<Entity> mbs = entities.iterator(); mbs.hasNext();) {
			Entity m = mbs.next();
			if (m.getBox().overlaps(mouseRec)) {
				mbs.remove();
			}
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

}
