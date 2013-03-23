package com.wookoouk.castlesandzombies.guns;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.wookoouk.castlesandzombies.*;
import com.wookoouk.castlesandzombies.entity.*;

public class Grenade {
	private Texture texture;
	private Sprite sprite;
	private float age;
	private float timer = 3;
	public boolean isDead;
	private float SCALE = 0.5f;

	private Body playerBody;
//	private Fixture playerPhysicsFixture;
	
	private float angleX;
	private float angleY;
	private float speed = 400;

	public Grenade(float playerX, float playerY, float rotation, World world) {
		texture = new Texture(Gdx.files.internal("images/grenade.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		sprite.setOrigin(sprite.getX()+(sprite.getWidth()/2), sprite.getY()+(sprite.getHeight()/2));
		sprite.setPosition(playerX, playerY);

		angleX = (float) Math.cos(Math.toRadians(rotation))
				* ((speed) * Gdx.graphics.getDeltaTime());
		angleY = (float) Math.sin(Math.toRadians(rotation))
				* ((speed) * Gdx.graphics.getDeltaTime());
		
		getBoxBody(world);
	}

	public void update(ArrayList<Entity> arrayList) {
		age += Gdx.graphics.getDeltaTime();

		if(age > timer){
			explode();
		}
		
		sprite.setPosition(
				playerBody.getPosition().x - (sprite.getWidth() / 2),
				playerBody.getPosition().y - (sprite.getHeight() / 2));
		sprite.setRotation((float) Math.toDegrees(playerBody.getAngle()));

	}

	private void explode() {
		isDead = true;
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

//	public float getAge() {
//		return age;
//	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public void getBoxBody(World world) {
		// TODO Box2D
		BodyDef playerDef = new BodyDef();
		playerDef.type = BodyType.DynamicBody;
		playerDef.position.set(sprite.getX() + (sprite.getWidth() / 2)+angleX,
				sprite.getY() + (sprite.getHeight() / 2)+angleY);

		CircleShape cs = new CircleShape(); // TODO
		cs.setRadius(sprite.getWidth() / 2);

		playerBody = world.createBody(playerDef);

		FixtureDef cFix = new FixtureDef();

		cFix.friction = 0.2f;
		cFix.density = 0.5f;
		cFix.restitution = 0.4f;
		cFix.shape = cs;

		cFix.filter.categoryBits = MyGdxGame.CATEGORY_PROJECTILE;
		cFix.filter.maskBits = MyGdxGame.MASK_PROJECTILE;
		
		playerBody.createFixture(cFix);
		
		
		
		
		Vector2 pos = playerBody.getPosition();
		playerBody.applyLinearImpulse(angleX*50, angleY*50, pos.x, pos.y);

	}

	public Body getBody() {
		return playerBody;
	}

}
