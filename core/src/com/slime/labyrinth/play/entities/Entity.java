package com.slime.labyrinth.play.entities;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Class to encapsulate the body of an entity
 * @author Bertrand
 *
 */
public interface Entity {

	/**
	 * Return the body of the Entity
	 * @return the body of the entity
	 */
	public Body getBody();
	
}
