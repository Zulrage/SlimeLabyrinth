package com.slime.labyrinth.utils;

import java.util.HashMap;

/**
 * Statics strings and contents for the game such as keys and paths
 * 
 */
public class GameStatics {
	
	public final static int ROW_NUMBER = 3;
	
	public final static int COLOR_NUMBER = 2;
	
	public final static int MINIMUM_PIXEL_DRAG_REACTION = 12;
	
	public final static float BOUNTY_SLIME_CHANCE = 30;
	
	public final static int EXPERIENCE_NEEDED_STEP = 3;
	
	public final static int SLIME_XP = 1;
	
	public final static int SLIME_SCORE = 10;
	
	public final static int COMBO_TIME = 5000;
	
	public static float SLIME_Y_OFFSET = 3f;
	
	public static float BASIC_DROP_VELOCITY = 5f;
	
	public final static long MINIMUM_TIME_BETWEEN_GENERATION = (long) (1000/BASIC_DROP_VELOCITY);
	
	public final static long MINIMUM_TIME_BETWEEN_GENERATION_ROOM = (long) (4000/BASIC_DROP_VELOCITY);
	
	public final static long MINIMUM_TIME_BETWEEN_GENERATION_SLIME = 50;
	
	public final static long MINIMUM_TIME_BETWEEN_GENERATION_CLOUD = 500;
	
	public static final HashMap<String, String> PATHS = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
            put("levels","levels/level%d.json");
        }
    };

}
