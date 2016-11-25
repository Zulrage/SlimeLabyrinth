package com.slime.labyrinth.play.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.slime.labyrinth.play.entities.collide.BoxCollide;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameStatics;
import com.slime.labyrinth.utils.GameTextures;

/**
 * Class to create a box that moves down
 * Automatically create the userData and the body
 * 
 * @author Bertrand
 *
 */
public class Box implements IMovable, Entity {

	protected Body body;
	protected Fixture fixture;
	protected final float WIDTH, HEIGHT;
	private Vector2 velocity = new Vector2();
	private float bottom;

	/**
	 * Create a slime with a define speed (When moving down
	 * 
	 * @param world : The world to place the slime
	 * @param x : The X position
	 * @param y : Y position
	 * @param width : Width and height
	 * @param color : Color of the slime
	 * @param bottom : End of the screen to handle delete flags
	 */
	public Box(World world, float x, float y, float width, float height, float bottom) {
		this(world, x, y, width, height, bottom, GameStatics.BASIC_DROP_VELOCITY);
	}
	
	/**
	 * Create a slime 
	 * 
	 * @param world : The world to place the slime
	 * @param x : The X position
	 * @param y : Y position
	 * @param width : Width and height
	 * @param color : Color of the slime
	 * @param bottom : End of the screen to handle delete flags
	 * @param dropVelocity : Speed
	 */
	public Box(World world, float x, float y, float width, float height, float bottom, float dropVelocity) {
		WIDTH = width;
		HEIGHT = height;
		velocity.y = -dropVelocity;
		this.bottom = bottom;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, HEIGHT / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		body = world.createBody(bodyDef);
		body.setUserData(
				new GenericUserData(EnumUserDataId.WALL, new Sprite(GameTextures.WALL_TEXTURE), WIDTH, HEIGHT, new BoxCollide()));
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}

	public Body getBody() {
		return body;
	}

	@Override
	public void move() {
		body.setLinearVelocity(velocity);
		if (body.getPosition().y < bottom) {
			GenericUserData userData = (GenericUserData) body.getUserData();
			if (userData != null) {
				userData.setFlaggedForDelete(true);
			}
		}
	}
	
	@Override
	public void stop() {
		body.setLinearVelocity(new Vector2(0,0));
	}
}
