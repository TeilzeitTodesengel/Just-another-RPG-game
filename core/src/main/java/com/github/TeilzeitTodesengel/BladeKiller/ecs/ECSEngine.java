package com.github.TeilzeitTodesengel.BladeKiller.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.github.TeilzeitTodesengel.BladeKiller.GameCore;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.AnimationComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.B2DComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.component.PlayerComponent;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.system.AnimationSystem;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.system.PlayerCameraSystem;
import com.github.TeilzeitTodesengel.BladeKiller.ecs.system.PlayerMovementSystem;
import com.github.TeilzeitTodesengel.BladeKiller.view.AnimationType;

import static com.github.TeilzeitTodesengel.BladeKiller.GameCore.*;

public class ECSEngine extends PooledEngine {
	public static final ComponentMapper<PlayerComponent> playerCmpMapper = ComponentMapper.getFor(PlayerComponent.class);
	public static final ComponentMapper<B2DComponent> b2DCmpMapper = ComponentMapper.getFor(B2DComponent.class);
	public static final ComponentMapper<AnimationComponent> aniCmpMapper = ComponentMapper.getFor(AnimationComponent.class);

	private final World world;


	public ECSEngine(GameCore context) {
		super();

		world = context.getWorld();


		this.addSystem(new PlayerMovementSystem(context));
		this.addSystem(new PlayerCameraSystem(context));
		this.addSystem(new AnimationSystem(context));
	}



	public void createPlayer(final Vector2 playerSpawnLocation, final float width, final float height) {
		final Entity player = this.createEntity();

		// add player component
		final PlayerComponent playerComponent = this.createComponent(PlayerComponent.class);
		playerComponent.speed.set(3,3);
		player.add(playerComponent);

		// add box2d component
		final B2DComponent b2DComponent = this.createComponent(B2DComponent.class);
		BODY_DEF.position.set(playerSpawnLocation.x, playerSpawnLocation.y + height * 0.5f);
		BODY_DEF.type = BodyDef.BodyType.DynamicBody;
		b2DComponent.body = world.createBody(BODY_DEF);
		b2DComponent.body.setUserData("PLAYER");
		b2DComponent.width = width;
		b2DComponent.height = height;
		b2DComponent.renderPosition.set(b2DComponent.body.getPosition());


		FIXTURE_DEF.filter.categoryBits = BIT_PLAYER;
		FIXTURE_DEF.density = 1;
		FIXTURE_DEF.filter.maskBits = BIT_GROUND;
		final PolygonShape pShape = new PolygonShape();
		pShape.setAsBox(width * 0.5f, height * 0.5f);
		FIXTURE_DEF.shape = pShape;
		b2DComponent.body.createFixture(FIXTURE_DEF);
		pShape.dispose();
		player.add(b2DComponent);

		// animation component
		final AnimationComponent animationComponent = this.createComponent(AnimationComponent.class);
		animationComponent.aniType= AnimationType.HERO_MOVE_DOWN;
		animationComponent.width = 32 * UNIT_SCALE  * 0.7f;
		animationComponent.height = 36 * UNIT_SCALE * 0.7f;
		player.add(animationComponent);

		this.addEntity(player);
	}


}
