package com.room17.mygdxgame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.room17.mygdxgame.entity.Door;
import com.room17.mygdxgame.entity.Heart;
import com.room17.mygdxgame.entity.Jetty;
import com.room17.mygdxgame.generate.Mapper;
import com.room17.mygdxgame.logic.Camera;
import com.room17.mygdxgame.logic.UI;

public class GameScreen implements Screen {
	
	private MyGame parent;

	private Camera camera;
	private UI myUI;

	private Sound coin;

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	// private float animeTime;

	private Jetty player;
	private Door door;
	
	private Array<Heart> myV;
	
	public GameScreen(MyGame aux) {
		//super();
		parent = aux;
	}

	@Override
	public void render(float delta) {
		// animeTime += 0.0014f;
		// if (animeTime > 1f)
		// animeTime = 0.0f;

		Gdx.gl.glClearColor(0.294f, 0.294f, 0.294f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float d = Gdx.graphics.getDeltaTime();
		player.update(d, myUI.getX(), myUI.isPressedA());

		Iterator<Heart> it = myV.iterator();
		while (it.hasNext()) {
			Heart aux = it.next();
			aux.update(delta);
			if (aux.col(player.getRect())) {
				coin.play();
				myUI.updtScore();
				it.remove();
			}
		}

		// Vector3 position = camera.position;
		// position.x += (player.getX() - position.x) * 0.1f;
		// position.y += (player.getY() - position.y) * 0.1f;
		// camera.position.x = position.x;
		// camera.position.y = position.y;

		camera.updatePos(player.getX(), player.getY());

		renderer.setView(camera.getCam());
		renderer.render();

		door.update(d);
		
		renderer.getSpriteBatch().begin();
		
		if(camera.inView(door.box)) {
			door.draw(renderer.getSpriteBatch());
		}
		
		player.draw(renderer.getSpriteBatch());

		it = myV.iterator();
		while (it.hasNext()) {
			Heart aux = it.next();
			if (camera.inView(aux.box)) {
				aux.draw(renderer.getSpriteBatch());
			}
		}

		renderer.getSpriteBatch().end();
		myUI.act(d);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		camera = new Camera();

		// batch = new SpriteBatch();
		// bground = new Texture("maps/background.png");
		// bground.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		map = Mapper.generator();
		renderer = new OrthogonalTiledMapRenderer(map);
		myUI = new UI(renderer.getSpriteBatch());

		Vector2 loc = Mapper.getNormalPos();
		player = new Jetty(loc.x * Mapper.tile_width, loc.y
				* Mapper.tile_height);
		Heart.setAnimation();
		myV = new Array<Heart>();
		for (int i = 0; i < 50; i++) {
			loc = Mapper.getNormalPos();
			myV.add(new Heart(loc.x * Mapper.tile_width, loc.y
					* Mapper.tile_height));
		}
		
		loc = Mapper.getDoorPos();
		
		door = new Door(loc.x * Mapper.tile_width, loc.y * Mapper.tile_height);
		System.out.println(loc.x + " " + loc.y);

		coin = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
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
		myUI.setSteps();
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		myUI.dispose();
		coin.dispose();
	}

}
