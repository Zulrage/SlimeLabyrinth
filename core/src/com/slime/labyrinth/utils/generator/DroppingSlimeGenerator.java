package com.slime.labyrinth.utils.generator;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.slime.labyrinth.play.entities.BountySlime;
import com.slime.labyrinth.play.entities.Cloud;
import com.slime.labyrinth.play.entities.IEntityWeighted;
import com.slime.labyrinth.play.entities.IMovable;
import com.slime.labyrinth.utils.GameStatics;

/**
 * Generate random {@link Cloud} and {@link BountySlime} to make the screen a
 * little bit more lively
 * 
 * @author Bertrand
 *
 */
public class DroppingSlimeGenerator {

	private float leftEdge, rightEdge, bottom, top;
	private ArrayList<IEntityWeighted> entities;
	private long lastSlimeGenerated;
	private long lastCloudGenerated;
	private List<IMovable> movableList;
	private World world;

	public DroppingSlimeGenerator(float leftEdge, float rightEdge, float bottom, float top,
			ArrayList<IEntityWeighted> entities, World world) {
		this.leftEdge = leftEdge;
		this.rightEdge = rightEdge;
		this.entities = entities;
		this.world = world;
		this.bottom = bottom;
		this.top = top;

		this.movableList = new ArrayList<IMovable>();
	}

	public void generate() {
		move();
		generateSlime();
		generateCloud();
	}

	private void generateCloud() {
		if (lastCloudGenerated + GameStatics.MINIMUM_TIME_BETWEEN_GENERATION_CLOUD > TimeUtils.millis()) {
			return;
		}
		lastCloudGenerated = (long) (TimeUtils.millis() + MathUtils.random(0, 1000));

		createCloud();
	}

	private void generateSlime() {
		if (lastSlimeGenerated + GameStatics.MINIMUM_TIME_BETWEEN_GENERATION_SLIME > TimeUtils.millis()) {
			return;
		}
		lastSlimeGenerated = (long) (TimeUtils.millis() + MathUtils.random(0, 500));

		createBountySlime();
	}

	private void move() {
		for (IMovable movable : movableList) {
			movable.move();
		}
	}

	public void createBountySlime() {
		float weight = (leftEdge - rightEdge) / MathUtils.random(20, 40);
		BountySlime slime = new BountySlime(world, MathUtils.random(leftEdge, rightEdge), top + 1, weight,
				MathUtils.random(0, 1), bottom - 1,
				MathUtils.random(GameStatics.BASIC_DROP_VELOCITY, GameStatics.BASIC_DROP_VELOCITY * 6), weight);
		entities.add(slime);
		movableList.add(slime);
	}

	public void createCloud() {
		float weight = (leftEdge - rightEdge) / MathUtils.random(20, 40);
		boolean directionRigth = true;
		Cloud cloud = new Cloud(world, directionRigth ? leftEdge + weight * 9 : rightEdge - 2,
				MathUtils.random(bottom, top), weight * 3, MathUtils.random(0, 1), leftEdge - 20, rightEdge + 20,
				MathUtils.random(GameStatics.BASIC_DROP_VELOCITY / 2, GameStatics.BASIC_DROP_VELOCITY), weight,
				directionRigth);
		entities.add(cloud);
		movableList.add(cloud);
	}

}
