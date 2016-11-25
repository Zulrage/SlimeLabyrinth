package com.slime.labyrinth.utils.generator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.slime.labyrinth.play.entities.Entity;
import com.slime.labyrinth.play.entities.LabyrinthRoom;
import com.slime.labyrinth.utils.GameStatics;

/**
 * Generate pattern in the differents rooms
 * 
 * @author Bertrand
 *
 */
public class LevelGenerator {

	private float leftEdge, rightEdge;
	private List<LabyrinthRoom> rooms;
	private CopyOnWriteArrayList<Entity> entities;
	private long lastWallGenerated;
	private long[] lastWallGeneratedRoom;

	public LevelGenerator(float leftEdge, float rightEdge, List<LabyrinthRoom> rooms,
			CopyOnWriteArrayList<Entity> entities) {
		this.leftEdge = leftEdge;
		this.rightEdge = rightEdge;
		this.rooms = rooms;
		this.entities = entities;
		lastWallGeneratedRoom = new long[rooms.size()];
	}

	public void generate() {
		if (lastWallGenerated + GameStatics.MINIMUM_TIME_BETWEEN_GENERATION > TimeUtils.millis()) {
			return;
		}
		lastWallGenerated = (long) (TimeUtils.millis() + MathUtils.random(0, 300));

		int roomNumber = MathUtils.random(0, rooms.size() - 1);
		if (lastWallGeneratedRoom[roomNumber] + GameStatics.MINIMUM_TIME_BETWEEN_GENERATION_ROOM > TimeUtils.millis()) {
			return;
		}
		lastWallGeneratedRoom[roomNumber] = (long) (TimeUtils.millis() + MathUtils.random(0, 300));
		rooms.get(roomNumber).createWallPattern(entities, leftEdge, rightEdge);
	}

}
