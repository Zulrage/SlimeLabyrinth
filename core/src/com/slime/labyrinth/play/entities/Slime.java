package com.slime.labyrinth.play.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.slime.labyrinth.play.entities.collide.PlayerSlimeCollide;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameStatics;
import com.slime.labyrinth.utils.GameTextures;

/**
 * Player slime
 * 
 * @author Bertrand
 *
 */
public class Slime extends AbstractSlime {

	public Slime(World world, float x, float y, float width, int color) {
		super(world, x, y, width, color);
	}

	@Override
	public GenericUserData defineUserData() {
		switch (color) {
		case 0:
			return new GenericUserData(EnumUserDataId.GREEN_PLAYER, new Sprite(GameTextures.GREEN_SLIME_TEXTURE), WIDTH,
					HEIGHT * 2, new PlayerSlimeCollide());

		default:
			return new GenericUserData(EnumUserDataId.RED_PLAYER, new Sprite(GameTextures.RED_SLIME_TEXTURE), WIDTH,
					HEIGHT * 2, new PlayerSlimeCollide());
		}
	}

	@Override
	public void changeColor() {
		color = (color + 1) % GameStatics.COLOR_NUMBER;
		GenericUserData data = (GenericUserData) body.getUserData();
		switch (color) {
		case 0:
			data.setId(EnumUserDataId.GREEN_PLAYER);
			data.getSprite().setTexture(GameTextures.GREEN_SLIME_TEXTURE);
			break;
		default:
			data.setId(EnumUserDataId.RED_PLAYER);
			data.getSprite().setTexture(GameTextures.RED_SLIME_TEXTURE);
			break;
		}
	}
}
