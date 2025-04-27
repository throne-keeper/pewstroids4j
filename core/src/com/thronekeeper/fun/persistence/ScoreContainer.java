package com.thronekeeper.fun.persistence;

import com.badlogic.gdx.utils.Array;

public class ScoreContainer {

    private Array<Score> scores;

    public ScoreContainer(Array<Score> scores) {
        this.scores = scores;
    }

    public ScoreContainer() {
        this.scores = new Array<>();
    }

    public Array<Score> getScores() {
        return scores;
    }

    public void setScores(Array<Score> scores) {
        this.scores = scores;
    }

    public void addScore(Score score) {
        scores.add(score);
    }
}
