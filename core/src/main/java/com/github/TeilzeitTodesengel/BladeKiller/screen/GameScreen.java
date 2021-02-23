package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.audio.AudioType;
import com.github.TeilzeitTodesengel.BladeKiller.input.GameKeys;
import com.github.TeilzeitTodesengel.BladeKiller.input.InputManager;
import com.github.TeilzeitTodesengel.BladeKiller.map.Map;
import com.github.TeilzeitTodesengel.BladeKiller.map.MapListener;
import com.github.TeilzeitTodesengel.BladeKiller.map.MapManager;
import com.github.TeilzeitTodesengel.BladeKiller.map.MapType;
import com.github.TeilzeitTodesengel.BladeKiller.ui.GameUI;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.UNIT_SCALE;

public class GameScreen extends AbstractScreen<GameUI> implements MapListener {
	private final AssetManager assetManager;
	private final OrthographicCamera gameCamera;
	private final OrthogonalTiledMapRenderer mapRenderer;
	private final GLProfiler profiler;
	private final MapManager mapManager;
	private boolean isMusicLoaded;
	public GameScreen(final GameCore context) {
		super(context);

		assetManager = context.getAssetManager();
		gameCamera = context.getGameCamera();

		profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();

		mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getSpriteBatch());

		mapManager = context.getMapManager();
		mapManager.addMapLister(this);
		mapManager.setMap(MapType.MAP_1);

		// load audio
		isMusicLoaded = false;
		for (final AudioType audioType : AudioType.values()) {
			if (audioType.isMusic()) assetManager.load(audioType.getFilePath(), Music.class);
			else assetManager.load(audioType.getFilePath(), Sound.class);
		}

		context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartLocation(), 0.75f, 0.75f);

	}

	@Override
	protected GameUI getScreenUI(GameCore context) {
		return new GameUI(context);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		/* Gdx.app.debug("RenderInfo", "Bindings: " + profiler.getTextureBindings());
		 Gdx.app.debug("RenderInfo", "DrawCalls: " + profiler.getDrawCalls());
		 profiler.reset(); */

		assetManager.update();
		if (!isMusicLoaded && assetManager.isLoaded(AudioType.BACKGROUND.getFilePath())) {
			isMusicLoaded = true;
			audioManager.playAudio(AudioType.BACKGROUND);
		}

		viewport.apply(false);
		if (mapRenderer.getMap() != null) {
			mapRenderer.setView(gameCamera);
			mapRenderer.render();
		}
		box2DDebugRenderer.render(world, viewport.getCamera().combined);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
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

	@Override
	public void keyPressed(InputManager manager, GameKeys key) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			mapManager.setMap(MapType.MAP_1);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			mapManager.setMap(MapType.MAP_2);
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKeys key) {
	}

	@Override
	public void mapChange(final Map map) {
		mapRenderer.setMap(mapManager.getCurrentMap().getTiledMap()); // <--- this is most likely the part you are missing
	}
}
