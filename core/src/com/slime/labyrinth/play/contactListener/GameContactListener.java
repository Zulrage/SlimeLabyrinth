package com.slime.labyrinth.play.contactListener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;

/**
 * Class that handle how the objects should collide.</br> Use the class
 * {@link GenericUserData} to handle each contacts
 * 
 * @author Bertrand
 *
 */
public class GameContactListener implements ContactListener, ContactFilter {

	@Override
	public void beginContact(Contact contact) {
		// No need
	}

	@Override
	public void endContact(Contact contact) {
		// No need
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		if (contact.getFixtureA() != null && contact.getFixtureB() != null) {
			GenericUserData dataA = (GenericUserData) contact.getFixtureA().getBody().getUserData();
			GenericUserData dataB = (GenericUserData) contact.getFixtureB().getBody().getUserData();
			if (dataA != null && dataB != null) {
				dataA.getContactReaction().onCollide(contact, oldManifold);
				dataB.getContactReaction().onCollide(contact, oldManifold);
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

	@Override
	public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
		// Null check and call the canCollide function of each ICollidable stored in the GenericUserData.
		if (fixtureA != null && fixtureA.getBody() != null && fixtureB != null && fixtureB.getBody() != null) {
			GenericUserData dataA = (GenericUserData) fixtureA.getBody().getUserData();
			GenericUserData dataB = (GenericUserData) fixtureB.getBody().getUserData();
			if (dataA != null && dataB != null) {
				if (!dataA.getContactReaction().canCollide(fixtureA, fixtureB)
						|| !dataB.getContactReaction().canCollide(fixtureA, fixtureB)) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

}
