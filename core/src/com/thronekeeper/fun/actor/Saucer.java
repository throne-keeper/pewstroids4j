package com.thronekeeper.fun.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.thronekeeper.fun.config.ActorType;

public class Saucer extends BaseActor {

    private final Sound saucerSound;
    private boolean saucerSoundPlaying;

    public Saucer(float x, float y, Stage stage) {
        super(x, y, stage);
        addAction(Actions.moveBy(-2, 0, 1));
        setSpeed(50);
        setDeceleration(0);
        saucerSound = Gdx.audio.newSound(Gdx.files.internal("audio/ufo.mp3"));
        saucerSoundPlaying = false;
        loadAnimation(new String[] { "ufo_move_1.png", "ufo_move_2.png" }, 0.25f, true);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        applyPhysics(delta);
        if (!saucerSoundPlaying) {
            saucerSoundPlaying = true;
            long id = saucerSound.play();
            saucerSound.setLooping(id, true);
        }
    }

    public void shootAtPlayer(BaseActor player) {
        float angle = calculateAngle(player);
        if (getStage() != null) {
            Beam beam = new Beam(0, 0, getStage(), ActorType.COMPUTER);
            beam.centerAtActor(this);
            beam.setRotation(angle);
            beam.setMotionAngle(angle);
        }
    }

    private float calculateAngle(BaseActor player) {
        float x = player.getX() - this.getX();
        float y = player.getY() - this.getY();
        return Double.valueOf(Math.atan2(y, x) * 180f / Math.PI)
                .floatValue();
    }
}
