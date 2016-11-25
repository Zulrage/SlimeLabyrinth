package com.slime.labyrinth.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.slime.labyrinth.SlimeLabyrinthGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.useGL30 = true;
		config.width *= 1.5;
		config.height *=1.5;
		new LwjglApplication(new SlimeLabyrinthGame(), config);
	}
}
