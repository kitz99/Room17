package com.room17.mygdxgame.logic;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class Resources {
	public static Pool<Rectangle> rPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	private static int nrSteps = 0;

	public// synchronized
	static void addSteps(int aux) {
		nrSteps += aux;
	}

	public// synchronized
	static int getSteps() {
		return nrSteps;
	}
	
}
