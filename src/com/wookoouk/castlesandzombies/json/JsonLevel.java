package com.wookoouk.castlesandzombies.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.mobs.*;

public class JsonLevel {

	ArrayList<JsonMob> mobs;
	ArrayList<JsonEntity> entities;
	JsonStartPoint startPoint;

	JsonLevel() {
		mobs = new ArrayList<JsonMob>();
		entities = new ArrayList<JsonEntity>();
	}

	public void setMobs(ArrayList<Mob> realMobs) {

		for (Mob m : realMobs) {

			JsonMob jm = new JsonMob(m.getX(), m.getY(), m.getClass().getName());
			jm.setID(m.getID());
			mobs.add(jm);

		}
	}

	public void setEntitys(ArrayList<Entity> realEntities) {
		for (Entity e : realEntities) {

			JsonEntity JE = new JsonEntity(e.getX(), e.getY(), e.isSolid(), e
					.getClass().getName());
			if (e.isSign()) {
				JE.setSignText(e.getSignText());
			}

			entities.add(JE);

		}
	}

	public void setStartPoint(StartPoint startPoint) {
		this.startPoint = new JsonStartPoint(startPoint.getX(),
				startPoint.getY());
	}

	ArrayList<Entity> getEntities() {
		ArrayList<Entity> realEntities = new ArrayList<Entity>();

		for (JsonEntity je : this.entities) {

			Entity xyz = null;

			Class<?> cl;
			try {
				cl = Class.forName(je.getName());

				Constructor<?> con = cl.getConstructor(float.class,
						float.class, boolean.class);
				xyz = (Entity) con.newInstance(je.getX(), je.getY(),
						je.getSolid());
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
				xyz.setSignText(je.getSignText());
				System.out.println("Sign Text = " + je.getSignText());
			}
			realEntities.add(xyz);
		}
		return realEntities;
	}

	ArrayList<Mob> getMobs() {
		ArrayList<Mob> realMobs = new ArrayList<Mob>();

		for (JsonMob jm : mobs) {

			Mob xyz = null;

			Class<?> cl;
			try {
				cl = Class.forName(jm.getName());

				Constructor<?> con = cl
						.getConstructor(float.class, float.class);
				xyz = (Mob) con.newInstance(jm.getX(), jm.getY());
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

			xyz.setID(jm.getID());

			realMobs.add(xyz);
		}
		return realMobs;
	}

	public StartPoint getStartPoint() {
		StartPoint sp = new StartPoint(startPoint.getX(), startPoint.getY());
		sp.setPlaced(true);
		return sp;
	}

//	public void setLights(ArrayList<JsonLight> lights) {
//		for (JsonLight l : lights) {
//			fakeLights.add(l);
//		}
//	}
//
//	public ArrayList<JsonLight> getLights() {
//		return fakeLights;
//	}
}
