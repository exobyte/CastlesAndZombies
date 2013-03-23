package com.wookoouk.castlesandzombies.server.fakes;

public class FakeMob {

	private float x;
	private float y;
	private int health;
	private String ID;
	private boolean facingRight;

	public FakeMob(){};
	
	public FakeMob(float x, float y, int health, String string, boolean b) {
		this.x = x;
		this.y = y;
		this.health = health;
		this.ID = string;
		this.facingRight = b;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getID() {
		return ID;
	}

	public boolean isFacingRight(){
		return facingRight;
	}
	
	public void setID(String iD) {
		ID = iD;
	}

	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}

}
