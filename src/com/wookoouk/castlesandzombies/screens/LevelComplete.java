package com.wookoouk.castlesandzombies.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public class LevelComplete extends Stage {

	NinePatchDrawable buttonPatch;
	TextButtonStyle tbs;
	TextButton continueButton;

	
	
	public LevelComplete(int width, int height, boolean keepAspect, final Game myGame) {
		super(width, height, keepAspect);

		BitmapFont font = new BitmapFont(Gdx.files.internal("test.fnt"),
				Gdx.files.internal("test.png"), false);
		font.setScale(Gdx.graphics.getHeight() / 200);
		font.setUseIntegerPositions(false);

		int buttonWidth = Gdx.graphics.getWidth() / 2;
		float buttonHeight = Gdx.graphics.getHeight() / 8;

		buttonPatch = new NinePatchDrawable(new NinePatch(new Texture(
				Gdx.files.internal("images/menuskin.png")), 8, 8, 8, 8));

		tbs = new TextButtonStyle(buttonPatch, buttonPatch, buttonPatch);
		tbs.font = font;

		continueButton = new TextButton("Next Level", tbs);
		continueButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				
				//TODO go to next level
				//TODO demo: return to title
				myGame.setScreen(new MainMenu(myGame));
			}
		});

		
		
		LabelStyle ls = new LabelStyle(font, Color.WHITE);
		
		Label l = new Label("Level Complete", ls);
		
		Table t = new Table();
		
		t.setFillParent(true);
		t.add(l).size(buttonWidth, buttonHeight);
		t.row();
		t.add(continueButton).size(buttonWidth, buttonHeight);

		this.addActor(t);

	}

}
