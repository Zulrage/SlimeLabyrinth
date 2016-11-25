package com.slime.labyrinth.play.entities.collide;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameStatistic;

/**
 * Handle the logic when a Box collide something else
 * @author Bertrand
 *
 */
public class BoxCollide implements ICollidable {

	@Override
	public boolean canCollide(Fixture fixtureA, Fixture fixtureB) {
		GenericUserData userDataA = (GenericUserData) fixtureA.getBody().getUserData();
		GenericUserData userDataB = (GenericUserData) fixtureB.getBody().getUserData();
		// Check wich on is the box and remove it
		if(userDataA.getId().equals(EnumUserDataId.WALL)) {
			userDataA.setFlaggedForDelete(true);
			userDataA.setId(EnumUserDataId.DESTRUCTED);
			loseLevel(userDataA, userDataB);
		} else if(userDataB.getId().equals(EnumUserDataId.WALL)){
			userDataB.setFlaggedForDelete(true);
			userDataB.setId(EnumUserDataId.DESTRUCTED);
			loseLevel(userDataB, userDataA);
		}
		return false;
	}

	public void loseLevel(GenericUserData wallData, GenericUserData playerData) {
		GameStatistic.loseLevel(1);
		if(GameStatistic.getLevel()<0) {
			playerData.setFlaggedForDelete(true);
			playerData.setId(EnumUserDataId.DESTRUCTED);
		}
	}
	
	@Override
	public void onCollide(Contact contact, Manifold oldManifold) {
		// TODO put the logic here
	}

}
