package com.room17.mygdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainMenu implements Screen {

	// private Texture bck;
	// private Sprite background;
	private Stage stage;
	private Skin skinner;
	private Button playButton;
	private SpriteBatch batch;
	private boolean pressedStart;

	private Music playing;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// batch.begin();
		// background.draw(batch);
		// batch.end();
		if (!playing.isPlaying()) {
			playing.play();
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		if (pressedStart) {
			((Game) Gdx.app.getApplicationListener())
					.setScreen(new GameScreen());
		}
		pressedStart = playButton.isPressed();

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		// bck = new Texture(Gdx.files.internal("menu/background.png"));
		// background = new Sprite(bck, 0, 0, bck.getWidth(),
		// Gdx.graphics.getHeight());
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true, batch);
		skinner = new Skin();
		skinner.add("A", new Texture("UI/menu/playButton.png"));
		skinner.add("a", new Texture("UI/menu/playButton1.png"));
		playButton = new Button(skinner.getDrawable("A"),
				skinner.getDrawable("a"));
		playButton.setPosition(Gdx.graphics.getWidth() / 2 - 312 / 2,
				Gdx.graphics.getHeight() / 2 - 123 / 2f);
		stage.addActor(playButton);
		Gdx.input.setInputProcessor(stage);
		pressedStart = false;

		playing = Gdx.audio.newMusic(Gdx.files.internal("sounds/one.mp3"));
		playing.setLooping(true);
	}

	@Override
	public void hide() {
		playing.stop();
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
		stage.dispose();
		playing.dispose();
		batch.dispose();
		skinner.dispose();
	}

}
