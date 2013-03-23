package com.wookoouk.castlesandzombies.server;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.physics.box2d.World;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.wookoouk.castlesandzombies.mobs.Mob;
import com.wookoouk.castlesandzombies.server.fakes.FakeMob;
import com.wookoouk.castlesandzombies.server.fakes.FakePlayer;

public class GameServer {
	private Server server;
	private ArrayList<Mob> mobs;
	private ArrayList<FakePlayer> players;
	public final static int TCP = 54554; // 54555

	// public final int UDP = 54777;

	public GameServer() throws IOException {

		server = new Server();
		mobs = new ArrayList<Mob>();
		players = new ArrayList<FakePlayer>();

		Kryo kryo = server.getKryo();
		kryo.register(FakeMob.class);
		kryo.register(RequestMobUpdate.class);
		kryo.register(RequestPlayerUpdate.class);
		kryo.register(FakePlayer.class);
		kryo.register(DisconnectPlayer.class);

		Log.set(Log.LEVEL_NONE);

		
		server.addListener(new Listener() {
			
			public void received(Connection connection, Object object) {
				if (object instanceof RequestMobUpdate) {
					for (Mob m : mobs) {
						FakeMob fm = new FakeMob(m.getX(), m.getY(), m
								.getHealth(), m.getID(), m.isFacingRight());
						connection.sendTCP(fm);
					}
				}
				if (object instanceof RequestPlayerUpdate) {
					for (FakePlayer p : players) {
						connection.sendTCP(p);
					}
				}
				if (object instanceof FakePlayer) {
					FakePlayer p = (FakePlayer) object;

					boolean newPlayer = false;

					for (FakePlayer pla : players) {

						System.out.println("SERVER: " + pla.getID());

						if (pla.getID().equals(p.getID())) {
							pla.setX(p.getX());
							pla.setY(p.getY());
							pla.setDirection(p.facingRight());
							newPlayer = true;
						}
					}

					if (newPlayer == false) {
						players.add(p);
					}
				}
				if (object instanceof DisconnectPlayer) {
					
					DisconnectPlayer quitter = (DisconnectPlayer)object;
					
					for (Iterator<FakePlayer> itr = players.iterator(); itr.hasNext();) {
						FakePlayer dp = itr.next();
						if(dp.getID().equals(quitter.getID())){
							System.out.println("SERVER REMOVING: "+quitter.getID());
							itr.remove();
							server.sendToAllTCP(quitter);
						}
					}
				}
			}

		});

		server.start();
		server.bind(TCP);
	}

	public void initMobs(ArrayList<Mob> mobsIN, World world) {
		mobs = cloneMobs(mobsIN, world);
	}

	public ArrayList<Mob> cloneMobs(ArrayList<Mob> mobsIN, World world) {

		ArrayList<Mob> clone = new ArrayList<Mob>();
		for (Mob mob : mobsIN) {

			Mob xyz = null;
			Class<?> cl;

			try {
				cl = Class.forName(mob.getClass().getName());

				Constructor<?> con = cl
						.getConstructor(float.class, float.class);
				xyz = (Mob) con.newInstance(mob.getX(), mob.getY());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			xyz.setupBox2D(world);
			xyz.setID(mob.getID());
			xyz.setHealth(mob.getHealth());
			clone.add(xyz);
		}
		return clone;

	}

	// public void pushMobUpdate(ArrayList<Mob> mobsIN) {
	//
	// }

	public void disconnect() {
		server.stop();
	}

	public void pushMobUpdate() {
		for (Mob m : mobs) {
			m.updateFromServer(players);
		}
	}

}