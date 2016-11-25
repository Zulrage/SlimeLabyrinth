package com.slime.labyrinth;

import com.badlogic.gdx.Game;
import com.slime.labyrinth.screens.MenuScreen;

/**
 * Game class called by each other projects. Call the first screen to be shown
 * 
 * @author Bertrand
 *
 */
public class SlimeLabyrinthGame extends Game {

	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
