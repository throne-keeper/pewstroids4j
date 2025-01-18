package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.thronekeeper.fun.PewstroidsGame;
import com.thronekeeper.fun.actor.Asteroid;
import com.thronekeeper.fun.actor.BaseActor;
import com.thronekeeper.fun.config.AsteroidType;
import com.thronekeeper.fun.util.FontFactory;


public class MainMenuScreen extends BaseScreen {

    private Label playLabel;
    private long startTime;

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.setSize(1200, 720);
        space.loadTexture("space.jpg");
        BaseActor.setWorldBounds(space);
        mainStage.addActor(space);

        new Asteroid(100, 500, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(200, 500, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(0, 500, mainStage, "smallasteroid.png", AsteroidType.SMALL);
        new Asteroid(600, 500, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(150, 500, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(30, 500, mainStage, "smallasteroid.png", AsteroidType.SMALL);

        BitmapFont font = FontFactory.createFont(Color.WHITE, Color.BLACK, 48, 2);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        playLabel = new Label("1 COIN    1 PLAY", labelStyle);
        uiStage.addActor(playLabel);
        playLabel.setPosition(uiStage.getWidth()/2 - playLabel.getWidth()/2, uiStage.getHeight()/4 - playLabel.getHeight());
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            PewstroidsGame.setActiveScreen(new LevelScreen());
        }
        if (TimeUtils.timeSinceNanos(startTime) > 500_000_000) {
            System.out.println(startTime);
            playLabel.setVisible(!playLabel.isVisible());
            startTime = TimeUtils.nanoTime();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
