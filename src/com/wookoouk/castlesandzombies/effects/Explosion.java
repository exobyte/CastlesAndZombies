package com.wookoouk.castlesandzombies.effects;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wookoouk.castlesandzombies.entity.*;

public class Explosion {
	private float particleCount = 10;
	ArrayList<ExplosionParticle> particles;
	public boolean isDead;
	private Sound sound;
	
	public Explosion(float X, float Y) {

		particles = new ArrayList<ExplosionParticle>();

		for (int i = 0; i < particleCount; i++) {
			particles.add(new ExplosionParticle(X, Y));
		}

		sound = Gdx.audio.newSound(Gdx.files.internal("sounds/explode.wav"));
		sound.play();
		
	}
	public void render(SpriteBatch batch) {

		for (Iterator<ExplosionParticle> itr = particles.iterator(); itr.hasNext();) {
			ExplosionParticle dp = itr.next();
			
				dp.render(batch);
		}

	}
	public void update(ArrayList<Entity> arrayList) {
		
		if(particles.size() == 0){
			isDead = true;
		}
		
		for (Iterator<ExplosionParticle> itr = particles.iterator(); itr.hasNext();) {
			ExplosionParticle dp = itr.next();
			dp.update(arrayList);
			if (dp.isDead()) {
				itr.remove();
			} 
		}
	}
	public ArrayList<ExplosionParticle> getParticles() {
		return particles;
	}
}
