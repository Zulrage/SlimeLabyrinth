package com.slime.labyrinth.play.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;

/**
 * Shared functions for the differents slime
 * 
 * @author Bertrand
 *
 */
public abstract class AbstractSlime implements Entity {

	protected Body body;
	protected Fixture fixture;
	protected final float WIDTH, HEIGHT;
	protected int color;

	public AbstractSlime(World world, float x, float y, float width, int color) {
		WIDTH = width;
		HEIGHT = width;
		this.color = color;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		CircleShape shape = new CircleShape();
		shape.setRadius(WIDTH / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		body = world.createBody(bodyDef);
		body.setUserData(defineUserData());
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}

	/**
	 * Set the {@link GenericUserData} to handle the logic of
	 * drawing/colliding/... when the world step or the game render
	 * 
	 * @return a GenericUserData set in the class
	 */
	abstract public GenericUserData defineUserData();

	@Override
	public Body getBody() {
		return body;
	}

	/**
	 * Change the color and the GenericUserData
	 */
	abstract public void changeColor();

}
