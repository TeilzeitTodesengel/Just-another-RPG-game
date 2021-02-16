package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> {

	private final AssetManager assetManager;

	public LoadingScreen(final GameCore context) {
		super(context);

		this.assetManager = context.getAssetManager();
		assetManager.load("Map.tmx", TiledMap.class);


	}

	@Override
	protected LoadingUI getScreenUI(Skin skin) {
		return new LoadingUI(skin);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (assetManager.update() && Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
			context.setScreen(ScreenType.GAME);
		}
		screenUI.setProgress(assetManager.getProgress());
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}


	@Override
	public void dispose() {

	}
}
