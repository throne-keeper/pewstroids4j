package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;
import com.thronekeeper.fun.actor.*;
import com.thronekeeper.fun.config.ActorType;

import java.sql.Time;


public class LevelScreen extends BaseScreen {

    private Spaceship spaceship;
    private Sound pewSound;
    private Sound explode;

    private Label scoreLabel;
    private String scoreTemplate = "%02d";

    private boolean gameOver;
    private int score;
    private long startTime;
    private Saucer saucer;

    private long lastFireTime;

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.setSize(1200, 720);
        space.loadTexture("space.jpg");
        BaseActor.setWorldBounds(space);
        spaceship = new Spaceship(400, 300, mainStage);
        new Asteroid(600, 500, mainStage);
        new Asteroid(600, 300, mainStage);
        new Asteroid(300, 300, mainStage);
        new Asteroid(300, 600, mainStage);
        gameOver = false;
        pewSound = Gdx.audio.newSound(Gdx.files.internal("audio/pew.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("audio/explode.mp3"));
        scoreLabel = initializeScoreLabel();
        uiStage.addActor(scoreLabel);
        score = 0;
        startTime = TimeUtils.nanoTime();
        lastFireTime = TimeUtils.nanoTime();
    }

    private Label initializeScoreLabel() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("anita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = 24;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.WHITE;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = fontGenerator.generateFont(fontParameters);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        var label = new Label("00", labelStyle);
        uiStage.addActor(label);
        label.setPosition(uiStage.getWidth()/2 - label.getWidth(), 0f);
        return label;
    }

    @Override
    public void update(float delta) {
        for (BaseActor asteroid : BaseActor.getActors(mainStage, Asteroid.class)) {
            if (asteroid.overlaps(spaceship)) {
                Explosion boom = new Explosion(0, 0, mainStage);
                boom.centerAtActor(spaceship);
                spaceship.remove();
                spaceship.setPosition(-1000, -1000);
                gameOver = true;
                explode.play();
                spaceship.remove();
                System.out.println("YOU LOSE. YOU'RE A LOSER");
            }
            for (BaseActor beam : BaseActor.getActors(mainStage, Beam.class)) {
                Beam b = (Beam) beam;
                if (b.getOwner().equals(ActorType.PLAYER) && (b.overlaps(asteroid))) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    explode.play();
                    boom.centerAtActor(asteroid);
                    asteroid.remove();
                    b.remove();
                    score++;
                    scoreLabel.setText("Score: " + String.format(scoreTemplate, score));
                } else if (b.overlaps(spaceship) && b.getOwner().equals(ActorType.COMPUTER)) {
                    // TODO (vomit emoji) All duplicate code
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(spaceship);
                    spaceship.remove();
                    spaceship.setPosition(-1000, -1000);
                    gameOver = true;
                    explode.play();
                    spaceship.remove();
                    System.out.println("YOU LOSE. YOU'RE A LOSER");
                }
            }
        }

        if (!gameOver && BaseActor.getActors(mainStage, Asteroid.class).isEmpty()) {
            gameOver = true;
        }
        if (!gameOver && BaseActor.getActors(mainStage, Saucer.class).isEmpty()) {
            if (TimeUtils.timeSinceNanos(startTime) > 10_000_000_000L) {
                sendInTheSaucer();
            }
        }
        if (saucer != null) {
            if (TimeUtils.timeSinceNanos(lastFireTime) > 1_000_000_000L) {
                saucer.shootAtPlayer(spaceship);
                lastFireTime = TimeUtils.nanoTime();
            }
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            System.exit(0);
        }
        if (keycode == Input.Keys.SPACE) {
            spaceship.shoot();
            pewSound.play();
        }
        return false;
    }

    public void sendInTheSaucer() {
        saucer = new Saucer(0, mainStage.getHeight()-100, mainStage);
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
