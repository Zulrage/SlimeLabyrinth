package com.slime.labyrinth.play.entities.collide;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;

/**
 * Interface to help handling collision within the {@link GenericUserData} when
 * the world step
 * 
 * @author Bertrand
 *
 */
public interface ICollidable {

	public boolean canCollide(Fixture fixtureA, Fixture fixtureB);

	public void onCollide(Contact contact, Manifold oldManifold);

}
