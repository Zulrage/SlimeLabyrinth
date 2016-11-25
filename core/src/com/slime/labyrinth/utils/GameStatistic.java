package com.slime.labyrinth.utils;

import java.util.ArrayList;
import java.util.List;

import com.slime.labyrinth.listeners.IGameStatisticsListener;

/**
 * Static class to handle the scoring and XP system within the game
 * 
 * @author Bertrand
 *
 */
public class GameStatistic {

	private static int numberOfSlimeTaken = 0;

	private static int numberOfWallTaken = 0;

	private static int experience = 0;

	private static int experienceNeeded = 0;

	private static int level = 0;

	private static int score = 0;

	private static int comboScore;
	private static int comboNumber;

	private static List<IGameStatisticsListener> listeners;

	public static void resetStatistic() {
		level = 0;
		experience = 0;
		numberOfSlimeTaken = 0;
		numberOfWallTaken = 0;
		experienceNeeded = 5;
		score = 0;
		comboScore = 0;
		comboNumber = 0;

		if (listeners == null) {
			listeners = new ArrayList<IGameStatisticsListener>();
		}
		listeners.clear();
	}

	public static void loseLevel(int number) {
		level -= number;
		notifyListeners(EnumGameStatisticFields.LEVEL);
	}

	public static void addExperience(int experienceGained) {
		experience += experienceGained;
		while (experience >= experienceNeeded) {
			level++;
			experience -= experienceNeeded;
			experienceNeeded += GameStatics.EXPERIENCE_NEEDED_STEP;
		}
		notifyListeners(EnumGameStatisticFields.XP);
	}

	public static void addScore(int plusScore) {
		score += plusScore;
		comboScore += plusScore;
		notifyListeners(EnumGameStatisticFields.SCORE);
	}

	public static void addListener(IGameStatisticsListener l) {
		listeners.add(l);
	}

	public static int getNumberOfSlimeTaken() {
		return numberOfSlimeTaken;
	}

	public static int getNumberOfWallTaken() {
		return numberOfWallTaken;
	}

	public static int getExperience() {
		return experience;
	}

	public static int getExperienceNeeded() {
		return experienceNeeded;
	}

	public static int getLevel() {
		return level;
	}

	public static int getScore() {
		return score;
	}

	public static int getComboScore() {
		return comboScore;
	}

	public static void setComboScore(int comboScore) {
		GameStatistic.comboScore = comboScore;
	}

	public static int getComboNumber() {
		return comboNumber;
	}

	public static void setComboNumber(int comboNumber) {
		GameStatistic.comboNumber = comboNumber;
	}

	private static void notifyListeners(EnumGameStatisticFields field) {
		for (IGameStatisticsListener iGameStatisticsListener : listeners) {
			iGameStatisticsListener.performAction(field);
		}
	}

}
