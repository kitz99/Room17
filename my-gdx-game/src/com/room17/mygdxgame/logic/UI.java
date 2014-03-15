package com.room17.mygdxgame.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;

public class UI implements Disposable {
	private Stage stage;
	private Skin skinner;

	private Touchpad touchpad;
	private TouchpadStyle touchpadStyle;
	
	private Drawable touchBackground;
	private Drawable touchKnob;

	private Button btnA;
	private Button btnB;

	private int score;
	private String yourScore;
	private String yourSteps;
	private BitmapFont font;
	private Label textScore;
	private Label stepsTaken;
	private LabelStyle textStyle;

	public UI(Batch batch) {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true, batch);

		font = new BitmapFont(Gdx.files.internal("UI/fonts/arialblack.fnt"),
				Gdx.files.internal("UI/fonts/arialblack.png"), false);

		skinner = new Skin();
		skinner.add("touchBackground", new Texture("UI/touchBackground.png"));
		skinner.add("touchKnob", new Texture("UI/touchKnob.png"));
		skinner.add("a", new Texture("UI/A.png"));
		skinner.add("aa", new Texture("UI/AA.png"));
		skinner.add("b", new Texture("UI/B.png"));
		skinner.add("bb", new Texture("UI/BB.png"));

		score = 0;
		yourScore = "score: ";
		yourSteps = "steps: ";

		textStyle = new LabelStyle();
		textStyle.font = font;
		textScore = new Label(yourScore + score, textStyle);
		stepsTaken = new Label(yourSteps + InStream.getSteps(), textStyle);
		stepsTaken.setColor(Color.WHITE);
		textScore.setColor(Color.WHITE);

		textScore.setBounds(10, Gdx.graphics.getHeight() - 20, 0, 0);
		stepsTaken.setBounds(10, Gdx.graphics.getHeight() - 50, 0, 0);

		touchpadStyle = new TouchpadStyle();
		touchBackground = skinner.getDrawable("touchBackground");
		touchKnob = skinner.getDrawable("touchKnob");
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		touchpad = new Touchpad(10, touchpadStyle);
		float sizeCircle = Gdx.graphics.getWidth() * 0.25f;
		touchpad.setBounds(15, 15, sizeCircle, sizeCircle);

		btnA = new Button(skinner.getDrawable("a"), skinner.getDrawable("aa"));
		btnA.setPosition(Gdx.graphics.getWidth() - 200, 30);

		btnB = new Button(skinner.getDrawable("b"), skinner.getDrawable("bb"));
		btnB.setPosition(Gdx.graphics.getWidth() - 100, 30);

		stage.addActor(btnA);
		stage.addActor(btnB);
		stage.addActor(touchpad);
		stage.addActor(textScore);
		stage.addActor(stepsTaken);

		Gdx.input.setInputProcessor(stage);
	}

	public Touchpad getTouch() {
		return touchpad;
	}

	public float getX() {
		return touchpad.getKnobPercentX();
	}

	public float getY() {
		return touchpad.getKnobPercentY();
	}

	@Override
	public void dispose() {
		skinner.dispose();
		stage.dispose();
		font.dispose();
	}

	public void act(float d) {
		stage.act(d);
		stage.draw();
	}

	public boolean isPressedA() {
		return btnA.isPressed();
	}

	public void setSteps() {
		stepsTaken.setText(yourSteps + InStream.getSteps());
	}

	public void updtScore() {
		score++;
		textScore.setText(yourScore + score);
	}
}
