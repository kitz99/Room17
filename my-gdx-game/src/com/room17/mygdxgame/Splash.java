package com.room17.mygdxgame;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.room17.mygdxgame.tween.SpriteAccessor;

public class Splash implements Screen {
	
	private MyGame parent;

	private SpriteBatch batch;
	private Sprite splash;
	private TweenManager tweenManager;
	
	public Splash(MyGame aux) {
		parent = aux;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		tweenManager.update(delta);
		batch.begin();
		splash.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		splash = new Sprite(new Texture("data/myLogo.png"));
		splash.setX(Gdx.graphics.getWidth() / 2f - splash.getWidth() / 2f);
		splash.setY(Gdx.graphics.getHeight() / 2f - splash.getHeight() / 2f);

		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 0.5f).target(1).repeatYoyo(1, 2)
				.setCallback(new TweenCallback() {

					@Override
					public void onEvent(int type, BaseTween<?> source) {
						parent.setScreen(new MainMenu(parent));
					}

				}).start(tweenManager);

	}

	@Override
	public void hide() {
		// dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		splash.getTexture().dispose();
	}

}
