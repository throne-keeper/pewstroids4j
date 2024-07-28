package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.thronekeeper.fun.PewstroidsGame;
import com.thronekeeper.fun.actor.Asteroid;
import com.thronekeeper.fun.actor.BaseActor;


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

        new Asteroid(600, 500, mainStage);
        new Asteroid(600, 500, mainStage);
        new Asteroid(600, 300, mainStage);
        new Asteroid(300, 300, mainStage);
        new Asteroid(300, 600, mainStage);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("anita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = 48;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = fontGenerator.generateFont(fontParameters);
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
