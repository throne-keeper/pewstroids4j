package com.thronekeeper.fun.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class ForceField extends BaseActor {

    public ForceField(float x, float y, Stage stage) {
        super(x, y, stage);
        loadAnimation(new String[] { "invincible_1.png", "invincible_2.png" }, 0.25f, true);
    }
}
