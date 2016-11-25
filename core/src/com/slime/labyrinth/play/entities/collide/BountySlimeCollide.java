package com.slime.labyrinth.play.entities.collide;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameStatics;
import com.slime.labyrinth.utils.GameStatistic;

/**
 * Handle the logic when a BountySlime collide something else
 * @author Bertrand
 *
 */
public class BountySlimeCollide implements ICollidable {

	@Override
	public boolean canCollide(Fixture fixtureA, Fixture fixtureB) {
		GenericUserData userDataA = (GenericUserData) fixtureA.getBody().getUserData();
		GenericUserData userDataB = (GenericUserData) fixtureB.getBody().getUserData();
		// Check wich on is the bounty slime and remove it
		if (!userDataA.getId().equals(EnumUserDataId.CLOUD) && !userDataB.getId().equals(EnumUserDataId.CLOUD)) {
			if (userDataA.getId().equals(EnumUserDataId.RED) || userDataA.getId().equals(EnumUserDataId.GREEN)) {
				userDataA.setFlaggedForDelete(true);
				getSlime(userDataA, userDataB);
				userDataA.setId(EnumUserDataId.DESTRUCTED);
			} else if (userDataB.getId().equals(EnumUserDataId.RED) || userDataB.getId().equals(EnumUserDataId.GREEN)) {
				userDataB.setFlaggedForDelete(true);
				getSlime(userDataB, userDataA);
				userDataB.setId(EnumUserDataId.DESTRUCTED);
			}
		}
		return false;
	}

	@Override
	public void onCollide(Contact contact, Manifold oldManifold) {
		// TODO put the logic here
	}

	/**
	 * If the player matched the color, add XP, if not, lose a level
	 * @param slimeData
	 * @param otherData
	 */
	private void getSlime(GenericUserData slimeData, GenericUserData otherData) {
		if ((slimeData.getId().equals(EnumUserDataId.RED) && otherData.getId().equals(EnumUserDataId.RED_PLAYER))
				|| (slimeData.getId().equals(EnumUserDataId.GREEN)
						&& otherData.getId().equals(EnumUserDataId.GREEN_PLAYER))) {
			GameStatistic.addExperience(GameStatics.SLIME_XP);
			GameStatistic.addScore(GameStatics.SLIME_SCORE);
		} else {
			loseLevel(slimeData, otherData);
		}
	}
	
	public void loseLevel(GenericUserData wallData, GenericUserData playerData) {
		GameStatistic.loseLevel(1);
		if(GameStatistic.getLevel()<0) {
			playerData.setFlaggedForDelete(true);
			playerData.setId(EnumUserDataId.DESTRUCTED);
		}
	}
}
