package com.slime.labyrinth.play.entities;

/**
 * Class to handle movement between different objects
 * @author Bertrand
 *
 */
public interface IMovable {

	/**
	 * Logic of moving
	 */
	abstract public void move();
	
	/**
	 * Reset movement
	 */
	abstract public void stop();
	
}
