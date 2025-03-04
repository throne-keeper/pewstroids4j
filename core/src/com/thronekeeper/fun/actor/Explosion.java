package com.thronekeeper.fun.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.thronekeeper.fun.config.Resource;

public class Explosion extends BaseActor {

    public Explosion(float x, float y, Stage stage) {
        super(x, y, stage);
        loadAnimation(Resource.KABOOM_ANIMATION, 0.25f, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isAnimationFinished()) {
            remove();
        }
    }
}
