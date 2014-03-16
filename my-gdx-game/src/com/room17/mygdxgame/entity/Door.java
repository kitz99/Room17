package com.room17.mygdxgame.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.room17.mygdxgame.generate.Mapper;

public class Door {
	
	private float X, Y;
	
	private float WIDTH = 128, HEIGHT = 140;
	private float time;

	private State state;

	private Animation idle;
	private Animation open;
	
	public BoundingBox box;
	private Rectangle rect;

	public Door(float x, float y) {
		X = x;
		Y = y;
		time = 0;

		TextureRegion[][] myArr = TextureRegion.split(new Texture(
				"sprites/door.png"), 64, 70);
		
		idle = new Animation(0.15f, myArr[0]);
		idle.setPlayMode(Animation.LOOP);

		TextureRegion[] myV = new TextureRegion[12];
		
		int k = 0;
		for(int i = 0; i < 8; i++) {
			myV[k] = myArr[1][i];
			k++;
		}
		
		for(int i = 0; i < 4; i++) {
			myV[k] = myArr[2][i];
			k++;
		}
		
		open = new Animation(0.15f, myV);
		open.setPlayMode(Animation.NORMAL);

		state = State.IDLE;
		
		rect = new Rectangle(X, Y, WIDTH, HEIGHT);
		box = Mapper.getBounds(X, Y, WIDTH, HEIGHT);
	}

	public void update(float delta) {
		time += delta;
		if(Gdx.input.justTouched() && state != State.OPEN) {
			time = 0;
			state = State.OPEN;
		}
	}

	public void draw(Batch batch) {
		switch (state) {
		case IDLE:
			batch.draw(idle.getKeyFrame(time), X, Y, WIDTH, HEIGHT);
			break;
		case OPEN:
			batch.draw(open.getKeyFrame(time), X, Y, WIDTH, HEIGHT);
			if(open.isAnimationFinished(time)) {
				time = 0;
				state = State.IDLE;
			}
			break;
		default:
			break;
		}

	}
	
	public boolean col(Rectangle test) {
		return rect.overlaps(test);
	}
}
