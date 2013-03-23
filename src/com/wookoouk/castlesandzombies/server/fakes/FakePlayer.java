package com.wookoouk.castlesandzombies.server.fakes;


public class FakePlayer {

	private float x;
	private float y;
	private String ID;
	private boolean facingRight;

	public FakePlayer() {
	};

	public FakePlayer(float x, float y, String string, boolean b) {
		this.x = x;
		this.y = y;
		this.ID = string;
		this.facingRight = b;

	}

	public String getID() {
		return ID;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean facingRight() {
		return facingRight;
	}

	public void setX(float x2) {
x = x2;		
	}

	public void setY(float y2) {
y = y2;		
	}

	public void setDirection(boolean b) {
		// TODO Auto-generated method stub
		facingRight = b;
	}

}
