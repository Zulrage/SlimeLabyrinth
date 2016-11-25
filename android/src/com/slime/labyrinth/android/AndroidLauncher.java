package com.slime.labyrinth.android;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.slime.labyrinth.SlimeLabyrinthGame;
import com.slime.labyrinth.utils.GameStatics;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		GameStatics.BASIC_DROP_VELOCITY=8f;
		GameStatics.SLIME_Y_OFFSET=10f;
		initialize(new SlimeLabyrinthGame(), config);
	}
}
