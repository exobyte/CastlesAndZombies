package com.wookoouk.castlesandzombies;

import com.badlogic.gdx.Game;
import com.wookoouk.castlesandzombies.screens.*;

public class MyGdxGame extends Game {

	public static final short CATEGORY_PLAYER = 0x0001;  // 0000000000000001 in binary
	public static final short CATEGORY_MONSTER = 0x0002; // 0000000000000010 in binary
	public static final short CATEGORY_BLOCK = 0x0004; // 0000000000000100 in binary
	public static final short CATEGORY_PROJECTILE = 0x0004; // 0000000000000100 in binary
	public static final short MASK_PLAYER = CATEGORY_BLOCK; // or ~CATEGORY_PLAYER
	public static final short MASK_MONSTER = CATEGORY_BLOCK; // or ~CATEGORY_MONSTER
	public static final short MASK_PROJECTILE = CATEGORY_BLOCK; // or ~CATEGORY_MONSTER
	public static final short MASK_BLOCK = -1; // or ~CATEGORY_MONSTER

	@Override
	public void create() {


		
		this.setScreen(new AnimatedSplash(this));
	}

}
