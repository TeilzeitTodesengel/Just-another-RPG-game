package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.audio.AudioType;
import com.github.TeilzeitTodesengel.BladeKiller.input.GameKeys;
import com.github.TeilzeitTodesengel.BladeKiller.input.InputManager;
import com.github.TeilzeitTodesengel.BladeKiller.ui.LoadingUI;

public class LoadingScreen extends AbstractScreen<LoadingUI> {

	private final AssetManager assetManager;
	private boolean isMusicLoaded;

	public LoadingScreen(final GameCore context) {
		super(context);

		this.assetManager = context.getAssetManager();

		// load map
		assetManager.load(context.getMap(), TiledMap.class);

		// load audio
		isMusicLoaded = false;
		for (final AudioType audioType : AudioType.values()) {
			if (audioType.isMusic()) assetManager.load(audioType.getFilePath(), Music.class);
			else assetManager.load(audioType.getFilePath(), Sound.class);
		}
	}


	@Override
	protected LoadingUI getScreenUI(GameCore context) {
		return new LoadingUI(context);
	}


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		assetManager.update();
		if (!isMusicLoaded && assetManager.isLoaded(AudioType.INTRO.getFilePath())) {
			isMusicLoaded = true;
			audioManager.playAudio(AudioType.INTRO);
		}

		screenUI.setProgress(assetManager.getProgress());
	}


	@Override
	public void show() {
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
		audioManager.stopCurrentMusic();
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

	@Override
	public void keyPressed(InputManager manager, GameKeys key) {
		audioManager.playAudio(AudioType.SELECT);
		if (assetManager.getProgress() >= 1) {
			context.setScreen(ScreenType.GAME);
		}
	}

	@Override
	public void keyUp(InputManager manager, GameKeys key) {

	}
}
