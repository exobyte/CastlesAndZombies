package com.wookoouk.castlesandzombies.json;


public class JsonEntity {

	private String name;
	private float X;
	private float Y;
	private boolean solid;
	public String signText;
	
	public JsonEntity(float X, float Y, boolean solid, String name) {
		this.name = name;
		this.X = X;
		this.Y = Y;
		this.solid = solid;
	}

	public void setSignText(String signText){
		this.signText = signText;
	}
	
	public String getSignText(){
		return signText;
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

	public Object getSolid() {
		return solid;
	}
}
