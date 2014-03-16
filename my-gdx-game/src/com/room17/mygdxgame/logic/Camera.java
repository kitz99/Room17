package com.room17.mygdxgame.logic;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.room17.mygdxgame.entity.Jetty;
import com.room17.mygdxgame.generate.Mapper;

public class Camera {

	private static float WIDTH = 22 * Mapper.tile_width,
			HEIGHT = 12 * Mapper.tile_height;
	private static float halfW = 11 * Mapper.tile_width,
			halfH = 6 * Mapper.tile_height;
	private static float totalW = 89 * Mapper.tile_width,
			totalH = 94 * Mapper.tile_height;

	private OrthographicCamera camera;

	public Camera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		camera.update();
	}

	public void updatePos(float a, float b) {
		float x = a + Jetty.WIDTH / 2, y = b + Jetty.HEIGHT / 2;
		if (x < halfW) {
			x = halfW;
		} else if (x > totalW) {
			x = totalW;
		}
		if (y < halfH) {
			y = halfH;
		} else if (y > totalH) {
			y = totalH;
		}
		camera.position.x = x;
		camera.position.y = y;
		camera.update();
	}

	public OrthographicCamera getCam() {
		return camera;
	}

	public boolean inView(BoundingBox aux) {
		return camera.frustum.boundsInFrustum(aux);
	}
}
