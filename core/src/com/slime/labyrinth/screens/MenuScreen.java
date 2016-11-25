package com.slime.labyrinth.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.slime.labyrinth.SlimeLabyrinthGame;
import com.slime.labyrinth.play.contactListener.GameContactListener;
import com.slime.labyrinth.play.entities.Entity;
import com.slime.labyrinth.play.entities.IEntityWeighted;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.ui.MenuGroup;
import com.slime.labyrinth.utils.GameTextures;
import com.slime.labyrinth.utils.generator.DroppingSlimeGenerator;

/**
 * Screen creating the menu interface and the background
 * 
 * @author Bertrand
 *
 */
public class MenuScreen extends SlimeLabyrinthScreen {

	public MenuScreen(SlimeLabyrinthGame shadowLightGame) {
		super(shadowLightGame);
	}

	private Vector3 bottomLeft, bottomRight, topLeft;

	private ArrayList<IEntityWeighted> entities = new ArrayList<IEntityWeighted>();

	private DroppingSlimeGenerator generator;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.4f, .4f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// Sort the list for placing the entities depending on their weight
		Collections.sort(entities);
		for (IEntityWeighted entity : entities) {
			updateSpriteOnTick((Entity) entity);
		}
		sweepDeadBody(entities);
		batch.end();

		generator.generate();

		stage.act(delta);
		stage.draw();
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
		batch = new SpriteBatch();

		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 25, Gdx.graphics.getHeight() / 25);

		bottomLeft = new Vector3(0, Gdx.graphics.getHeight(), 0);
		topLeft = new Vector3(0, 0, 0);
		bottomRight = new Vector3(Gdx.graphics.getWidth(), bottomLeft.y, 0);
		camera.unproject(bottomLeft);
		camera.unproject(bottomRight);
		camera.unproject(topLeft);
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(new InputAdapter() {

			@Override
			public boolean keyDown(int keycode) {
				switch (keycode) {
				case Keys.BACK:
				case Keys.ESCAPE:
					Gdx.app.exit();
					break;
				}
				return false;
			}

		});

		createContactListener();
		generator = new DroppingSlimeGenerator(bottomLeft.x, bottomRight.x, bottomLeft.y, topLeft.y, entities, world);

		createUI(multiplexer);
		Gdx.input.setInputProcessor(multiplexer);
	}

	private void createContactListener() {
		GameContactListener contact = new GameContactListener();
		world.setContactFilter(contact);
		world.setContactListener(contact);
	}

	private void createUI(InputMultiplexer multiplexer) {
		stage = new Stage();
		multiplexer.addProcessor(stage);
		Image title = new Image(GameTextures.TITLE_TEXTURE);
		title.setPosition(Gdx.graphics.getWidth() - title.getWidth() + 10,
				Gdx.graphics.getHeight() - title.getHeight() - 60);
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), new TextureAtlas("ui/atlas.pack"));
		stage.addActor(title);
		stage.addActor(new MenuGroup(skin, game));
	}

	public void sweepDeadBody(List<IEntityWeighted> entities) {
		for (Entity entity : entities) {
			Body body = entity.getBody();
			if (body != null) {
				GenericUserData data = (GenericUserData) body.getUserData();
				if (data != null && data.isFlaggedForDelete()) {
					final Array<JointEdge> list = body.getJointList();
					while (list.size > 0) {
						world.destroyJoint(list.get(0).joint);
					}
					body.setUserData(null);
					body = null;
				}
			}
		}
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
		skin.dispose();
		stage.dispose();
	}
}
