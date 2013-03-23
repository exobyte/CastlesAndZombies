package com.wookoouk.castlesandzombies.levels;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.json.*;
import com.wookoouk.castlesandzombies.mobs.*;

public class Level {

	ArrayList<Entity> entities;
	StartPoint startPoint;
	ArrayList<Mob> mobs;
	ArrayList<JsonLight> lights;

	// public void loadFromSave() {
	//
	// Level l = SaveFile.open();
	//
	// if (l != null) {
	// entities = l.getEntities();
	// mobs = l.getMobs();
	// startPoint = l.getStartPoint();
	// }
	// }

	public void processLights() {

		lights = new ArrayList<JsonLight>();

		for (Iterator<Entity> it1 = entities.iterator(); it1.hasNext();) {
			Entity e = it1.next();

			if (e.getClass().getName().endsWith("Light")) {
				lights.add(new JsonLight(e.getX() + (e.getWidth() / 2), e
						.getY() + (e.getHeight() / 2)));
				it1.remove();
			}

		}

	}

	public ArrayList<Mob> getMobs() {
		return mobs;
	}

	public StartPoint getStartPoint() {
		return startPoint;
	}

	public void addEntities(ArrayList<Entity> entities) {
		this.entities = entities;

		this.entities = new ArrayList<Entity>(entities.size());
		for (Entity entity : entities) {

			Entity xyz = null;
			Class<?> cl;

			try {
				cl = Class.forName(entity.getClass().getName());

				Constructor<?> con = cl.getConstructor(float.class,
						float.class, boolean.class);
				xyz = (Entity) con.newInstance(entity.getX(), entity.getY(),
						entity.isSolid());
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

			if (xyz.isSign()) {
				xyz.setSignText(entity.getSignText());
			}

			this.entities.add(xyz);
		}
		for (Entity e : entities) {
			System.out.println(e.getClass().getName());
		}

		// processLights();
	}

	public void setStartPoint(StartPoint startPointIN) {
		this.startPoint = startPointIN;
	}

	public void setMobs(ArrayList<Mob> mobs) {

		this.mobs = mobs;

		this.mobs = new ArrayList<Mob>(mobs.size());
		for (Mob mob : mobs) {

			Mob xyz = null;
			Class<?> cl;

			try {
				cl = Class.forName(mob.getClass().getName());

				Constructor<?> con = cl
						.getConstructor(float.class, float.class);
				xyz = (Mob) con.newInstance(mob.getX(), mob.getY());
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

			xyz.setID(mob.getID());

			this.mobs.add(xyz);
		}

	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public void renderBackground(SpriteBatch batch, OrthographicCamera camera) {

		for (Entity c : entities) {
			if (isVisable(c, camera)) {
				if (!c.isSolid()) {
					c.render(batch);
				}
			}
		}
	}

	private boolean isVisable(Entity sprite, OrthographicCamera camera) {

		float width = camera.viewportWidth;
		float height = camera.viewportHeight;
		
		float hWidth = width/2;
		float hHeight = height/2;

		float x = camera.position.x - hWidth;
		float y = camera.position.y - hHeight;

		Rectangle camRec = new Rectangle(x, y, width, height);

		if (camRec.overlaps(sprite.getBox())) {
			return true;
		}

		return false;
	}

	public void renderForeground(SpriteBatch batch, OrthographicCamera camera) {

		for (Entity c : entities) {
			if (isVisable(c, camera)) {
				if (c.isSolid()) {
					c.render(batch);
				}
			}
		}
	}

	public ArrayList<JsonLight> getLights() {
		return lights;
	}
	//
	// public void setLights(ArrayList<JsonLight> lights2) {
	// lights = lights2;
	// }

}
