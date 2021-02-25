package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.audio.AudioManager;
import com.github.TeilzeitTodesengel.BladeKiller.input.GameKeyInputListener;
import com.github.TeilzeitTodesengel.BladeKiller.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, GameKeyInputListener {
	protected final GameCore context;
	protected final FitViewport viewport;
	protected final World world;
	protected final Stage stage;
	protected final T screenUI;
	protected final InputManager inputManager;
	protected final AudioManager audioManager;

	public AbstractScreen(final GameCore context) {
		this.context = context;
		viewport = context.getScreenViewport();
		this.world = context.getWorld();
		inputManager = context.getInputManager();

		stage = context.getStage();
		screenUI = getScreenUI(context);
		audioManager = context.getAudioManager();
	}

	protected abstract T getScreenUI(final GameCore context);

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		inputManager.addInputListener(this);
		stage.addActor(screenUI);
	}

	@Override
	public void hide() {
		inputManager.removeInputListener(this);
		stage.getRoot().removeActor(screenUI);
	}
}
