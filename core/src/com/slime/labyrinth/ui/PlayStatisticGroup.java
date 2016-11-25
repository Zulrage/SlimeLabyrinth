package com.slime.labyrinth.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.TimeUtils;
import com.slime.labyrinth.listeners.IGameStatisticsListener;
import com.slime.labyrinth.tweens.LabelAccessor;
import com.slime.labyrinth.utils.EnumGameStatisticFields;
import com.slime.labyrinth.utils.GameStatics;
import com.slime.labyrinth.utils.GameStatistic;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

/**
 * UI class to create the header in the PlayScreen
 * @author Bertrand
 *
 */
public class PlayStatisticGroup extends WidgetGroup implements IGameStatisticsListener {

	private ProgressBar progress, comboProgressbar;
	private Label levelLabel;
	private Label score;
	private Label XPLabel;
	private Skin skin;

	private long lastScoredTime = 0;
	private Label currentScoreLabel, previousScoreLabel;
	private TweenManager tweenManager;

	public PlayStatisticGroup(Skin skin, float width) {
		GameStatistic.addListener(this);

		createUI(skin, width);

		tweenManager = new TweenManager();
		Tween.registerAccessor(Label.class, new LabelAccessor());
		tweenManager.update(Gdx.graphics.getDeltaTime());
	}

	private void createUI(Skin skin, float width) {
		this.skin = skin;
		addActors(skin, width);
	}

	private void addActors(Skin skin, float width) {
		score = new Label("0", skin, "small");
		progress = new ProgressBar(0, 100, 1, false, skin);
		progress.setAnimateDuration(1);
		
		Label staticLevelText = new Label("lvl.", skin, "small");
		levelLabel = new Label("0", skin, "small");
		XPLabel = new Label("0/5", skin, "small");
		XPLabel.setPosition(progress.getOriginX() + progress.getPrefWidth() / 2, -10);
		staticLevelText.setPosition(progress.getOriginX() + progress.getPrefWidth() + 5, -10);
		levelLabel.setPosition(progress.getOriginX() + progress.getPrefWidth() + 5 + staticLevelText.getPrefWidth() + 3,
				-10);
		score.setPosition(width - 60, -10);
		comboProgressbar = new ProgressBar(0, 100, 1, false, skin, "combo");
		comboProgressbar.setPosition(score.getX(), score.getY() - score.getHeight()*2);
		comboProgressbar.setWidth(50);
		setComboProgressbarAlpha(0);
		this.addActor(comboProgressbar);
		this.addActor(progress);
		this.addActor(XPLabel);
		this.addActor(staticLevelText);
		this.addActor(levelLabel);
		this.addActor(score);
	}

	@Override
	public void performAction(EnumGameStatisticFields field) {
		switch (field) {
		case XP:
			levelLabel.setText(("" + GameStatistic.getLevel()));
			XPLabel.setText((GameStatistic.getExperience() + "/" + GameStatistic.getExperienceNeeded()));
			progress.setValue(GameStatistic.getExperience() / (float) GameStatistic.getExperienceNeeded() * 100);
			break;
		case SCORE:
			if (TimeUtils.millis() > lastScoredTime + GameStatics.COMBO_TIME) {
				createTempScoreLabel();
			} else {
				currentScoreLabel.setText("" + GameStatistic.getComboScore());
			}
			setComboProgressbarAlpha(1);
			comboProgressbar.setValue(100);
			lastScoredTime = TimeUtils.millis();
			break;
		case LEVEL:
			levelLabel.setText(("" + GameStatistic.getLevel()));
			break;
		default:
			break;
		}

	}

	private void createTempScoreLabel() {
		currentScoreLabel = new Label("" + GameStatistic.getComboScore(), skin, "small");
		currentScoreLabel.setPosition(score.getX(), score.getY() - score.getHeight());
		this.addActor(currentScoreLabel);
	}

	@Override
	public void act(float delta) {
		tweenManager.update(delta);
		updateScore(delta);
		super.act(delta);
	}

	private void updateScore(float delta) {
		if (TimeUtils.millis() > lastScoredTime + GameStatics.COMBO_TIME) {
			if (currentScoreLabel != null) {
				previousScoreLabel = currentScoreLabel;
				currentScoreLabel = null;
				setComboProgressbarAlpha(0);
				this.removeActor(currentScoreLabel);
				this.addActor(previousScoreLabel);
				GameStatistic.setComboScore(0);
				GameStatistic.setComboNumber(0);;
				Tween.to(previousScoreLabel, LabelAccessor.ALPHA, 1f).target(0).start(tweenManager);
				Timeline.createSequence().beginSequence()
				.push(Tween.to(previousScoreLabel, LabelAccessor.Y, 1f).target(score.getY()))
				.push(Tween.to(score, LabelAccessor.NUMBER, 0.5f).target(GameStatistic.getScore()))
				.push(Tween.call(new TweenCallback() {
					
					@Override
					public void onEvent(int arg0, BaseTween<?> arg1) {
						removeActor(previousScoreLabel);
					}
				})).end().start(tweenManager);

			}
		} else {
			comboProgressbar.setValue((100*(float)(GameStatics.COMBO_TIME-TimeUtils.millis()+lastScoredTime)/GameStatics.COMBO_TIME));
		}
	}
	
	public void setComboProgressbarAlpha(float alpha) {
		comboProgressbar.setColor(comboProgressbar.getColor().r, comboProgressbar.getColor().g, comboProgressbar.getColor().b, alpha);
	}
}
