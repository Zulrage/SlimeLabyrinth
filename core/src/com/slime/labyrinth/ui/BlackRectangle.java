package com.slime.labyrinth.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;


/**
 * Create a transparent rectangle in a area
 * @author Bertrand
 *
 */
public class BlackRectangle extends Actor {

	public float opacity = 0.0f;
	private Color shapeFillColor = Color.BLACK;
	public Rectangle area;
	public ShapeRenderer shapeRen;

	public BlackRectangle(float x, float y, float w, float h) {
		shapeRen = new ShapeRenderer();
		this.area = new Rectangle(x, y, w, h);
	}

	@Override
	public void act(float delta) {
		shapeRen.begin(ShapeType.Filled);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRen.setColor(new Color(shapeFillColor.r, shapeFillColor.g, shapeFillColor.b, 0.5f));
		shapeRen.rect(area.x, area.y, area.width, area.height);
		shapeRen.end();

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		super.act(delta);
	}

	public void setShapeFillColor(float r, float g, float b) {
		this.shapeFillColor = new Color(r, g, b, 1);
	}

}