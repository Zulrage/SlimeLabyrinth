package com.slime.labyrinth.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.slime.labyrinth.SlimeLabyrinthGame;
import com.slime.labyrinth.screens.PlayScreen;
import com.slime.labyrinth.tweens.LabelAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

/**
 * UI class for creating the menu
 * @author Bertrand
 *
 */
public class MenuGroup extends WidgetGroup {

	private TweenManager tweenManager;
	private TextButton buttonPlay, buttonSetting;
	
	public MenuGroup(Skin skin, SlimeLabyrinthGame game) {

		createUI(skin, game);

		tweenManager = new TweenManager();
		Tween.registerAccessor(Label.class, new LabelAccessor());
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	private void createUI(Skin skin, final SlimeLabyrinthGame game) {
		addActors(skin, game);
	}

	private WidgetGroup addActors(Skin skin, final SlimeLabyrinthGame game) {
		WidgetGroup groupMain = new WidgetGroup();
		buttonPlay = new TextButton("Play", skin);
		buttonPlay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PlayScreen(game, 3));
			}
		});
		buttonPlay.setWidth(Gdx.graphics.getWidth()/2);
		buttonPlay.setHeight(Gdx.graphics.getHeight()/15);
		buttonPlay.setPosition(Gdx.graphics.getWidth()/2-buttonPlay.getWidth()/2,Gdx.graphics.getHeight()/2f);
		
		buttonSetting = new TextButton("Settings", skin);
		buttonSetting.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new PlayScreen(game, 3));
			}
		});
		buttonSetting.setWidth(Gdx.graphics.getWidth()/2);
		buttonSetting.setHeight(Gdx.graphics.getHeight()/15);
		buttonSetting.setPosition(Gdx.graphics.getWidth()/2-buttonSetting.getWidth()/2,Gdx.graphics.getHeight()/2f-buttonPlay.getHeight()-10);
		groupMain.addActor(buttonPlay);
		this.addActor(groupMain);
		return buttonPlay;
	}

}
