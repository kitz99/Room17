package com.room17.mygdxgame;

import com.badlogic.gdx.Game;

public class MyGame extends Game {

	@Override
	public void create() {
		this.setScreen(new Splash(this));
	}
}
