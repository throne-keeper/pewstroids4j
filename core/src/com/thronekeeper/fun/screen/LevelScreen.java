package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.thronekeeper.fun.actor.Asteroid;
import com.thronekeeper.fun.actor.BaseActor;
import com.thronekeeper.fun.actor.Beam;
import com.thronekeeper.fun.actor.Explosion;
import com.thronekeeper.fun.actor.Spaceship;

public class LevelScreen extends BaseScreen {

    private Spaceship spaceship;
    private boolean gameOver;

    private Sound pewSound;
    private Sound bzz;
    private Sound explode;

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.setSize(800, 600);
        space.loadTexture("space.jpg");
        BaseActor.setWorldBounds(space);
        spaceship = new Spaceship(400, 300, mainStage);
        new Asteroid(600, 500, mainStage);
        new Asteroid(600, 300, mainStage);
        new Asteroid(300, 300, mainStage);
        new Asteroid(300, 600, mainStage);
        gameOver = false;
        pewSound = Gdx.audio.newSound(Gdx.files.internal("audio/pew.mp3"));
        bzz = Gdx.audio.newSound(Gdx.files.internal("audio/bzz.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("audio/explode.mp3"));
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
                if (beam.overlaps(asteroid)) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    explode.play();
                    boom.centerAtActor(asteroid);
                    asteroid.remove();
                }
            }
        }

        if (!gameOver && BaseActor.getActors(mainStage, Asteroid.class).isEmpty()) {
            gameOver = true;
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
