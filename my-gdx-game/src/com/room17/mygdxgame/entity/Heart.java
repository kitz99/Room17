package com.room17.mygdxgame.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.room17.mygdxgame.generate.Mapper;

public class Heart {
	private static Animation myAnim = null;

	private static float WIDTH = 70, HEIGHT = 64;

	public BoundingBox box;

	public static void setAnimation() {
		TextureRegion[][] myV = TextureRegion.split(new Texture(
				"sprites/hearty.png"), (int) WIDTH, (int) HEIGHT);
		myAnim = new Animation(0.10f, myV[0]);
		myAnim.setPlayMode(Animation.LOOP);
	}

	private float stateTime;

	private float X, Y;

	private Rectangle rect;

	public Heart(float x, float y) {
		stateTime = (float) (Math.random() * 30);
		X = x;
		Y = y;
		rect = new Rectangle(X, Y, WIDTH, HEIGHT);
		box = Mapper.getBounds(X, Y, WIDTH, HEIGHT);
	}

	public void update(float delta) {
		stateTime += delta;
	}

	public void draw(Batch batch) {
		batch.draw(myAnim.getKeyFrame(stateTime), X, Y, WIDTH, HEIGHT);
	}

	public float getX() {
		return X;
	}

	public float getY() {
		return Y;
	}

	public boolean col(Rectangle test) {
		return rect.overlaps(test);
	}

	public float width() {
		return WIDTH;
	}

	public float height() {
		return HEIGHT;
	}

}
