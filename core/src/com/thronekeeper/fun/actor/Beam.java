package com.thronekeeper.fun.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.thronekeeper.fun.config.ActorType;
import com.thronekeeper.fun.config.Resource;

public class Beam extends BaseActor {

    private final ActorType owner;

    public Beam(float x, float y, Stage stage, ActorType owner) {
        super(x, y, stage);
        this.owner = owner;
        if (owner.equals(ActorType.PLAYER)) {
            loadTexture(Resource.SPACESHIP_BEAM);
            addAction(Actions.delay(1));
            addAction(Actions.after(Actions.fadeOut(0.5f)));
            addAction(Actions.after(Actions.removeActor()));
            setSpeed(300);
            setMaxSpeed(300);
            setDeceleration(0);
        } else if (owner.equals(ActorType.COMPUTER)) {
            loadTexture(Resource.EVIL_BEAM);
            addAction(Actions.delay(2));
            addAction(Actions.after(Actions.fadeOut(0.5f)));
            addAction(Actions.after(Actions.removeActor()));
            setSpeed(250);
            setMaxSpeed(250);
            setDeceleration(0);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        applyPhysics(delta);
        wrapAroundWorld();
    }

    public ActorType getOwner() {
        return owner;
    }
}
