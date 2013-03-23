package com.wookoouk.castlesandzombies.players;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.wookoouk.castlesandzombies.MyGdxGame;
import com.wookoouk.castlesandzombies.effects.*;
import com.wookoouk.castlesandzombies.entity.*;
import com.wookoouk.castlesandzombies.guns.*;

public class Player {
	private Sprite sprite;
	private TextureRegion currentFrame;
	private float stateTime = 0f;
	private Animation walkLeft;
	private Animation walkRight;
	private static float SCALE = 1;
	private static final float SPEED = 2;
	private static final float jumpHeight = 50;
	private boolean rightBackwards = false;
	private boolean leftBackwards = false;
	private int ammo = 100;
	private ArrayList<Bullet> bullets;
	private ArrayList<Grenade> grenades;
	private ArrayList<Explosion> explosions;
	private float SHOOTTIMEERMAX = 0.2f;
	private float shootTimer = SHOOTTIMEERMAX;
	private Sound noAmmoSound;
	private boolean facingRight = true;
	private Gun gun;
	private float gunRot;
	private boolean onfloor;
//	private Sound hitFloor;
	private Sound pickUp;
	private Sound shoot;
	private int grenadeAmmo = 5;
	private int MAXHEALTH = 100;
	private int health = MAXHEALTH;
	private int hurtTimer = 0;
	private int hurtTimerMax = 100;
	private boolean usingTorch = true;
	private boolean jumping;
	final static float MAX_VELOCITY = 20f; // 14f
	private Body playerBody;
	private Fixture playerPhysicsFixture;
	private Fixture feet;
	private String ID = "";
//	private int IDLength = 50;

	public Player(float X, float Y, String ID) {

		walkLeft = new AnimationResult(4, 1, 4, 8, "walkLeft2", 0.2f).gen();
		walkRight = new AnimationResult(4, 1, 4, 8, "walkRight2", 0.2f).gen();

		if (ID != null && !ID.isEmpty()) {
			this.ID = ID.toUpperCase();
		}

		stateTime = 0f;
		currentFrame = walkRight.getKeyFrame(stateTime, true);

		sprite = new Sprite(currentFrame);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setSize(sprite.getWidth() * SCALE, sprite.getHeight() * SCALE);
		sprite.setPosition(X, Y);

		noAmmoSound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/gunClick.wav"));

		shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot2.wav"));

		bullets = new ArrayList<Bullet>();
		grenades = new ArrayList<Grenade>();
		explosions = new ArrayList<Explosion>();

//		hitFloor = Gdx.audio.newSound(Gdx.files.internal("sounds/land.wav"));
		pickUp = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup.wav"));

		gun = new Gun(sprite.getX() + (sprite.getWidth() / 2), sprite.getY()
				+ (sprite.getHeight() / 2));

		// ID = randomID();
	}

	// private String randomID() {
	//
	// Random rand = new Random();
	//
	// String tempID = "";
	//
	// for (int i = 0; i < IDLength; i++) {
	// tempID += rand.nextInt(9);
	// }
	//
	// return tempID;
	// }

	private float getGunX() {
		float j = sprite.getX() + (sprite.getWidth() / 2);
		return j;
	}

	private float getGunY() {
		float k = sprite.getY() + (sprite.getHeight() / 2);
		return k;
	}

	public void moveRight(ArrayList<Entity> arrayList) {

		if (playerBody.getLinearVelocity().x + SPEED < MAX_VELOCITY) {

			System.out.println("RIGHT: " + playerBody.getLinearVelocity().x);

			Vector2 pos = playerBody.getPosition();

			playerBody.applyLinearImpulse(SPEED, 0, pos.x, pos.y);

			currentFrame = walkRight.getKeyFrame(stateTime, true);
			if (onfloor) {
				stateTime += Gdx.graphics.getDeltaTime() * SPEED * SPEED
						* SPEED;
			}
			// }

			if (!facingRight) {
				if (!leftBackwards) {
					walkLeft.setPlayMode(Animation.LOOP_REVERSED);
					leftBackwards = true;
				}
			} else {
				if (rightBackwards) {
					walkRight.setPlayMode(Animation.NORMAL);
					rightBackwards = false;
				}
			}
		}
	}

	public void moveLeft(ArrayList<Entity> arrayList) {

		if (playerBody.getLinearVelocity().x - SPEED > -MAX_VELOCITY) {

			System.out.println("LEFT: " + playerBody.getLinearVelocity().x);

			Vector2 pos = playerBody.getPosition();

			playerBody.applyLinearImpulse(-SPEED, 0, pos.x, pos.y);

			currentFrame = walkLeft.getKeyFrame(stateTime, true);

			if (onfloor) {
				stateTime += Gdx.graphics.getDeltaTime() * SPEED * SPEED
						* SPEED;
			}

			if (facingRight) {
				if (!rightBackwards) {
					walkRight.setPlayMode(Animation.LOOP_REVERSED);
					rightBackwards = true;
				}
			} else {
				if (leftBackwards) {
					walkLeft.setPlayMode(Animation.NORMAL);
					leftBackwards = false;
				}
			}

		}
	}

	public void render(SpriteBatch batch) {
		sprite.setRegion(currentFrame);
		sprite.draw(batch);
		gun.render(batch);
		updateBullets(batch);
		renderGrenades(batch);
		renderExplosions(batch);
	}

	private void renderExplosions(SpriteBatch batch) {

		for (Iterator<Explosion> itr = explosions.iterator(); itr.hasNext();) {
			Explosion dp = itr.next();
			dp.render(batch);
		}
	}

	private void renderGrenades(SpriteBatch batch) {

		for (Grenade g : grenades) {
			g.render(batch);
		}
	}

	public float getX() {
		return sprite.getX();
	}

	public float getY() {
		return sprite.getY();
	}

	public float getWidth() {
		return sprite.getWidth();
	}

	public float getHeight() {
		return sprite.getHeight();
	}

	public void update(OrthographicCamera camera, ArrayList<Entity> arrayList,
			World world) {

		onfloor = isPlayerGrounded(world);

		if (hurtTimer < hurtTimerMax) {
			hurtTimer++;
		}

		gravity(arrayList, world);

		if (Gdx.app.getType().equals(ApplicationType.Desktop)) {
			followMouse();
		}

		gun.update(getGunX(), getGunY(), getGunRot(camera));

		updateGrenades(arrayList, world);
		updateExplosions(arrayList);

		shootTimer += Gdx.graphics.getDeltaTime();

	}

	private void gravity(ArrayList<Entity> arrayList, World world) {

		playerBody.setAwake(true);

		sprite.setPosition(
				playerBody.getPosition().x - (sprite.getWidth() / 2),
				playerBody.getPosition().y - (sprite.getHeight() / 2));

		//
	}

	private boolean isPlayerGrounded(World world) {
		List<Contact> contactList = world.getContactList();

		for (int i = 0; i < contactList.size(); i++) {
			Contact contact = contactList.get(i);
			if (contact.isTouching()
					&& (contact.getFixtureA() == feet || contact.getFixtureB() == feet)) {
				return true;

			}
		}
		return false;
	}

	private float getGunRot(OrthographicCamera camera) {

		Vector3 tmpVec = new Vector3();

		camera.unproject(tmpVec.set(Gdx.input.getX(), Gdx.input.getY(), 0));

		float xDistance = tmpVec.x - getGunX();
		float yDistance = tmpVec.y - getGunY();

		gunRot = (float) Math.toDegrees(Math.atan2(yDistance, xDistance));

		if (Gdx.app.getType().equals(ApplicationType.Android)) {
			if (facingRight) {
				gunRot = 0;
				return 0;
			} else {
				gunRot = 180;
				return 180;
			}

		}

		return gunRot;
	}

	private void updateBullets(SpriteBatch batch) {

		for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext();) {
			Bullet dp = itr.next();
			dp.update();
			dp.render(batch);
			if (dp.getAge() > dp.MAXAGE) {
				itr.remove();
			}
		}

	}

	private void updateGrenades(ArrayList<Entity> arrayList, World world) {

		for (Iterator<Grenade> itr = grenades.iterator(); itr.hasNext();) {
			Grenade dp = itr.next();
			dp.update(arrayList);
			if (dp.isDead) {
				explosions.add(new Explosion(dp.getX(), dp.getY()));
				world.destroyBody(dp.getBody());
				itr.remove();
			}
		}
	}

	private void updateExplosions(ArrayList<Entity> arrayList) {
		for (Iterator<Explosion> itr = explosions.iterator(); itr.hasNext();) {
			Explosion dp = itr.next();
			dp.update(arrayList);
			if (dp.isDead) {
				itr.remove();
			}
		}
	}

	public void followMouse() {
		if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
			if (!facingRight) {
				gun.flip();
			}
			facingRight = true;
			currentFrame = walkRight.getKeyFrame(stateTime, true); // Backwards
		} else {
			if (facingRight) {
				gun.flip();
			}
			facingRight = false;
			currentFrame = walkLeft.getKeyFrame(stateTime, true);
		}
	}

	public int getAmmo() {
		return ammo;
	}

	public void shoot() {
		if (shootTimer > SHOOTTIMEERMAX) {

			if (ammo > 0) {

				float playerCenterX = sprite.getX() + (sprite.getWidth() / 2);
				float playerCenterY = sprite.getY() + (sprite.getHeight() / 2);

				bullets.add(new Bullet(
						playerCenterX - (Bullet.bulletWidth / 2), playerCenterY
								- (Bullet.bulletHeight / 2), gunRot));

				shoot.play();

				shootTimer = 0;
				ammo -= 1;
			} else {
				noAmmoSound.play();
				shootTimer = 0;
			}

		}
	}

	public void throwGrenade(World world) {

		if (grenadeAmmo > 0) {

			float playerCenterX = sprite.getX() + (sprite.getWidth() / 2);
			float playerCenterY = sprite.getY() + (sprite.getHeight() / 2);

			grenades.add(new Grenade(playerCenterX, playerCenterY, gunRot,
					world));

			grenadeAmmo--;

		}
	}

	public void jump(World world) {

		if (onfloor) {

			Vector2 pos = playerBody.getPosition();
			Vector2 vel = playerBody.getLinearVelocity();

			playerBody.setLinearVelocity(vel.x, 0);
			playerBody.setTransform(pos.x, pos.y + 0.01f, 0);
			playerBody.applyLinearImpulse(0, jumpHeight, pos.x, pos.y);
		}
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public ArrayList<Explosion> getExplosions() {
		return explosions;
	}

	public void addAmmo(int ammount) {
		ammo += ammount;
		pickUp.play();
	}

	public int getGrenades() {
		return grenadeAmmo;
	}

	public int getHealth() {
		return health;
	}

	public Rectangle getRec() {
		return sprite.getBoundingRectangle();
	}

	public void damage(int attackPower) {
		if (health > 0) {
			health -= attackPower;
		}
	}

	public boolean canBeHurt() {

		if (hurtTimer == hurtTimerMax) {
			hurtTimer = 0;
			return true;
		}
		return false;

	}

	public float getMaxHealth() {
		return MAXHEALTH;
	}

	public void addHealth(int i) {

		if (health + i >= MAXHEALTH) {
			health = MAXHEALTH;
		} else if (health + i < MAXHEALTH) {
			health += i;
		}
	}

	public void faceLeft() {
		// currentFrame = walkLeft.getKeyFrame(stateTime, true);
		if (facingRight) {
			gun.flip();
		}
		facingRight = false;
	}

	public void faceRight() {
		// currentFrame = walkRight.getKeyFrame(stateTime, true);
		if (!facingRight) {
			gun.flip();
		}
		facingRight = true;
	}

	public void setPosition(float x, float y) {
		// TODO Auto-generated method stub
		sprite.setPosition(x, y);
	}

	public void getBoxBody(World world) {
		// TODO Box2D
		BodyDef playerDef = new BodyDef();
		playerDef.type = BodyType.DynamicBody;
		playerDef.position.set(sprite.getX() + (sprite.getWidth() / 2),
				sprite.getY() + (sprite.getHeight() / 2));

		playerBody = world.createBody(playerDef);
		playerBody.setBullet(true);
		playerBody.setFixedRotation(true);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

		FixtureDef cFix = new FixtureDef();
		cFix.shape = poly;
		cFix.friction = 0.2f;
		cFix.filter.categoryBits = MyGdxGame.CATEGORY_PLAYER;
		cFix.filter.maskBits = MyGdxGame.MASK_PLAYER;

		playerPhysicsFixture = playerBody.createFixture(cFix);

		PolygonShape foot = new PolygonShape();
		foot.setAsBox(sprite.getWidth() / 2.1f, sprite.getHeight() / 4,
				new Vector2(0, -sprite.getHeight() / 4), 0);
		feet = playerBody.createFixture(foot, 0);
		feet.isSensor();

	}

	public boolean facingRight() {
		return facingRight;
	}

	public float getGunRot() {
		return gunRot;
	}

	public void toggleTorch() {
		usingTorch = !usingTorch;
	}

	public boolean isUsingTorch() {
		return usingTorch;
	}

	public void setJumping(boolean b) {
		jumping = b;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setFriction(float f) {
		// System.out.println("Friction set to " + f);
		feet.setFriction(f);
		playerPhysicsFixture.setFriction(f);
	}

	public String getID() {
		return ID;
	}
}
