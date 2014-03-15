package com.room17.mygdxgame.generate;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class Mapper {

	public static Array<Vector2> myDoors;
	public static Array<Vector2> myArr;
	public static int width = 100, height = 100, tile_width = 70,
			tile_height = 70;
	static Random rand = new Random();

	public static TiledMap generator() {
		Generator.generate();
		TiledMap map = new TiledMap();
		MapLayers layers = map.getLayers();

		myArr = new Array<Vector2>();
		myDoors = new Array<Vector2>();

		TiledMapTileLayer layer1 = new TiledMapTileLayer(width, height,
				tile_width, tile_height);

		TextureRegion[] myV = new TextureRegion[3];

		myV[0] = new TextureRegion(new Texture("maps/box0.png"));
		myV[1] = new TextureRegion(new Texture("maps/box1.png"));
		myV[2] = new TextureRegion(new Texture("maps/box2.png"));
		//myV[3] = new TextureRegion(new Texture("maps/box3.png"));

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (Generator.finalMap[i][j] == 0) {
					Cell cell = new Cell();

					cell.setTile(new StaticTiledMapTile(myV[Math.abs(rand
							.nextInt()) % 3]));

					layer1.setCell(i, j, cell);
				} else {
					Vector2 aux = new Vector2(i, j);
					if (Generator.finalMap[i][j - 1] == 0
							&& Generator.finalMap[i + 1][j - 1] == 0
							&& Generator.finalMap[i][j + 1] == 1
							&& Generator.finalMap[i + 1][j] == 1
							&& Generator.finalMap[i + 1][j + 1] == 1) {
						myDoors.add(aux);
					}
					myArr.add(aux);
				}
			}
		}

		layers.add(layer1);

		return map;
	}

	public static Vector2 getNormalPos() {
		int i = Math.abs(rand.nextInt()) % myArr.size;
		Vector2 aux = myArr.get(i);
		myArr.removeIndex(i);
		return aux;
	}
	
	public static Vector2 getDoorPos() {
		int i = Math.abs(rand.nextInt()) % myDoors.size;
		Vector2 aux = myDoors.get(i);
		myArr.removeValue(aux, false);
		myDoors.removeIndex(i);
		return aux;
	}

	public static BoundingBox getBounds(float a, float b, float w, float h) {
		return new BoundingBox(new Vector3(a, b, 0), new Vector3(a + w, b + h,
				0));
	}
}
