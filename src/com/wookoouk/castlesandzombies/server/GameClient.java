package com.wookoouk.castlesandzombies.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.wookoouk.castlesandzombies.mobs.Mob;
import com.wookoouk.castlesandzombies.players.Player;
import com.wookoouk.castlesandzombies.server.fakes.*;

public class GameClient {

	private static Client client;
//	private String IP;
	// public final int UDP = 54777;

	// private ArrayList<FakeMob> deadMobs;
	private ArrayList<FakeMob> fakeMobs;
	private ArrayList<FakePlayer> fakePlayers;
	public final static int TCP = GameServer.TCP;

	public GameClient(final String IP) throws IOException {
//		this.IP = IP;
		client = new Client();

		Kryo kryo = client.getKryo();
		kryo.register(FakeMob.class);
		kryo.register(RequestMobUpdate.class);
		kryo.register(RequestPlayerUpdate.class);
		kryo.register(FakePlayer.class);
		kryo.register(DisconnectPlayer.class);

		Log.set(Log.LEVEL_NONE);

		fakeMobs = new ArrayList<FakeMob>();
		fakePlayers = new ArrayList<FakePlayer>();
		// deadMobs = new ArrayList<FakeMob>();

		client.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof FakeMob) {

					FakeMob ob = (FakeMob) object;

					for (Iterator<FakeMob> fm = fakeMobs.iterator(); fm
							.hasNext();) {
						FakeMob e = fm.next();

						if (e.getID().equals(ob.getID())) {
							e.setX(ob.getX());
							e.setY(ob.getY());
							e.setFacingRight(ob.isFacingRight());
						}
					}
				}

				if (object instanceof FakePlayer) {

					FakePlayer p = (FakePlayer) object;

					System.out.println("SERVER: " + p.getID());

					boolean newPlayer = false;

					for (FakePlayer pla : fakePlayers) {
						if (pla.getID().equals(p.getID())) {

							pla.setX(p.getX());
							pla.setY(p.getY());
							pla.setDirection(p.facingRight());
							newPlayer = true;
						}
					}

					if (newPlayer == false) {
						fakePlayers.add(p);
					}
				}
				if (object instanceof DisconnectPlayer) {

					DisconnectPlayer quitter = (DisconnectPlayer) object;

					for (Iterator<FakePlayer> itr = fakePlayers.iterator(); itr
							.hasNext();) {
						FakePlayer dp = itr.next();
						if (dp.getID().equals(quitter.getID())) {
							System.out.println("CLIENT REMOVING: "+quitter.getID());
							itr.remove();
						}
					}
				}
			}

		});
		client.start();

		new Thread("Connect") {
			public void run() {
				try {
					client.connect(5000, IP, TCP);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}.start();

	}

	public ArrayList<FakeMob> getFakeMobs() {

		return fakeMobs;
	}

	public void requestMobUpdate() {
		client.sendTCP(new RequestMobUpdate());
	}

	public void setFakeMobs(ArrayList<Mob> mobs) {
		for (Mob m : mobs) {
			fakeMobs.add(new FakeMob(m.getX(), m.getY(), m.getHealth(), m
					.getID(), m.isFacingRight()));
		}
	}

	public boolean isConnected() {
		return client.isConnected();
	}

	public void disconnect(String string) {
		client.sendTCP(new DisconnectPlayer(string));
		client.stop();
	}

	public void requestPlayerUpdate() {
		client.sendTCP(new RequestPlayerUpdate());
	}

	public ArrayList<FakePlayer> getFakePlayers() {

		return fakePlayers;
	}

	public void pushPlayer(Player player) {

		FakePlayer fp = new FakePlayer(player.getX(), player.getY(),
				player.getID(), player.facingRight());

		client.sendTCP(fp);
	}

	// public ArrayList<FakeMob> getDeadMobs() {
	// return deadMobs;
	// }

}