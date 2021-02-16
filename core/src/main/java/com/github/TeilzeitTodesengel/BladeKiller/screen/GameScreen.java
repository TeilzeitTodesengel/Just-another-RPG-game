package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.map.CollisionArea;
import com.github.TeilzeitTodesengel.BladeKiller.map.Map;
import com.github.TeilzeitTodesengel.BladeKiller.ui.GameUI;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.*;

public class GameScreen extends AbstractScreen {
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;

	private Body player;

	private final AssetManager assetManager;
	private final OrthographicCamera gameCamera;

	private final OrthogonalTiledMapRenderer mapRenderer;

	private final GLProfiler profiler;

	private Map map;

	public GameScreen(final GameCore context) {
		 super(context);

		 assetManager = context.getAssetManager();
		 gameCamera = context.getGameCamera();

		 profiler = new GLProfiler(Gdx.graphics);
		 profiler.enable();

		 mapRenderer = new OrthogonalTiledMapRenderer(null,UNIT_SCALE , context.getSpriteBatch());

		 bodyDef = new BodyDef();
		 fixtureDef = new FixtureDef();


		final TiledMap tiledMap = assetManager.get("Map.tmx", TiledMap.class);
		mapRenderer.setMap(assetManager.get("Map.tmx", TiledMap.class));
		map = new Map(tiledMap);

		spawnCollisionAreas();
		spawnPlayer();

	}

	@Override
	protected Table getScreenUI(Skin skin) {
		return new GameUI(stage, skin);
	}

	private void resetBodyAndFixtureDefinition() {
		bodyDef.position.set(0,0);
		bodyDef.gravityScale = 1;
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.fixedRotation = false;

		fixtureDef.density = 0;
		fixtureDef.isSensor = false;
		fixtureDef.restitution = 0;
		fixtureDef.friction = 0.2f;
		fixtureDef.filter.categoryBits = 0x0001;
		fixtureDef.filter.maskBits = -1;
		fixtureDef.shape = null;
	}

	private void spawnCollisionAreas() {
		for (final CollisionArea collisionArea : map.getCollisionAreas()) {
			resetBodyAndFixtureDefinition();

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

	private void spawnPlayer() {
		resetBodyAndFixtureDefinition();
		bodyDef.position.set(map.getStartLocation());
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		player  = world.createBody(bodyDef);
		player.setUserData("PLAYER");


		fixtureDef.filter.categoryBits = BIT_PLAYER;
		fixtureDef.density = 1;
		fixtureDef.filter.maskBits = BIT_GROUND;
		final PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(0.5f, 0.5f);
		fixtureDef.shape = pShape;
		player.createFixture(fixtureDef);
		pShape.dispose();
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		final float speedX;
		final float speedY;

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			speedX = -3;
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			speedX = 3;
		} else {
			speedX = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			speedY = -3;
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			speedY = 3;
		} else {
			speedY = 0;
		}

		player.applyLinearImpulse(
				(speedX - player.getLinearVelocity().x) * player.getMass(),
				(speedY - player.getLinearVelocity().y) * player.getMass(),
				player.getWorldCenter().x,
				player.getWorldCenter().y,
				true
		);

		// Gdx.app.debug("RenderInfo", "Bindings: " + profiler.getTextureBindings());
		// Gdx.app.debug("RenderInfo", "DrawCalls: " + profiler.getDrawCalls());
		// profiler.reset();

		viewport.apply(true);
		mapRenderer.setView(gameCamera);
		mapRenderer.render();
		box2DDebugRenderer.render(world, viewport.getCamera().combined);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width,height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}


	@Override
	public void dispose() {
		mapRenderer.dispose();

	}
}
