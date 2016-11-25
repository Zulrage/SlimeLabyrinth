package com.slime.labyrinth.tweens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Class handling the animation of a label
 */
public class LabelAccessor implements TweenAccessor<Label> {

	public static final int Y = 0, RGB = 1, ALPHA = 2, NUMBER = 3;

	@Override
	public int getValues(Label target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case Y:
			returnValues[0] = target.getY();
			return 1;
		case RGB:
			returnValues[0] = target.getColor().r;
			returnValues[1] = target.getColor().g;
			returnValues[2] = target.getColor().b;
			return 3;
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		case NUMBER:
			returnValues[0] = Integer.parseInt(target.getText().toString());
			return 1;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Label target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case Y:
			target.setY(newValues[0]);
			break;
		case RGB:
			target.setColor(newValues[0], newValues[1], newValues[2], target.getColor().a);
			break;
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			break;
		case NUMBER:
			target.setText("" + (int) newValues[0]);
			break;
		default:
			assert false;
		}
	}

}
