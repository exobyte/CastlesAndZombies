package com.wookoouk.castlesandzombies.json;


public class JsonMob {


	private String name;
	private float X;
	private float Y;
	private String ID;
	
	public JsonMob(float X, float Y, String name) {
		this.name = name;
		this.X = X;
		this.Y = Y;
		
	}
	public String getName() {
		return name;
	}

	public float getX() {
		return X;
	}

	public float getY() {
		return Y;
	}
	public void setID(String ID) {
		this.ID = ID;
	}
	public String getID() {
		return ID;
	}
}
