package com.slime.labyrinth.listeners;

import com.slime.labyrinth.utils.EnumGameStatisticFields;

/**
 * Simple interface for listeners on the game progress (Example : XP, SCORE)
 * 
 * @author Bertrand
 *
 */
public interface IGameStatisticsListener {

	public void performAction(EnumGameStatisticFields field);
}