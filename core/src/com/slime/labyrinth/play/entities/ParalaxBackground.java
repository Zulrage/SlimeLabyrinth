package com.slime.labyrinth.play.entities;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.slime.labyrinth.play.entities.collide.NotCollidingEntityCollide;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameStatics;
import com.slime.labyrinth.utils.GameTextures;

/**
 * Class creating a background moving down and teleporting itself on the top
 * when reaching the bottom of the screen
 * 
 * @author Bertrand
 *
 */
public class ParalaxBackground implements IMovable, Entity {

	private Vector2 velocity = new Vector2();
	private Body body;
	private float bottom, top;

	public ParalaxBackground(World world, float x, float y, float width, float height,
			CopyOnWriteArrayList<Entity> entites, boolean isTop) {
		this(world, x, y, width, height, entites, GameStatics.BASIC_DROP_VELOCITY, isTop);
	}

	public ParalaxBackground(World world, float x, float y, float width, float height,
			CopyOnWriteArrayList<Entity> entites, float dropVelocity, boolean isTop) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y + (isTop ? height * 2 : 0));
		velocity.y = -dropVelocity;
		bottom = y - height * 2;
		top = y + height * 2;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		body = world.createBody(bodyDef);
		body.setUserData(new GenericUserData(EnumUserDataId.ROOM, new Sprite(GameTextures.ROOM_TEXTURE), width * 2,
				height * 2 + 0.2f, new NotCollidingEntityCollide()));
		entites.add(this);
		body.createFixture(fixtureDef);
		shape.dispose();
	}

	@Override
	public void move() {
		body.setLinearVelocity(velocity);
		if (body.getPosition().y < bottom) {
			body.setTransform(new Vector2(body.getPosition().x, top), body.getAngle());
		}
	}

	@Override
	public void stop() {
		body.setLinearVelocity(new Vector2(0, 0));
	}

	@Override
	public Body getBody() {
		return body;
	}

}
