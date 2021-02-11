package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.*;

public class GameScreen extends AbstractScreen {
	private final BodyDef bodyDef;
	private final FixtureDef fixtureDef;

	private final Body player;

	private final AssetManager assetManager;
	private final OrthographicCamera gameCamera;

	private final OrthogonalTiledMapRenderer mapRenderer;

	public GameScreen(final GameCore context) {
		 super(context);

		 assetManager = context.getAssetManager();
		 gameCamera = context.getGameCamera();

		 mapRenderer = new OrthogonalTiledMapRenderer(null,UNIT_SCALE , context.getSpriteBatch());

		 bodyDef = new BodyDef();
		 fixtureDef = new FixtureDef();

		bodyDef.position.set(4.5f, 3);
		bodyDef.gravityScale = 1;
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		player  = world.createBody(bodyDef);
		player.setUserData("PLAYER");

		fixtureDef.density = 1;
		fixtureDef.isSensor = false;
		fixtureDef.restitution = 0;
		fixtureDef.friction = 0.2f;
		fixtureDef.filter.categoryBits = BIT_PLAYER;
		fixtureDef.filter.maskBits = BIT_GROUND;
		final PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(0.5f, 0.5f);
		fixtureDef.shape = pShape;
		player.createFixture(fixtureDef);
		pShape.dispose();


		// create a box
		bodyDef.position.set(0, 0);
		bodyDef.gravityScale = 1;
		bodyDef.type = BodyDef.BodyType.StaticBody;
		final Body body = world.createBody(bodyDef);
		body.setUserData("Box");

		fixtureDef.isSensor = false;
		fixtureDef.restitution = 0.75f;
		fixtureDef.friction = 0.2f;
		fixtureDef.filter.categoryBits = BIT_GROUND;
		fixtureDef.filter.maskBits = BIT_PLAYER;
		final ChainShape chainShape = new ChainShape();
		chainShape.createLoop(new float[] {1, 1, 1, 15, 8, 15, 8, 1});
		fixtureDef.shape = chainShape;
		body.createFixture(fixtureDef);
		chainShape.dispose();

	}

	@Override
	public void show() {
		mapRenderer.setMap(assetManager.get("Map.tmx", TiledMap.class));

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
	public void hide() {

	}

	@Override
	public void dispose() {
		mapRenderer.dispose();

	}
}
