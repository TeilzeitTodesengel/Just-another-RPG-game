package com.github.TeilzeitTodesengel.BladeKiller.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.ECSEngine;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.B2DComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.PlayerComponent;

public class PlayerCameraSystem extends IteratingSystem {
	private final OrthographicCamera gameCamera;

	public PlayerCameraSystem(final GameCore context) {
		super(Family.all(PlayerComponent.class, B2DComponent.class).get());

		gameCamera = context.getGameCamera();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		gameCamera.position.set(ECSEngine.b2DCmpMapper.get(entity).renderPosition, 0);
		gameCamera.update();

	}
}
