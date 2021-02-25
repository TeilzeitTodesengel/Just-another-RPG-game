package com.github.TeilzeitTodesengel.BladeKiller.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;

import java.util.EnumMap;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.BIT_GROUND;

public class MapManager {
	public static final String TAG = MapManager.class.getSimpleName();

	private final World world;
	private final Array<Body> bodies;

	private final AssetManager assetManager;

	private MapType currentMapType;
	private Map currentMap;
	private final EnumMap<MapType, Map> mapCache;
	private final Array<MapListener> listeners;

	public MapManager(final GameCore context) {
		currentMapType = null;
		currentMap = null;
		world = context.getWorld();
		assetManager = context.getAssetManager();
		bodies = new Array<Body>();
		mapCache = new EnumMap<MapType, Map>(MapType.class);
		listeners = new Array<MapListener>();
	}

	public void addMapLister(final MapListener listener) {
		listeners.add(listener);
	}

	public void setMap(final MapType type) {
		if (currentMapType == type){
			// map is already set
			return;
		}

		if (currentMap != null) {
			world.getBodies(bodies);
			destroyCollisionAreas();
		}

		// set new map
		Gdx.app.debug(TAG, "Changing to map " + type);
		currentMap = mapCache.get(type);
		if (currentMap == null) {
			Gdx.app.debug(TAG, "Creating new map of type " + type);
			final TiledMap tiledMap = assetManager.get(type.getFilePath(), TiledMap.class);
			currentMap = new Map(tiledMap);
			mapCache.put(type, currentMap);
		}

		// create map entities/ bodies
		spawnCollisionAreas();

		for (final MapListener listener : listeners) {
		listener.mapChange(currentMap);
		}
	}

	private void destroyCollisionAreas() {
		for (final Body body : bodies) {
			if ("GROUND".equals(body.getUserData())) {
				world.destroyBody(body);
			}
		}
	}

	private void spawnCollisionAreas() {
		GameCore.resetBodyAndFixtureDefinition();
		for (final CollisionArea collisionArea : currentMap.getCollisionAreas()) {
			final BodyDef bodyDef = new BodyDef();
			final FixtureDef fixtureDef = new FixtureDef();

			// create room
			bodyDef.position.set(collisionArea.getX(), collisionArea.getY());
			bodyDef.fixedRotation = true;
			final Body body = world.createBody(bodyDef);
			body.setUserData("GROUND");


			fixtureDef.filter.categoryBits = BIT_GROUND;
			fixtureDef.filter.maskBits = -1;
			final ChainShape chainShape = new ChainShape();
			chainShape.createChain(collisionArea.getVertices());
			fixtureDef.shape = chainShape;
			body.createFixture(fixtureDef);
			chainShape.dispose();

		}
	}

	public void setCurrentMap(Map currentMap) {
		this.currentMap = currentMap;
	}

	public Map getCurrentMap() {
		return currentMap;
	}
}
