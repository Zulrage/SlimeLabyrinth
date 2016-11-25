package com.slime.labyrinth.play.entities.collide;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * ICollidable that do nothing when colliding other Entities
 * @author Bertrand
 *
 */
public class NotCollidingEntityCollide implements ICollidable {

	@Override
	public boolean canCollide(Fixture fixtureA, Fixture fixtureB) {
		return false;
	}

	@Override
	public void onCollide(Contact contact, Manifold oldManifold) {
		// Nothing happen
	}

}
