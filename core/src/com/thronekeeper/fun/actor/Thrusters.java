package com.thronekeeper.fun.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Thrusters extends BaseActor{
    public Thrusters(float x, float y, Stage stage) {
        super(x, y, stage);
        loadAnimation(new String[] { "bzz_one.png", "bzz_two.png" }, 0.25f, true);
    }
}
