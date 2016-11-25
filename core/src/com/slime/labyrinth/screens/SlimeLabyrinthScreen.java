package com.slime.labyrinth.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.slime.labyrinth.SlimeLabyrinthGame;
import com.slime.labyrinth.play.entities.Entity;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;

/**
 * Comon class for the screen needing Box2D and UI elements
 * 
 * @author Bertrand
 *
 */
public abstract class SlimeLabyrinthScreen implements Screen {

	protected SlimeLabyrinthGame game;
	protected World world;
	protected SpriteBatch batch;
	protected OrthographicCamera camera;

	protected Stage stage;
	protected Skin skin;

	protected final float TIMESTEP = 1 / 60f;
	protected final int VELOCITYITERATIONS = 8, POSITIONITERATIONS = 3;

	protected Vector3 bottomLeft, bottomRight, topLeft;

	public SlimeLabyrinthScreen(SlimeLabyrinthGame shadowLightGame) {
		super();
		this.game = shadowLightGame;
	}

	@Override
	public void render(float delta) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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
	}

	public SlimeLabyrinthGame getGame() {
		return game;
	}

	/**
	 * Draw sprites for an entity
	 * @param entity
	 */
	protected void updateSpriteOnTick(Entity entity) {
		Body body = entity.getBody();
		if (body != null && body.getUserData() instanceof GenericUserData) {
			GenericUserData userData = ((GenericUserData) body.getUserData());
			if (!userData.isFlaggedForDelete()) {
				Sprite sprite = userData.getSprite();
				if (sprite != null) {
					sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2,
							body.getPosition().y - sprite.getHeight() / 2);
					sprite.draw(batch);
				}
			}
		}
	}

}
