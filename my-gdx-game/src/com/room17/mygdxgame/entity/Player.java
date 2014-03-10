package com.room17.mygdxgame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.room17.mygdxgame.generate.Generator;
import com.room17.mygdxgame.generate.Mapper;
import com.room17.mygdxgame.logic.Resources;

public class Player implements Disposable {

	private Sound jumping;

	private Rectangle play;

	public static float HEIGHT = 64f, WIDTH = 64f;
	private float speed = 60 * 3, gravity = 60 * 2.4f;
	private float X, Y;
	boolean canJump, right, punching, isJumping;
	private Vector2 velocity;
	private State stat;

	private Animation idle;
	private Animation walk;
	private Animation jump;
	// private Animation fall;
	// private Animation punch;

	private float time;

	private Texture pText;

	private float olderX, olderY;

	public Player(float a, float b) {
		int row = 9, col = 6;
		float rate = 0.15f;
		X = a;
		Y = b;
		pText = new Texture("sprites/sumoHulk.png");
		TextureRegion[][] myArr = TextureRegion.split(pText, pText.getWidth()
				/ col, pText.getHeight() / row);

		TextureRegion[] myV = new TextureRegion[4];
		for (int i = 0; i < 4; i++) {
			BleedFix.fixBleeding(myArr[0][i]);
			myV[i] = myArr[0][i];
		}
		idle = new Animation(rate, myV);
		idle.setPlayMode(Animation.LOOP);

		myV = new TextureRegion[6];
		for (int i = 0; i < 6; i++) {
			BleedFix.fixBleeding(myArr[1][i]);
			myV[i] = myArr[1][i];
		}
		walk = new Animation(rate, myV);
		walk.setPlayMode(Animation.LOOP);

		myV = new TextureRegion[3];
		for (int i = 0; i < 3; i++) {
			BleedFix.fixBleeding(myArr[2][i]);
			myV[i] = myArr[2][i];
		}
		jump = new Animation(rate, myV);
		jump.setPlayMode(Animation.LOOP);

		// myV = new TextureRegion[3];
		// for (int i = 0; i < 3; i++) {
		// BleedFix.fixBleeding(myArr[3][i]);
		// myV[i] = myArr[3][i];
		// }
		// fall = new Animation(rate, myV);
		// fall.setPlayMode(Animation.LOOP);
		//
		// myV = new TextureRegion[3];
		// for (int i = 0; i < 3; i++) {
		// BleedFix.fixBleeding(myArr[6][i]);
		// myV[i] = myArr[6][i];
		// }
		// punch = new Animation(0.10f, myV);
		// punch.setPlayMode(Animation.NORMAL);

		right = true;

		velocity = new Vector2();
		stat = State.STAND;
		time = 0;
		isJumping = false;
		play = new Rectangle(X, Y, WIDTH, HEIGHT);

		jumping = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
	}

	public boolean collidesRight() {
		int a = (int) X / Mapper.tile_width, b = (int) Y / Mapper.tile_height;
		Rectangle rect = Resources.rPool.obtain();
		rect.set((a + 1) * Mapper.tile_width, (b + 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a + 1][b + 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set((a + 1) * Mapper.tile_width, b * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a + 1][b] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set((a + 1) * Mapper.tile_width, (b - 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a + 1][b - 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		Resources.rPool.free(rect);
		return false;
	}

	public boolean collidesLeft() {
		int a = (int) X / Mapper.tile_width + 1, b = (int) Y
				/ Mapper.tile_height;
		Rectangle rect = Resources.rPool.obtain();
		rect.set((a - 1) * Mapper.tile_width, (b + 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a - 1][b + 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set((a - 1) * Mapper.tile_width, b * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a - 1][b] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set((a - 1) * Mapper.tile_width, (b - 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a - 1][b - 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		Resources.rPool.free(rect);
		return false;
	}

	public boolean collidesTop() {
		int a = (int) X / Mapper.tile_width, b = (int) Y / Mapper.tile_height;
		Rectangle rect = Resources.rPool.obtain();
		rect.set((a - 1) * Mapper.tile_width, (b + 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a - 1][b + 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set(a * Mapper.tile_width, (b + 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a][b + 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set((a + 1) * Mapper.tile_width, (b + 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a + 1][b + 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		Resources.rPool.free(rect);
		return false;
	}

	public boolean collidesBottom() {
		int a = (int) X / Mapper.tile_width, b = (int) Y / Mapper.tile_height
				+ 1;
		Rectangle rect = Resources.rPool.obtain();
		rect.set((a - 1) * Mapper.tile_width, (b - 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a - 1][b - 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set(a * Mapper.tile_width, (b - 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a][b - 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		rect.set((a + 1) * Mapper.tile_width, (b - 1) * Mapper.tile_height,
				Mapper.tile_width, Mapper.tile_height);
		if (Generator.finalMap[a + 1][b - 1] == 0) {
			if (Intersector.overlaps(play, rect)) {
				Resources.rPool.free(rect);
				return true;
			}
		}
		Resources.rPool.free(rect);
		return false;
	}

	public void update(float delta, float x, float y, boolean aPress) {
		time += delta;
		velocity.y -= gravity * delta;

		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			x = -1;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			x = 1;
		}

		if (!isJumping && canJump
				&& (Gdx.input.isKeyPressed(Keys.SPACE) || aPress)) {
			velocity.y = speed / 1.7f;
			isJumping = true;
			jumping.play();
		}
		if (!aPress) {
			isJumping = false;
		}

		velocity.x = speed * x;

		if (velocity.y > speed) {
			velocity.y = speed;
		} else if (velocity.y < -speed) {
			velocity.y = -speed;
		}

		olderX = X;
		olderY = Y;
		boolean collisionX = false, collisionY = false;
		float a = velocity.x, b = velocity.y;

		X += velocity.x * delta;
		play.set(X, Y, WIDTH, HEIGHT);
		if (velocity.x < 0) { // going left
			collisionX = collidesLeft();
		} else if (velocity.x > 0) {// going right
			collisionX = collidesRight();
		}
		if (collisionX) {
			X = olderX;
			velocity.x = 0;
		}

		Y += velocity.y * delta * 5f;
		play.set(X, Y, WIDTH, HEIGHT);
		if (velocity.y < 0) { // going down
			canJump = collisionY = collidesBottom();
		} else if (velocity.y > 0) {// going up
			collisionY = collidesTop();
			if (collisionY) {
				canJump = false;
			}
		}
		if (collisionY) {
			Y = olderY;
			velocity.y = 0;
		}

		if (b > 0 && stat != State.JUMP) {
			stat = State.JUMP;
			time = 0;
		}
		if (velocity.y == 0 && a != 0 && stat != State.WALK) {
			stat = State.WALK;
			time = 0;
		}
		if (velocity.y == 0 && a == 0 && stat != State.STAND) {
			stat = State.STAND;
			time = 0;
		}
		if (a != 0) {
			right = (a > 0);
		}
		play.set(X, Y, WIDTH, HEIGHT);

	}

	public float getX() {
		return X;
	}

	public float getY() {
		return Y;
	}

	@Override
	public void dispose() {
		pText.dispose();
	}

	public void draw(Batch batch) {
		TextureRegion frame = null;
		switch (stat) {
		case STAND:
			frame = idle.getKeyFrame(time);
			break;
		// case FALL:
		// frame = fall.getKeyFrame(time);
		// break;
		case WALK:
			frame = walk.getKeyFrame(time);
			break;
		case JUMP:
			frame = jump.getKeyFrame(time);
			break;
		// case PUNCH:
		// frame = punch.getKeyFrame(time);
		// if (punch.isAnimationFinished(time)) {
		// punching = false;
		// }
		// break;
		default:
			break;
		}
		if (right) {
			batch.draw(frame, X, Y, WIDTH, HEIGHT);
		} else {
			batch.draw(frame, X + WIDTH, Y, -WIDTH, HEIGHT);
		}
	}

	public Rectangle getRect() {
		return play;
	}

	// public float getOlderX() {
	// return olderX;
	// }

	// public void setOlderX(float olderX) {
	// this.olderX = olderX;
	// }
}
