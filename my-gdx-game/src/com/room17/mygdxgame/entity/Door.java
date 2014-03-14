package com.room17.mygdxgame.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door {
	private float X, Y;
	private float WIDTH = 128, HEIGHT = 140;
	private float time;

	private Animation idle;
	//private Animation open;
	//private Animation close;

	public Door(float x, float y) {
		X = x;
		Y = y;
		time = 0;

		TextureRegion[][] myArr = TextureRegion.split(new Texture(
				"sprites/door.png"), 64, 70);
		idle = new Animation(0.15f, myArr[0]);
		idle.setPlayMode(Animation.LOOP);
	}

	public void update(float delta) {
		time += delta;
	}

	public void draw(Batch batch) {
		batch.draw(idle.getKeyFrame(time), X, Y, WIDTH, HEIGHT);
	}
}
