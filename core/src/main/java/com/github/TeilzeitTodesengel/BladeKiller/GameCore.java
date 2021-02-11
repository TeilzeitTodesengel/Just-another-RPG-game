package com.github.TeilzeitTodesengel.BladeKiller;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.TeilzeitTodesengel.BladeKiller.screen.ScreenType;

import java.util.EnumMap;

public class GameCore extends Game {
	private static final String TAG = GameCore.class.getSimpleName();
	private FitViewport screenViewport;

	private EnumMap<ScreenType, Screen> screenCache;

	public static final short BIT_GROUND = 1 << 0;
	public static final short BIT_PLAYER = 1 << 1;

	private World world;

	private WorldContactListener worldContactListener;

	private Box2DDebugRenderer box2DDebugRenderer;

	private float accumulator;
	private static final float FIXED_TIME_STEP = 1 / 60f;

 	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		accumulator = 0;

		Box2D.init();
		world = new World(new Vector2(0,0), true);
		worldContactListener = new WorldContactListener();
		world.setContactListener(worldContactListener);
		box2DDebugRenderer = new Box2DDebugRenderer();

		screenViewport = new FitViewport(9,16);
		screenCache = new EnumMap<ScreenType, Screen>(ScreenType.class);
		setScreen(ScreenType.LOADING);
	}

	public FitViewport getScreenViewport() {
		return screenViewport;
	}

	public World getWorld() {
		return world;
	}

	public Box2DDebugRenderer getBox2DDebugRenderer() {
		return box2DDebugRenderer;
	}

	public void setScreen (final ScreenType screenType) {
		final Screen screen = screenCache.get(screenType);
		if (screen == null){
			// Screen not yet created -> create it
			try {
				Gdx.app.debug(TAG, "Creating new Scree " + screenType);
				final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), GameCore.class).newInstance(this);
				screenCache.put(screenType, newScreen);
				setScreen(newScreen);
			} catch (ReflectionException e) {
				throw  new GdxRuntimeException("Scree" + screenType + "could not be created.");
			}
		} else {
			Gdx.app.debug(TAG, "Switching to screen: " + screenType);
			setScreen(screen);
		}
	}

	@Override
	public void render() {
		super.render();

		//Gdx.app.debug(TAG, "" + Gdx.graphics.getDeltaTime());
		accumulator += Math.min(0.25f, Gdx.graphics.getDeltaTime());
		while (accumulator >= FIXED_TIME_STEP) {
			world.step(FIXED_TIME_STEP,6,2);
			accumulator -= FIXED_TIME_STEP;
		}

		final float alpha = accumulator / FIXED_TIME_STEP;

	}

	@Override
	public void dispose() {
		super.dispose();
		box2DDebugRenderer.dispose();
		world.dispose();
	}


}