package com.wookoouk.castlesandzombies.mobs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.wookoouk.castlesandzombies.*;
import com.wookoouk.castlesandzombies.effects.*;
import com.wookoouk.castlesandzombies.players.*;
import com.wookoouk.castlesandzombies.server.fakes.FakePlayer;

public class Mob {
	private Sprite sprite;
	private static float SPEED = 0.5f;
	final static float MAX_VELOCITY = 10f; // 14f
	public int SCALE = 1;
	private boolean onfloor;
	private boolean faceingRight;
	private Random rand;
	private int health = 100;
	private boolean dead;
	protected Texture texture;
	private Color originalColor;
	private int IDLength = 50;
	// private Sound dieSound;
	private int attentionDistance = 50;
	private boolean flipped;
	protected int damagePower = 10;

	Body playerBody;
	Fixture playerPhysicsFixture;

	private String ID;

	private ArrayList<HitPoint> hitPoints;

	public Mob(float x, float y, String type) {
		rand = new Random();
		texture = new Texture(Gdx.files.internal("images/" + type + ".png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		sprite.setPosition(x, y);
		SPEED += ((rand.nextFloat() - rand.nextFloat()) / 2);

		ID = randomID();

		setRandomColor();

		originalColor = sprite.getColor();
		hitPoints = new ArrayList<HitPoint>();

		// dieSound = Gdx.audio
		// .newSound(Gdx.files.internal("sounds/enemyDie.wav"));
	}

	public Mob(Mob mobExample) {
		rand = new Random();
		ID = randomID();
		texture = mobExample.texture;
		sprite = new Sprite(texture);
		sprite.setPosition(mobExample.getX(), mobExample.getY());
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		SPEED += ((rand.nextFloat() - rand.nextFloat()) / 2);
		originalColor = sprite.getColor();
		hitPoints = new ArrayList<HitPoint>();
		setRandomColor();
	}

	private String randomID() {

		String tempID = "";

		for (int i = 0; i < IDLength; i++) {
			tempID += rand.nextInt(9);
		}

		return tempID;
	}

	public int getHealth() {
		return health;
	}

	private void setRandomColor() {
		sprite.setColor(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
	}

	private void moveLeft() {
		if (faceingRight) {
			sprite.flip(true, false);
			faceingRight = false;
		}
		// Vector2 vel = playerBody.getLinearVelocity();
		Vector2 pos = playerBody.getPosition();

		if (playerBody.getLinearVelocity().x + SPEED > -MAX_VELOCITY) {
			playerBody.applyLinearImpulse(-SPEED, 0, pos.x, pos.y);
		}

		// if (testCollision(arrayList) != null) {
		// sprite.translateX(SPEED);
		// if (shouldJump(arrayList)) {
		// jump();
		// }
		// }

	}

	private void moveRight() {
		if (!faceingRight) {
			sprite.flip(true, false);
			faceingRight = true;
		}
		// Vector2 vel = playerBody.getLinearVelocity();
		Vector2 pos = playerBody.getPosition();

		if (playerBody.getLinearVelocity().x + SPEED < MAX_VELOCITY) {
			playerBody.applyLinearImpulse(SPEED, 0, pos.x, pos.y);
		}

		// if (testCollision(arrayList) != null) {
		// sprite.translateX(-SPEED);
		// if (shouldJump(arrayList)) {
		// jump();
		// }
		//
		// }
	}

	// private boolean shouldJump(ArrayList<Entity> arrayList) {
	//
	// float jumpHeight = JUMPHEIGHT;
	// float testX;
	// float invertX;
	//
	// if (faceingRight) {
	// testX = SPEED;
	// invertX = -SPEED;
	// } else {
	// testX = -SPEED;
	// invertX = SPEED;
	// }
	//
	// sprite.translateY(jumpHeight);
	// sprite.translateX(testX);
	//
	// boolean hitSomething = testCollision(arrayList) != null;
	//
	// sprite.translateY(-jumpHeight);
	// sprite.translateX(invertX);
	//
	// // if (hitSomething) {
	// // // System.out.println("colission");
	// // return false;
	// // }
	// // return true;
	// return false;
	// }

	public void render(SpriteBatch batch) {
		sprite.draw(batch);

		Iterator<HitPoint> itr = hitPoints.iterator();

		while (itr.hasNext()) {
			HitPoint dp = itr.next();
			dp.update();
			dp.render(batch);
			if (dp.isDead()) {
				itr.remove();
			}
		}

	}

	public void update(Player player) {

		double distance;

		distance = Math
				.sqrt((player.getX() - sprite.getX())
						* (player.getX() - (player.getWidth() / 2)
								- sprite.getX() - (sprite.getWidth() / 2))
						+ (player.getY() - sprite.getY())
						* (player.getY() - sprite.getY()));

		if (distance <= attentionDistance) {

			// System.out.println("I see a player!");

			if (player.getX() > sprite.getX()) {
				playerPhysicsFixture.setFriction(0.2f);
				moveRight();
			} else if (player.getX() < sprite.getX()) {
				playerPhysicsFixture.setFriction(0.2f);
				moveLeft();
			} else {
				playerPhysicsFixture.setFriction(10000f);
			}
		}
		// gravity(arrayList);
		sprite.setPosition(
				playerBody.getPosition().x - (sprite.getWidth() / 2),
				playerBody.getPosition().y - (sprite.getHeight() / 2));

	}

	public Rectangle getHead() {

		Rectangle r = sprite.getBoundingRectangle();
		r.set(r.getX(), r.getY() + ((r.getHeight() / 4) * 3), r.getWidth(),
				r.getHeight() / 4);

		return r;

	}

	public Rectangle getBody() {
		return sprite.getBoundingRectangle();

	}

	public void hit(boolean headShot) {

		Color damageColor;
		int hurt;

		if (headShot) {
			hurt = 5 + rand.nextInt(5);
			damageColor = Color.RED;
		} else {
			hurt = rand.nextInt(5);
			damageColor = Color.WHITE;
		}

		health -= hurt;
		hitPoints.add(new HitPoint(hurt, sprite.getX()
				+ (sprite.getWidth() / 2),
				sprite.getY() + (sprite.getHeight()), damageColor));

		if (health <= 0) {
			playDeathSound();
			dead = true;
		}
	}

	public void jump() {
		if (onfloor) {
			onfloor = false;
		}
	}

	public boolean isDead() {
		return dead;
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public void setX(float x) {
		sprite.setX(x);
	}

	public void setY(float y) {
		sprite.setY(y);
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public Rectangle getRec() {
		return sprite.getBoundingRectangle();
	}

	public void setColorRed() {
		sprite.setColor(Color.RED);
	}

	public void clearColor() {
		sprite.setColor(originalColor);
	}

	public Color getColor() {
		return sprite.getColor();
	}

	private void playDeathSound() {
		// dieSound.play(0.6f);
	}

	public void setID(String string) {
		ID = string;
	}

	public String getID() {
		return ID;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setColor(Color color) {

		this.originalColor = color;
	}

	public boolean isFacingRight() {
		return faceingRight;
	}

	public void setFacingRight(boolean facingRight) {
		this.faceingRight = facingRight;
	}

	public boolean isFlipped() {
		return flipped;
	}

	public void flip(boolean b, boolean c) {
		if (flipped) {
			flipped = false;
		} else {
			flipped = true;
		}
		sprite.flip(true, false);
	}

	public int getAttackPower() {
		return damagePower;
	}

	public void setupBox2D(World world) {

		if (playerBody == null) {
			// TODO Box2D
			BodyDef playerDef = new BodyDef();
			playerDef.type = BodyType.DynamicBody;
			playerDef.position.set(sprite.getX() + (sprite.getWidth() / 2),
					sprite.getY() + (sprite.getHeight() / 2));

			playerBody = world.createBody(playerDef);

			PolygonShape poly = new PolygonShape();
			poly.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

			System.out.println(sprite.getWidth() / 2 + " " + sprite.getHeight()
					/ 2);

			FixtureDef cFix = new FixtureDef();
			cFix.shape = poly;
			cFix.filter.categoryBits = MyGdxGame.CATEGORY_MONSTER;
			cFix.filter.maskBits = MyGdxGame.MASK_MONSTER;

			playerBody.setBullet(true);
			playerBody.setFixedRotation(true);
			playerPhysicsFixture = playerBody.createFixture(cFix);
			playerPhysicsFixture.setFriction(0.1f);

			poly.dispose();
		}
	}

	public Body getBox2dBody() {
		return playerBody;
	}

	public void updateFromServer(ArrayList<FakePlayer> players) {

		// FakePlayer target = null;
		float targetX = 0;

		double distance = attentionDistance * 2;

		// get target

		for (FakePlayer fp : players) {

			double tempDistance = Math
					.sqrt((fp.getX() - sprite.getX())
							* (fp.getX() - (8 / 2) - sprite.getX() - (sprite
									.getWidth() / 2))
							+ (fp.getY() - sprite.getY())
							* (fp.getY() - sprite.getY()));

			if (tempDistance <= attentionDistance && tempDistance < distance) {
				distance = tempDistance;
				targetX = fp.getX();
			}

		}
		// now move the mob
		if (targetX != 0) {

			if (targetX > sprite.getX()) {
				playerPhysicsFixture.setFriction(0.2f);
				moveRight();
			} else if (targetX < sprite.getX()) {
				playerPhysicsFixture.setFriction(0.2f);
				moveLeft();
			} else {
				playerPhysicsFixture.setFriction(10000f);
			}
		}

		sprite.setPosition(
				playerBody.getPosition().x - (sprite.getWidth() / 2),
				playerBody.getPosition().y - (sprite.getHeight() / 2));
	}

}
