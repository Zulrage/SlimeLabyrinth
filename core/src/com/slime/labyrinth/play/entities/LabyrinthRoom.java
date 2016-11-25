package com.slime.labyrinth.play.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.slime.labyrinth.screens.PlayScreen;
import com.slime.labyrinth.utils.GameStatics;

/**
 * Class dynamically created by the {@link PlayScreen} dividing the screen in
 * different instance </br>
 * Create the entities and containing the logic of the movement in its row.
 * 
 * @author Bertrand
 *
 */
public class LabyrinthRoom extends InputAdapter {

	private int id;
	private boolean enable = true;
	protected final float WIDTH, HEIGHT, ROW_SIZE, X, Y;

	private AbstractSlime player;
	private int playerRow;
	// Used to unproject the location of clicks
	private final Camera CAMERA;
	private boolean isTouched = false;
	private int clickTouchedDown = -1;
	private final int ROW_NUMBER = GameStatics.ROW_NUMBER;
	private final World WORLD;

	private Array<IMovable> movableList = new Array<IMovable>();

	/**
	 * 
	 * @param world
	 *            : The world where the entities need to be placed
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param camera
	 *            : Unproject the clicks on the screen for game coordinates
	 * @param id
	 *            : For debugging purpose and alternating color
	 * @param entites
	 *            : For drawing in the render function
	 */
	public LabyrinthRoom(World world, float x, float y, float width, float height, Camera camera, int id,
			CopyOnWriteArrayList<Entity> entites) {
		WIDTH = width / 2;
		HEIGHT = height;
		Y = y;
		X = x;
		WORLD = world;
		CAMERA = camera;
		ROW_SIZE = WIDTH * 2 / ROW_NUMBER;
		this.id = id;

		createParalax(entites);
		createPlayer(entites);
	}

	/**
	 * Create the moving parralax background (Two background moving down and
	 * teleporting themselves at the top of the screen)
	 * 
	 * @param entites
	 */
	private void createParalax(CopyOnWriteArrayList<Entity> entites) {
		ParalaxBackground background = new ParalaxBackground(WORLD, X, Y, WIDTH, HEIGHT, entites, false);
		movableList.add(background);

		ParalaxBackground backgroundUp = new ParalaxBackground(WORLD, X, Y, WIDTH, HEIGHT, entites, true);
		movableList.add(backgroundUp);
	}

	/**
	 * Create the player in the middle row (If number of row even : Middle+0.5)
	 * 
	 * @param entites
	 */
	private void createPlayer(CopyOnWriteArrayList<Entity> entites) {
		int playerStartingRow = ((int) ROW_NUMBER / 2);
		player = new Slime(WORLD, X - WIDTH + ((playerStartingRow + 0.5f) * ROW_SIZE),
				Y - HEIGHT + GameStatics.SLIME_Y_OFFSET, ROW_SIZE / 2, id % 2);
		entites.add(player);
		playerRow = playerStartingRow + 1;
	}

	/**
	 * Call in the render method of the screen to move the entities that should
	 * be moved (see {@link IMovable})
	 */
	public void update() {
		for (IMovable movable : movableList) {
			movable.move();
		}
	}

	/**
	 * Reset the movement the entities that should be moved (see
	 * {@link IMovable})
	 */
	public void stop() {
		for (IMovable movable : movableList) {
			movable.stop();
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 clickPosition = new Vector3(screenX, screenY, 0);
		CAMERA.unproject(clickPosition);
		// Check if this room has been tocuhed
		if (clickPosition.x > X - WIDTH && clickPosition.x <= X + WIDTH) {
			isTouched = true;
			clickTouchedDown = screenX;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (isTouched) {
			isTouched = false;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (isTouched) {
			int distance = screenX - clickTouchedDown;
			if (Math.abs(distance) > ROW_SIZE * GameStatics.MINIMUM_PIXEL_DRAG_REACTION) {
				if (distance < 0 && playerRow > 1) {
					playerRow--;
					movePlayer(player.getBody().getPosition().x - ROW_SIZE);
				} else if (distance > 0 && playerRow < ROW_NUMBER) {
					playerRow++;
					movePlayer(player.getBody().getPosition().x + ROW_SIZE);
				}
				clickTouchedDown = screenX;
			}

			return true;
		}
		return false;
	}

	/**
	 * Teleport the player at a row
	 * 
	 * @param newPosition
	 */
	private void movePlayer(float newPosition) {
		if (enable) {
			player.getBody().setTransform(new Vector2(newPosition, player.getBody().getPosition().y),
					player.getBody().getAngle());
		}
	}

	public void createWallPattern(CopyOnWriteArrayList<Entity> entities, float leftEdge, float rightEdge) {
		boolean isThereASlime;
		int bountySlimeNumber = 0;
		// Create slime until the probability fails or there are 3 of them on
		// the pattern
		do {
			isThereASlime = MathUtils.random(1, 100) <= GameStatics.BOUNTY_SLIME_CHANCE;
			if (isThereASlime) {
				// More... More... Moooore !
				bountySlimeNumber++;
			}
		} while (isThereASlime && bountySlimeNumber < 3);
		int wallNumber = 0;
		int maxWallNumber = ROW_NUMBER - bountySlimeNumber;
		if (bountySlimeNumber == 0) {
			// Avoid to block the whole path with wall
			maxWallNumber--;
		}
		if (maxWallNumber >= 0) {
			wallNumber = MathUtils.random(0, maxWallNumber);
			// Ramdomize the position
			final List<Integer> ints = getDistinctIndex(wallNumber + bountySlimeNumber, 0, ROW_NUMBER - 1);
			for (int i = 0; i < wallNumber; i++) {
				createWall(ints.get(i), entities);
			}
			for (int i = wallNumber; i < ints.size(); i++) {
				createBountySlime(ints.get(i), entities);
			}
		}
	}

	/**
	 * Select <code>number</code> random distinct number between a min and a max
	 * 
	 * @param number
	 * @param min
	 * @param max
	 * @return a list of <code>number</code> distinct Integer
	 */
	private List<Integer> getDistinctIndex(int number, int min, int max) {
		List<Integer> indexes = new ArrayList<Integer>();
		while (indexes.size() != number) {
			int newInt = MathUtils.random(min, max);
			boolean alreadyContains = false;
			for (int i = 0; i < indexes.size(); i++) {
				if (newInt == indexes.get(i).intValue()) {
					alreadyContains = true;
					break;
				}
			}
			if (!alreadyContains) {
				indexes.add(newInt);
			}
		}
		return indexes;
	}

	public void createWall(int row, CopyOnWriteArrayList<Entity> entities) {
		Box box = new Box(WORLD, getRowPosition(row), Y + HEIGHT, ROW_SIZE / 2, ROW_SIZE / 2, Y - HEIGHT);
		entities.add(box);
		movableList.add(box);
	}

	public void createBountySlime(int row, CopyOnWriteArrayList<Entity> entities) {
		BountySlime slime = new BountySlime(WORLD, getRowPosition(row), Y + HEIGHT, ROW_SIZE / 2,
				MathUtils.random(0, 1), Y - HEIGHT);
		entities.add(slime);
		movableList.add(slime);
	}

	private float getRowPosition(int row) {
		return (X - WIDTH + (row + 0.5f) * ROW_SIZE);
	}

	public AbstractSlime getPlayer() {
		return player;
	}

	public void setPlayer(AbstractSlime player) {
		this.player = player;
	}

	public void changePlayerColor() {
		player.changeColor();

	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

}
