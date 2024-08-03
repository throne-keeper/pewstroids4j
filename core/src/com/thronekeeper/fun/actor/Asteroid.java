package com.thronekeeper.fun.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Asteroid extends BaseActor {

    public Asteroid(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("asteroid.png");
        float random = MathUtils.random(30);
        addAction(Actions.forever(Actions.rotateBy(30 + random, 1)));
        setSpeed(50 + random);
        setDeceleration(0);
        setMotionAngle(MathUtils.random(360));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        applyPhysics(delta);
        wrapAroundWorld();
    }
}
