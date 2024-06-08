package com.thronekeeper.fun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;

public abstract class BaseGame extends Game {

    private static BaseGame game;

    public BaseGame() {
        game = this;
    }

    public static void setActiveScreen(Screen s) {
        game.setScreen(s);
    }

    @Override
    public void create() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
}
