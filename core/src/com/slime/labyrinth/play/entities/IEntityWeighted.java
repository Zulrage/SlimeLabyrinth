package com.slime.labyrinth.play.entities;

/**
 * Class for sorting depending on the variable Weight for drawing a Sprite ahead
 * of others
 * 
 * @author Bertrand
 *
 */
public interface IEntityWeighted extends Entity, Comparable<IEntityWeighted> {

	float getWeight();

	void setWeight(float weight);

}
