package com.github.TeilzeitTodesengel.BladeKiller.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.UNIT_SCALE;

public class Map {
	private static final String TAG = Map.class.getSimpleName();

	private final TiledMap tiledMap;
	private final Array<CollisionArea> collisionAreas;
	private final Vector2 startLocation;

	public Map(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		collisionAreas = new Array<CollisionArea>();

		parseCollisionLayer();
		startLocation = new Vector2();
		parsePlayerStartLocation();
	}

	private void parseCollisionLayer() {
		final MapLayer collisionLayer = tiledMap.getLayers().get("collision");

		if (collisionLayer == null) {
			Gdx.app.debug(TAG, "There is no collision layer");
		}

		final MapObjects mapObjects = collisionLayer.getObjects();

		for (final MapObject mapObj : mapObjects) {
			if (mapObj instanceof RectangleMapObject){
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObj;
				final Rectangle rectangle = rectangleMapObject.getRectangle();
				final float[] rectVertices = new float[10];

				// left bottom
				rectVertices[0] = 0;
				rectVertices[1] = 0;

				// left top
				rectVertices[2] = 0;
				rectVertices[3] = rectangle.height;

				// right top
				rectVertices[4] = rectangle.width;
				rectVertices[5] = rectangle.height;

				// right bottom
				rectVertices[6] = rectangle.width;
				rectVertices[7] = 0;
				// left bottom
				rectVertices[8] = 0;
				rectVertices[9] = 0;
				collisionAreas.add(new CollisionArea(rectangle.x, rectangle.y, rectVertices));

			} else if (mapObj instanceof PolylineMapObject){
				final PolylineMapObject polylineMapObject = (PolylineMapObject) mapObj;
				final Polyline polyline = polylineMapObject.getPolyline();
				collisionAreas.add(new CollisionArea(polyline.getX(), polyline.getY(), polyline.getVertices()));
			} else {
				Gdx.app.debug(TAG, "MapObject of type " + mapObj + "is not supported for Collision Layer.");
			}
		}
	}

	private void parsePlayerStartLocation() {
		final MapLayer startLocationLayer = tiledMap.getLayers().get("PlayerStartLocation");
		if (startLocationLayer == null) {
			Gdx.app.debug(TAG, "There is no startlocation layer");
			return;
		}

		final MapObjects objects = startLocationLayer.getObjects();
		for (final MapObject mapObj : objects) {
			if (mapObj instanceof RectangleMapObject) {
				final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObj;
				final Rectangle rectangle = rectangleMapObject.getRectangle();
				startLocation.set(rectangle.x * UNIT_SCALE, rectangle.getY() * UNIT_SCALE);
			}
		}

	}

	public Array<CollisionArea> getCollisionAreas() {
		return collisionAreas;
	}

	public Vector2 getStartLocation() {
		return startLocation;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}
}
