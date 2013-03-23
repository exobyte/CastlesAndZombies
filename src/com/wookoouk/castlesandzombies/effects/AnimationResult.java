package com.wookoouk.castlesandzombies.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationResult {

	private int FRAME_COLS;
	private int FRAME_ROWS;
	private Animation walkAnimation;
	private Texture walkSheet;
	private TextureRegion[] walkFrames;
	private int width;
	private int height;
	private float animationSpeed = 0.1f;
	
	public AnimationResult(int FRAME_COLS, int FRAME_ROWS,int width, int height, String file, float animationSpeed){
		this.FRAME_COLS = FRAME_COLS;
		this.FRAME_ROWS = FRAME_ROWS;
		walkSheet = new Texture(Gdx.files.internal("images/"+file+".png"));
		this.width = width;
		this.height = height;
		this.animationSpeed = animationSpeed;
	}
	
	
	public Animation gen(){
		
		TextureRegion[][] tmp = TextureRegion.split(walkSheet, width, height);
				
		walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		walkAnimation = new Animation(animationSpeed, walkFrames);
		
		return walkAnimation;
	}
	
}
