package com.github.TeilzeitTodesengel.BladeKiller.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.ECSEngine;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.AnimationComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.B2DComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.PlayerComponent;
import com.github.TeilzeitTodesengel.BladeKiller.view.AnimationType;

import static com.badlogic.ashley.core.Family.*;

public class PlayerAnimationSystem extends IteratingSystem {
	public PlayerAnimationSystem(GameCore context) {
		super(all(AnimationComponent.class, PlayerComponent.class, B2DComponent.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		final B2DComponent b2DComponent = ECSEngine.b2DCmpMapper.get(entity);
		final AnimationComponent animationComponent = ECSEngine.aniCmpMapper.get(entity);

		if (b2DComponent.body.getLinearVelocity().equals(Vector2.Zero)) {
			// Player does not Move
			animationComponent.aniTime = 0;
		}else if (b2DComponent.body.getLinearVelocity().x > 0) {
			// Hero move Right
			animationComponent.aniType = AnimationType.HERO_MOVE_RIGHT;
		} else if (b2DComponent.body.getLinearVelocity().x < 0) {
			// Hero move left
			animationComponent.aniType = AnimationType.HERO_MOVE_LEFT;
		} else  if (b2DComponent.body.getLinearVelocity().y > 0 ) {
			// Hero moves up
			animationComponent.aniType = AnimationType.HERO_MOVE_UP;
		}else if (b2DComponent.body.getLinearVelocity().y < 0) {
			animationComponent.aniType = AnimationType.HERO_MOVE_DOWN;
		}

	}
}
