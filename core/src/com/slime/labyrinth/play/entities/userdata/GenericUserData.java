package com.slime.labyrinth.play.entities.userdata;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.slime.labyrinth.play.entities.collide.ICollidable;
import com.slime.labyrinth.utils.EnumUserDataId;

/**
 * Custom userdata used when the world render or step and bound to a body
 * 
 * @author Bertrand
 *
 */
public class GenericUserData {

	public GenericUserData() {
		this(null, null, 0, 0, null);
	}

	public GenericUserData(EnumUserDataId id, Sprite sprite, float width, float height, ICollidable contactReaction) {
		this.id = id;
		this.contactReaction = contactReaction;
		setSprite(sprite, width, height);
	}

	/**
	 * Have to be destroy ?
	 */
	private boolean flaggedForDelete = false;

	/**
	 * Type of the userdata
	 */
	private EnumUserDataId id;

	/**
	 * Sprite to draw
	 */
	private Sprite sprite;

	/**
	 * How the body handle reaction
	 */
	private ICollidable contactReaction;

	public EnumUserDataId getId() {
		return id;
	}

	public void setId(EnumUserDataId id) {
		this.id = id;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite, float width, float height) {
		this.sprite = sprite;
		if (sprite != null) {
			this.sprite.setSize(width, height);
		}
	}

	public boolean isFlaggedForDelete() {
		return flaggedForDelete;
	}

	public void setFlaggedForDelete(boolean flaggedForDelete) {
		this.flaggedForDelete = flaggedForDelete;
	}

	public ICollidable getContactReaction() {
		return contactReaction;
	}

	public void setContactReaction(ICollidable contactReaction) {
		this.contactReaction = contactReaction;
	}
}
