package com.wookoouk.castlesandzombies.effects;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.mobs.*;

public class Death {

	private float particleCount = 10;
	
	ArrayList<DeathParticle> particles;

	public Death(float X, float Y, Mob m) {

		particles = new ArrayList<DeathParticle>();

		for (int i = 0; i < particleCount; i++) {
			particles.add(new DeathParticle(X, Y, m));
		}

	}

	public void render(SpriteBatch batch, ArrayList<Entity> arrayList) {

		for (Iterator<DeathParticle> itr = particles.iterator(); itr.hasNext();) {
			DeathParticle dp = itr.next();
			if (dp.isDead()) {
				itr.remove();
			} else {
				dp.render(arrayList, batch);
			}
		}

	}
}
