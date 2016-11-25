package com.slime.labyrinth.play.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.slime.labyrinth.play.entities.collide.NotCollidingEntityCollide;
import com.slime.labyrinth.play.entities.userdata.GenericUserData;
import com.slime.labyrinth.utils.EnumUserDataId;
import com.slime.labyrinth.utils.GameTextures;

/**
 * Class to create a cloud that moves rigth of left
 * Automatically create the userData and the body
 * @author Bertrand
 *
 */
public class Cloud implements IEntityWeighted, IMovable {

	protected Body body;
	protected Fixture fixture;
	protected final float WIDTH, HEIGHT;
	private Vector2 velocity = new Vector2();
	private float right, left;
	private boolean directionRight;
	private float weight;

	public Cloud(World world, float x, float y, float width, int color, float right, float left, float dropVelocity,
			float weight, boolean directionRigth) {
		this.right = right;
		this.left = left;
		boolean isBig = MathUtils.randomBoolean();
		WIDTH = width * (isBig ? 3 : 1);
		HEIGHT = width;
		this.directionRight = directionRigth;
		velocity.x = dropVelocity * (directionRight ? 1 : -1);
		this.weight = weight;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, HEIGHT / 2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;

		body = world.createBody(bodyDef);
		Sprite cloudSprite = new Sprite(isBig ? GameTextures.CLOUD_TEXTURE1 : GameTextures.CLOUD_TEXTURE2);
		body.setUserData(
				new GenericUserData(EnumUserDataId.CLOUD, cloudSprite, WIDTH, HEIGHT, new NotCollidingEntityCollide()));
		fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}

	@Override
	public void move() {
		body.setLinearVelocity(velocity);
		if (body.getPosition().x > left || body.getPosition().x < right) {
			GenericUserData userData = (GenericUserData) body.getUserData();
			if (userData != null) {
				userData.setFlaggedForDelete(true);
			}
		}
	}

	@Override
	public void stop() {
		body.setLinearVelocity(new Vector2(0, 0));
	}

	@Override
	public float getWeight() {
		return weight;
	}

	@Override
	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(IEntityWeighted o) {
		if (o.getWeight() < this.weight) {
			return -1;
		} else if (o.getWeight() > this.weight) {
			return 1;
		}
		return 0;
	}

	@Override
	public Body getBody() {
		return body;
	}

}
