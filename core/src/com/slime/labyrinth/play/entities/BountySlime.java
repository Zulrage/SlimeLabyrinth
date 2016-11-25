package com.slime.labyrinth.play.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.slime.labyrinth.play.entities.collide.BountySlimeCollide;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameStatics;
import com.slime.labyrinth.utils.GameTextures;

/**
 * Slime class for multiple purpose to create a slime that moves down
 * Automatically create the userData and the body
 * 
 * @author Bertrand
 *
 */
public class BountySlime extends AbstractSlime implements IEntityWeighted, IMovable {

	private Vector2 velocity = new Vector2();
	private float bottom;
	private float weight;

	/**
	 * Create a slime with a define speed (When moving down) and weight (When
	 * check whether it has to be shown before or after other sprites)
	 * 
	 * @param world : The world to place the slime
	 * @param x : The X position
	 * @param y : Y position
	 * @param width : Width and height
	 * @param color : Color of the slime
	 * @param bottom : End of the screen to handle delete flags
	 */
	public BountySlime(World world, float x, float y, float width, int color, float bottom) {
		this(world, x, y, width, color, bottom, GameStatics.BASIC_DROP_VELOCITY, 1);
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
	 * @param weight : weight (When
	 * check whether it has to be shown before or after other sprites)
	 */
	public BountySlime(World world, float x, float y, float width, int color, float bottom, float dropVelocity,
			float weight) {
		super(world, x, y, width, color);
		velocity.y = -dropVelocity;
		this.bottom = bottom;
		this.weight = weight;
	}

	@Override
	public void move() {
		body.setLinearVelocity(velocity);
		// Out of the screen, set to delete
		if (body.getPosition().y < bottom) {
			GenericUserData userData = (GenericUserData) body.getUserData();
			if (userData != null) {
				userData.setFlaggedForDelete(true);
			}
		}
	}

	@Override
	public void stop() {
		body.setLinearVelocity(new Vector2(0, 0));
	}

	@Override
	public GenericUserData defineUserData() {
		switch (color) {
		case 0:
			return new GenericUserData(EnumUserDataId.GREEN, new Sprite(GameTextures.GREEN_SLIME_TEXTURE), WIDTH,
					HEIGHT * 2, new BountySlimeCollide());

		default:
			return new GenericUserData(EnumUserDataId.RED, new Sprite(GameTextures.RED_SLIME_TEXTURE), WIDTH,
					HEIGHT * 2, new BountySlimeCollide());
		}
	}

	@Override
	public void changeColor() {
		color = (color + 1) % GameStatics.COLOR_NUMBER;
		GenericUserData data = (GenericUserData) body.getUserData();
		switch (color) {
		case 0:
			data.setId(EnumUserDataId.GREEN);
			data.getSprite().setTexture(GameTextures.GREEN_SLIME_TEXTURE);
			break;
		default:
			data.setId(EnumUserDataId.RED);
			data.getSprite().setTexture(GameTextures.RED_SLIME_TEXTURE);
			break;
		}
	}

	@Override
	public float getWeight() {
		return weight;
	}

	@Override
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(IEntityWeighted o) {
		if (o.getWeight() < this.weight) {
			return -1;
		} else if (o.getWeight() > this.weight) {
			return 1;
		}
		return 0;
	}

}
