package com.thronekeeper.fun;

import com.thronekeeper.fun.screen.LevelScreen;

public class PewstroidsGame extends BaseGame {

    @Override
    public void create() {
        super.create();
        setActiveScreen(new LevelScreen());
    }

}