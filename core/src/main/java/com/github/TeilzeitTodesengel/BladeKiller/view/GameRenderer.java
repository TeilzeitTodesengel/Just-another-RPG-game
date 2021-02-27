package com.github.TeilzeitTodesengel.BladeKiller.view;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.ECSEngine;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.AnimationComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.B2DComponent;
import com.github.TeilzeitTodesengel.BladeKiller.map.Map;
import com.github.TeilzeitTodesengel.BladeKiller.map.MapListener;

import java.util.EnumMap;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.UNIT_SCALE;

public class GameRenderer implements Disposable, MapListener {
	private final String TAG = GameRenderer.class.getSimpleName();

	private final OrthographicCamera gameCamera;
	private final FitViewport viewport;
	private final SpriteBatch spriteBatch;
	private final AssetManager assetManager;
	private final EnumMap<AnimationType, Animation<Sprite>> animationCache;
	private final ObjectMap<String, TextureRegion[][]> regionCache;

	private final ImmutableArray<Entity> animatedEntities;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final Array<TiledMapTileLayer> tiledMapLayers;

	private final GLProfiler profiler;
	private final Box2DDebugRenderer box2DDebugRenderer;
	private final World world;


	public GameRenderer(final GameCore context) {
		assetManager = context.getAssetManager();
		viewport = context.getScreenViewport();
		gameCamera = context.getGameCamera();
		spriteBatch = context.getSpriteBatch();

		animationCache = new EnumMap<AnimationType, Animation<Sprite>>(AnimationType.class);
		regionCache = new ObjectMap<String, TextureRegion[][]>();

		animatedEntities = context.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, B2DComponent.class).get());

		mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, spriteBatch);
		context.getMapManager().addMapLister(this);
		tiledMapLayers = new Array<TiledMapTileLayer>();

		profiler = new GLProfiler(Gdx.graphics);
		profiler.disable();
		if (profiler.isEnabled()) {
			box2DDebugRenderer = new Box2DDebugRenderer();
			world = context.getWorld();
		} else {
			box2DDebugRenderer = null;
			world = null;
		}


	}

	public void render(final float alpha) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewport.apply(false);

		mapRenderer.setView(gameCamera);
		spriteBatch.begin();
		if (mapRenderer.getMap() != null) {
			AnimatedTiledMapTile.updateAnimationBaseTime();
			for (final TiledMapTileLayer layer : tiledMapLayers) {
				mapRenderer.renderTileLayer(layer);
			}
		}


		for (final Entity entity : animatedEntities) {
			renderEntity(entity, alpha);
		}
		spriteBatch.end();

		if (profiler.isEnabled()) {
			Gdx.app.debug(TAG, "Bindings: " + profiler.getTextureBindings());
			Gdx.app.debug(TAG, "Drawcalls: " + profiler.getDrawCalls());
			profiler.reset();

			box2DDebugRenderer.render(world, gameCamera.combined);
		}
	}

	private void renderEntity(final Entity entity, float alpha) {
		final B2DComponent b2DComponent = ECSEngine.b2DCmpMapper.get(entity);
		final AnimationComponent aniComponent = ECSEngine.aniCmpMapper.get(entity);

		if (aniComponent.aniType  != null) {
			final Animation<Sprite> animation = getAnimation(aniComponent.aniType);
			final Sprite frame = animation.getKeyFrame(aniComponent.aniTime);
			frame.setBounds(b2DComponent.renderPosition.x - aniComponent.width * 0.5f, b2DComponent.renderPosition.y - aniComponent.height / 4, aniComponent.width, aniComponent.height);
			frame.draw(spriteBatch);
		}

		b2DComponent.renderPosition.lerp(b2DComponent.body.getPosition(), alpha);
//		dummySprite.setBounds(b2DComponent.renderPosition.x - b2DComponent.width * 0.5f, b2DComponent.renderPosition.y - b2DComponent.height * 0.5f, b2DComponent.width, b2DComponent.height);
//		dummySprite.draw(spriteBatch);
	}

	private Animation<Sprite> getAnimation(AnimationType aniType) {
		Animation<Sprite> animation = animationCache.get(aniType);
		if (animation == null) {
			// create Animation
			Gdx.app.debug(TAG, "Creating Animation of Type: " + aniType);
			TextureRegion[][] textureRegions = regionCache.get(aniType.getAtlasKey());
			if (textureRegions == null) {
				Gdx.app.debug(TAG, "Creating Texture Regions for " + aniType.getAtlasKey());
				final TextureAtlas.AtlasRegion atlasRegion = assetManager.get(aniType.getAtlasPath(), TextureAtlas.class).findRegion(aniType.getAtlasKey());
				textureRegions = atlasRegion.split(32,36);
				regionCache.put(aniType.getAtlasKey(), textureRegions);
			}
			animation = new Animation<Sprite>(aniType.getFrameTime(), getKeyFrames(textureRegions[aniType.getRowIndex()]));
			animation.setPlayMode(Animation.PlayMode.LOOP);
			animationCache.put(aniType, animation);
		}
		return animation;
	}

	private Sprite[] getKeyFrames(TextureRegion[] textureRegion) {
		final Sprite[] keyFrames = new Sprite[textureRegion.length];

		int i = 0;
		for (final TextureRegion region : textureRegion) {
			final Sprite sprite = new Sprite(region);
			sprite.setOriginCenter();
			keyFrames[i++] = sprite;
		}

		return keyFrames;
	}

	@Override
	public void dispose() {
		if (box2DDebugRenderer != null) {
			box2DDebugRenderer.dispose();
		}
		mapRenderer.dispose();

	}

	@Override
	public void mapChange(Map map) {
		mapRenderer.setMap(map.getTiledMap());
		map.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tiledMapLayers);
	}
}
