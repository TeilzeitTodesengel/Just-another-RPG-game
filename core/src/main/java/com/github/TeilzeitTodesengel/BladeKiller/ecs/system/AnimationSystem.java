package com.github.TeilzeitTodesengel.BladeKiller.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.ECSEngine;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.AnimationComponent;

public class AnimationSystem extends IteratingSystem {
	public AnimationSystem(GameCore context) {
		super(Family.all(AnimationComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);
		if (animationComponent.aniType != null) {
			animationComponent.aniTime += deltaTime;
		}
	}
}
