package com.slime.labyrinth.play.entities.collide;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Handle the logic when a BountySlime collide something else </br>
 * Preferably handle for each other entities
 * @author Bertrand
 *
 */
public class PlayerSlimeCollide implements ICollidable {

	@Override
	public boolean canCollide(Fixture fixtureA, Fixture fixtureB) {
		return true;
	}

	@Override
	public void onCollide(Contact contact, Manifold oldManifold) {

	}

}
