package com.wookoouk.castlesandzombies.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import javax.swing.JFileChooser;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.wookoouk.castlesandzombies.levels.*;

public class SaveFile {

	public static void save(Level level) {
		Gson gson = new Gson();

		JsonLevel fakeLevel = new JsonLevel();

		
		fakeLevel.setMobs(level.getMobs());
		fakeLevel.setEntitys(level.getEntities());
		fakeLevel.setStartPoint(level.getStartPoint());
//		fakeLevel.setLights(level.getLights());

		String json = gson.toJson(fakeLevel);

		final JFileChooser fc = new JFileChooser();
		fc.showDialog(fc, "Save Custom Level");

		String theFileToSave = null;

		if (fc.getSelectedFile() != null) {
			theFileToSave = fc.getSelectedFile().toString();
			System.out.println(theFileToSave);

			FileWriter writer;
			
			try {
				if(theFileToSave.endsWith(".level")){
					writer = new FileWriter(theFileToSave);
				} else {
					writer = new FileWriter(theFileToSave + ".level");
				}
				writer.write(json);
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static Level openQuiet(String levelName) {
		Gson gson = new Gson();

		JsonLevel obj = null;

		Reader b = Gdx.files.internal("levels/" + levelName + ".level")
				.reader();

		obj = gson.fromJson(b, JsonLevel.class);

		Level l = new Level();

		l.setMobs(obj.getMobs());
		l.addEntities(obj.getEntities());
		l.setStartPoint(obj.getStartPoint());
//		l.setLights(obj.getLights());

		return l;

	}

	public static Level open() {

		Gson gson = new Gson();

		JsonLevel obj = null;

		final JFileChooser fc = new JFileChooser();
		fc.showDialog(fc, "Open Custom Level");

		String theFileToOpen = null;

		if (fc.getSelectedFile() != null) {
			theFileToOpen = fc.getSelectedFile().toString();
			System.out.println(theFileToOpen);

			try {

				BufferedReader br = new BufferedReader(new FileReader(
						theFileToOpen));

				obj = gson.fromJson(br, JsonLevel.class);

			} catch (IOException e) {
				e.printStackTrace();
			}

			Level l = new Level();

			l.setMobs(obj.getMobs());
			l.addEntities(obj.getEntities());
			l.setStartPoint(obj.getStartPoint());

			return l;
		}
		return null;
	}

}
