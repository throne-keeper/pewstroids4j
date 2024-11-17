package com.thronekeeper.fun;

import com.thronekeeper.fun.screen.MainMenuScreen;

public class PewstroidsGame extends BaseGame {

    public static int finalScore;

    @Override
    public void create() {
        super.create();
        setActiveScreen(new MainMenuScreen());
    }
}