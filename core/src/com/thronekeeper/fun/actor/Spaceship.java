package com.thronekeeper.fun.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.thronekeeper.fun.config.ActorType;
import com.thronekeeper.fun.config.Resource;

public class Spaceship extends BaseActor {

    private final Thrusters thrusters;
    private final Sound bzz;
    private boolean invincible;

    private boolean bzzPlaying;

    public Spaceship(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture(Resource.SPACESHIP);
        setBoundaryPolygon(8);
        setAcceleration(200);
        setMaxSpeed(100);
        setDeceleration(10);
        thrusters = new Thrusters(0, 0, stage);
        bzz = Gdx.audio.newSound(Gdx.files.internal(Resource.BZZ_SOUND));
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
            if (!bzzPlaying) {
                bzzPlaying = true;
                var id = bzz.play();
                bzz.setLooping(id, true);
            }
        } else {
            bzzPlaying = false;
            bzz.stop();
            thrusters.setVisible(false);
        }
        applyPhysics(delta);
        wrapAroundWorld();
    }

    public void shoot() {
        if (getStage() != null) {
            Beam beam = new Beam(0, 0, getStage(), ActorType.PLAYER);
            beam.centerAtActor(this);
            beam.setRotation(this.getRotation());
            beam.setMotionAngle(this.getRotation());
        }
    }

    @Override
    public boolean remove() {
        if (bzzPlaying) {
            bzzPlaying = false;
            bzz.stop();
        }
        return super.remove();
    }

    public void warp() {
        // TODO Stub!
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }
}
