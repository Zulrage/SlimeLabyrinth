package com.slime.labyrinth.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slime.labyrinth.SlimeLabyrinthGame;
import com.slime.labyrinth.listeners.IGameStatisticsListener;
import com.slime.labyrinth.play.contactListener.GameContactListener;
import com.slime.labyrinth.play.entities.Entity;
import com.slime.labyrinth.play.entities.LabyrinthRoom;
import com.slime.labyrinth.ui.BlackRectangle;
import com.slime.labyrinth.ui.PlayStatisticGroup;
import com.slime.labyrinth.utils.EnumGameStatisticFields;
import com.slime.labyrinth.utils.GameStatistic;
import com.slime.labyrinth.utils.generator.LevelGenerator;

/**
 * Create the interface of the game and the pattern generator </br>
 * This class is a listener of the game stats
 * 
 * @author Bertrand
 *
 */
public class PlayScreen extends SlimeLabyrinthScreen implements IGameStatisticsListener {

	public PlayScreen(SlimeLabyrinthGame shadowLightGame, int roomNumber) {
		super(shadowLightGame);
		this.roomNumber = roomNumber;
	}

	private Box2DDebugRenderer debugRenderer;

	private LevelGenerator levelGenerator;
	private PlayStatisticGroup group;
	private final int roomNumber;
	private List<LabyrinthRoom> rooms;
	private BlackRectangle blackRectangle;
	private TextButton replayButton, backButton;

	private CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<Entity>();

	private boolean gameOver = false;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		if (!gameOver) {
			// Move and generate pattern as long there is no game over
			levelGenerator.generate();
			for (LabyrinthRoom labyrinthRoom : rooms) {
				labyrinthRoom.update();
			}
		}
		for (Entity entity : entities) {
			updateSpriteOnTick(entity);
		}
		batch.end();

		stage.act(delta);
		stage.draw();
		if (gameOver) {
			actGameOverMenu(delta);
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 25;
		camera.viewportHeight = height / 25;
	}

	@Override
	public void show() {
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			Gdx.graphics.setDisplayMode((int) (Gdx.graphics.getHeight() / 1.5f), Gdx.graphics.getHeight(), false);
		}

		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();

		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 25);
		rooms = new ArrayList<LabyrinthRoom>();

		bottomLeft = new Vector3(0, Gdx.graphics.getHeight(), 0);
		topLeft = new Vector3(0, 0, 0);
		bottomRight = new Vector3(Gdx.graphics.getWidth(), bottomLeft.y, 0);
		camera.unproject(bottomLeft);
		camera.unproject(bottomRight);
		camera.unproject(topLeft);
		Gdx.input.setCatchBackKey(true);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new InputAdapter() {

			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.BACK:
				case Keys.ESCAPE:
					game.setScreen(new MenuScreen(game));
					break;
				}
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				camera.zoom += amount / 25f;
				return true;
			}

		});

		createRooms(multiplexer);
		createContactListener();

		levelGenerator = new LevelGenerator(bottomLeft.x, bottomRight.x, rooms, entities);
		GameStatistic.resetStatistic();
		GameStatistic.addListener(this);
		createUI(multiplexer);
		Gdx.input.setInputProcessor(multiplexer);
	}

	private void createContactListener() {
		GameContactListener contact = new GameContactListener();
		world.setContactFilter(contact);
		world.setContactListener(contact);
	}

	/**
	 * Divide the screen is a number of rooms handling the control
	 * 
	 * @param multiplexer
	 */
	private void createRooms(InputMultiplexer multiplexer) {
		final float roomWidth = (bottomRight.x - bottomLeft.x) / roomNumber;
		for (int i = 0; i < roomNumber; i++) {
			float y = topLeft.y + 3;
			LabyrinthRoom room = new LabyrinthRoom(world,
					(bottomRight.x - bottomLeft.x - roomWidth * (roomNumber - 1)) * i
							- ((roomWidth / 2) * (roomNumber - 1)),
					bottomLeft.y + y, roomWidth, y, camera, i, entities);
			rooms.add(room);
			multiplexer.addProcessor(room);
		}
	}

	/**
	 * Create the stage UI (Button and so on)
	 * @param multiplexer
	 */
	private void createUI(InputMultiplexer multiplexer) {
		stage = new Stage();
		multiplexer.addProcessor(stage);
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));

		createGameOverUI();
		TextButton buttonChange = new TextButton("Change", skin, "small");
		buttonChange.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!gameOver) {
					for (LabyrinthRoom room : rooms) {
						room.changePlayerColor();
					}
				}
			}
		});
		buttonChange.setWidth(Gdx.graphics.getWidth());
		buttonChange.setHeight(Gdx.graphics.getHeight() / 15);
		group = new PlayStatisticGroup(skin, Gdx.graphics.getWidth());
		group.setPosition(0, Gdx.graphics.getHeight() - group.getHeight() * 2 - 15);
		stage.addActor(buttonChange);
		stage.addActor(group);
	}
	
	/**
	 * Creating the UI to show when reaching a Game over
	 */
	private void createGameOverUI() {
		blackRectangle = new BlackRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		replayButton = new TextButton("Replay", skin);
		replayButton.setWidth(Gdx.graphics.getWidth() / 1.5f);
		replayButton.setHeight(Gdx.graphics.getHeight() / 15);
		replayButton.setPosition(Gdx.graphics.getWidth() / 2 - replayButton.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		backButton = new TextButton("Back to menu", skin);
		backButton.setWidth(Gdx.graphics.getWidth() / 1.5f);
		backButton.setHeight(Gdx.graphics.getHeight() / 15);
		backButton.setPosition(Gdx.graphics.getWidth() / 2 - backButton.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - replayButton.getHeight() * 2 - 10);
		backButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		replayButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PlayScreen(game, 3));
			}
		});
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		world.dispose();
		debugRenderer.dispose();
		skin.dispose();
		stage.dispose();
	}

	@Override
	public void performAction(EnumGameStatisticFields field) {
		switch (field) {
		case LEVEL:
			// If level below 0, game over
			if (GameStatistic.getLevel() < 0) {
				gameOver = true;
				stage.addActor(blackRectangle);
				stage.addActor(backButton);
				stage.addActor(replayButton);
				for (LabyrinthRoom room : rooms) {
					room.stop();
					room.setEnable(false);
				}
			}
			break;
		default:
			break;
		}
	}

	private void actGameOverMenu(float delta) {
		blackRectangle.act(delta);
		backButton.act(delta);
		replayButton.act(delta);
	}
}
