package com.thronekeeper.fun.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Explosion extends BaseActor {

    private static final String[] animations = {"kaboom1.png", "kaboom2.png", "kaboom3.png"};

    public Explosion(float x, float y, Stage stage) {
        super(x, y, stage);
        loadAnimation(animations, 0.25f, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isAnimationFinished()) {
            remove();
        }
    }
}
