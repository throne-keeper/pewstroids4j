package com.thronekeeper.fun.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Score extends BaseActor {

    private final Label label;
    private final String text = "Score: %d";
    private int score;

    public Score(float x, float y, Stage stage) {
        super(x, y, stage);
        this.score = 0;
        Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        label = new Label(String.format(text, score), labelStyle);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        label.setText(String.format(text, score));
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void increaseScore() {
        this.score++;
    }
}
