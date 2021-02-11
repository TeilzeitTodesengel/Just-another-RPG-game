package com.github.TeilzeitTodesengel.BladeKiller.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;

public abstract class AbstractScreen implements Screen {
	protected final GameCore context;
	protected final FitViewport viewport;
	protected final World world;
	protected  final Box2DDebugRenderer box2DDebugRenderer;

	public AbstractScreen(final GameCore context) {
		this.context = context;
		viewport = context.getScreenViewport();
		this.world = context.getWorld();
		this.box2DDebugRenderer = context.getBox2DDebugRenderer();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}
