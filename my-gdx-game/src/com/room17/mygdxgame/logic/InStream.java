package com.room17.mygdxgame.logic;

public class InStream {
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
