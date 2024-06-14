package com.thronekeeper.fun.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Spaceship extends BaseActor {

    private Thrusters thrusters;
//    private Shield shield;

    public Spaceship(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("spaceship.png");
        setBoundaryPolygon(8);
        setAcceleration(200);
        setMaxSpeed(100);
        setDeceleration(10);
        thrusters = new Thrusters(0, 0, stage);
        addActor(thrusters);
        thrusters.setPosition(-thrusters.getWidth(), getHeight()/2 - thrusters.getHeight()/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        float degreesPerSecond = 120;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            rotateBy(degreesPerSecond * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            rotateBy(-degreesPerSecond * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerateAtAngle(getRotation());
            thrusters.setVisible(true);
        } else {
            thrusters.setVisible(false);
        }
        applyPhysics(delta);
        wrapAroundWorld();
    }

    public void shoot() {
        if (getStage() != null) {
            Beam beam = new Beam(0, 0, getStage());
            beam.centerAtActor(this);
            beam.setRotation(this.getRotation());
            beam.setMotionAngle(this.getRotation());
        }
    }

    public void warp() {
        // TODO Stub!
    }
}
