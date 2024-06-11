package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen implements Screen, InputProcessor {

    protected Stage mainStage;
    protected Stage uiStage;

    private Animation<TextureRegion> animation;
    private Vector2 velocityVector;
    private Vector2 accelerationVector;

    private float elapsedTime;

    public BaseScreen() {
        this.mainStage = new Stage();
        this.uiStage = new Stage();
        initialize();
    }

    public abstract void initialize();
    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        uiStage.act(delta);
        mainStage.act(delta);
        update(delta);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }

    @Override
    public void show() {
        InputProcessor inputProcessor = Gdx.input.getInputProcessor();
        if (inputProcessor instanceof InputMultiplexer inputMultiplexer) {
            inputMultiplexer.addProcessor(this);
            inputMultiplexer.addProcessor(uiStage);
            inputMultiplexer.addProcessor(mainStage);
        }
    }

    @Override
    public void hide() {
        InputProcessor inputProcessor = Gdx.input.getInputProcessor();
        if (inputProcessor instanceof InputMultiplexer inputMultiplexer) {
            inputMultiplexer.removeProcessor(this);
            inputMultiplexer.removeProcessor(uiStage);
            inputMultiplexer.removeProcessor(mainStage);
        }
    }
}
