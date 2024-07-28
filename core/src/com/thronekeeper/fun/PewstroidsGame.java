package com.thronekeeper.fun;

import com.thronekeeper.fun.screen.LevelScreen;
import com.thronekeeper.fun.screen.MainMenuScreen;

public class PewstroidsGame extends BaseGame {

    @Override
    public void create() {
        super.create();
        setActiveScreen(new MainMenuScreen());
    }

}