package com.wookoouk.castlesandzombies.server;

public class DisconnectPlayer {

	private String ID;

	public DisconnectPlayer (){}
	
	DisconnectPlayer(String ID) {
		this.ID = ID;
	}

	public String getID() {
		return ID;
	}
}
