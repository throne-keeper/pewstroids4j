package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.thronekeeper.fun.BaseGame;
import com.thronekeeper.fun.PewstroidsGame;
import com.thronekeeper.fun.actor.BaseActor;
import com.thronekeeper.fun.util.FontFactory;

public class GameOverScreen extends BaseScreen {

    private Button playAgainButton;
    private Button exitGameButton;

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.setSize(1200, 720);
        space.loadTexture(SPACE_BACKGROUND);
        BaseActor.setWorldBounds(space);
        mainStage.addActor(space);
        BitmapFont font = FontFactory.createDefaultFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label gameOverLabel = new Label("Game Over\nFinal Score: " + PewstroidsGame.finalScore, labelStyle);
        uiStage.addActor(gameOverLabel);
        gameOverLabel.setPosition(getLabelPositionWidth(gameOverLabel), getLabelPositionHeight(gameOverLabel));
        Skin skin = new Skin(Gdx.files.internal("skins/holo/skin/dark-hdpi/Holo-dark-hdpi.json"));

        exitGameButton = new TextButton("Exit", skin, "default");
        exitGameButton.setPosition(mainStage.getWidth()/2, mainStage.getHeight()/4);
        exitGameButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                System.exit(0);
            }
            return false;
        });

        playAgainButton = new TextButton("Play Again", skin, "default");
        playAgainButton.setPosition(exitGameButton.getX() - playAgainButton.getWidth(), mainStage.getHeight()/4);
        playAgainButton.addListener(event -> {
           if (event.toString().equals("touchDown")) {
               BaseGame.setActiveScreen(new LevelScreen());
           }
           return false;
        });

        mainStage.addActor(exitGameButton);
        mainStage.addActor(playAgainButton);
    }

    private float getLabelPositionWidth(Label label) {
        return uiStage.getWidth() / 2 - label.getWidth() / 2;
    }

    private float getLabelPositionHeight(Label label) {
        return uiStage.getHeight() / 2 - label.getHeight();
    }

    @Override
    public void update(float delta) {

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
