package com.thronekeeper.fun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.thronekeeper.fun.PewstroidsGame;
import com.thronekeeper.fun.actor.Asteroid;
import com.thronekeeper.fun.actor.BaseActor;
import com.thronekeeper.fun.actor.Beam;
import com.thronekeeper.fun.actor.Explosion;
import com.thronekeeper.fun.actor.Saucer;
import com.thronekeeper.fun.actor.Spaceship;
import com.thronekeeper.fun.config.ActorType;
import com.thronekeeper.fun.config.AsteroidType;

import java.util.logging.Logger;


public class LevelScreen extends BaseScreen {

    private static final Logger LOGGER = Logger.getLogger(LevelScreen.class.getName());

    private Spaceship spaceship;
    private Sound pewSound;
    private Sound explode;

    private Label scoreLabel;
    private String scoreTemplate = "%02d";

    private boolean gameOver;
    private int score;
    private Saucer saucer;
    private int lives;

    private long lastFireTime;
    private long lastSaucerSpawn;

    private Array<Asteroid> smallAsteroids;
    private Array<BaseActor> lifeLabels;

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.setSize(1200, 720);
        space.loadTexture("space.jpg");
        BaseActor.setWorldBounds(space);
        spaceship = new Spaceship(400, 300, mainStage);
        new Asteroid(600, 500, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(600, 300, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(300, 300, mainStage, "asteroid.png", AsteroidType.BIG);
        new Asteroid(300, 600, mainStage, "asteroid.png", AsteroidType.BIG);
        gameOver = false;
        pewSound = Gdx.audio.newSound(Gdx.files.internal("audio/pew.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("audio/explode.mp3"));
        scoreLabel = initializeScoreLabel();
        uiStage.addActor(scoreLabel);
        score = 0;
        lastSaucerSpawn = TimeUtils.nanoTime();
        lastFireTime = TimeUtils.nanoTime();
        lives = 4;
        initializeLives();
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

    private void initializeLives() {
        lifeLabels = new Array<>(lives);
        float startingPos = 0f;
        Texture texture = new Texture(Gdx.files.internal("spaceship.png"));
        for (int i = 1; i <= lives; i++) {
            BaseActor life = new BaseActor(startingPos, uiStage.getHeight() - texture.getHeight(), uiStage);
            life.loadTexture("spaceship.png");
            life.setRotation(90);
            lifeLabels.add(life);
            startingPos = texture.getWidth() * (float) i;
        }

        lifeLabels.forEach(uiStage::addActor);
    }

    @Override
    public void update(float delta) {
        for (BaseActor asteroid : BaseActor.getActors(mainStage, Asteroid.class)) {
            if (asteroid.overlaps(spaceship)) {
                Explosion boom = new Explosion(0, 0, mainStage);
                boom.centerAtActor(spaceship);
                spaceship.remove();
                removeLifeLabel();
                spaceship.setPosition(-1000, -1000);
                gameOver = true;
                explode.play();
                if (lives <= 0) {
                    gameOver();
                } else {
                    lives--;
                    initiateNewLife();
                }
            }
            for (BaseActor beam : BaseActor.getActors(mainStage, Beam.class)) {
                Beam b = (Beam) beam;
                if (b.getOwner().equals(ActorType.PLAYER) && (b.overlaps(asteroid))) {
                    if (asteroid instanceof Asteroid asteroidActor) {
                        Explosion boom = new Explosion(0, 0, mainStage);
                        explode.play();
                        boom.centerAtActor(asteroid);
                        asteroid.remove();
                        b.remove();
                        score++;
                        scoreLabel.setText("Score: " + String.format(scoreTemplate, score));
                        if (asteroidActor.getType().equals(AsteroidType.BIG)) {
                            sendInAsteroidBits(asteroidActor);
                        }
                    }
                } else if (b.overlaps(spaceship) && b.getOwner().equals(ActorType.COMPUTER)) {
                    // TODO (vomit emoji) All duplicate code
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(spaceship);
                    spaceship.remove();
                    removeLifeLabel();
                    initiateNewLife();
                    spaceship.setPosition(-1000, -1000);
                    explode.play();
                    if (lives <= 0) {
                        gameOver();
                    } else {
                        lives--;
                        initiateNewLife();
                    }
                } else if (b.overlaps(saucer) && b.getOwner().equals(ActorType.PLAYER)) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(saucer);
                    saucer.remove();
                    explode.play();
                    score++;
                    scoreLabel.setText("Score: " + String.format(scoreTemplate, score));
                    lastSaucerSpawn = TimeUtils.nanoTime();
                    // TODO Add in custom animation animation
                }
            }
        }

        fireAtPlayer();
        checkGameOver();
    }

    private void fireAtPlayer() {
        if (saucer != null && TimeUtils.timeSinceNanos(lastFireTime) > 1_000_000_000L) {
            saucer.shootAtPlayer(spaceship);
            lastFireTime = TimeUtils.nanoTime();
        }
    }

    private void checkGameOver() {
        if (!gameOver && BaseActor.getActors(mainStage, Asteroid.class).isEmpty()) {
            gameOver = true;
            gameOver();
        }
        if (!gameOver && BaseActor.getActors(mainStage, Saucer.class).isEmpty()
                && TimeUtils.timeSinceNanos(lastSaucerSpawn) > (10_000_000_000L)) {
                LOGGER.info("Time Hit");
                sendInTheSaucer();
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

    private void sendInAsteroidBits(Asteroid asteroid) {
        for (int i = 0; i < 4; i++) {
            new Asteroid(asteroid.getX(), asteroid.getY(), mainStage, "smallasteroid.png", AsteroidType.SMALL);
        }
    }

    private void initiateNewLife() {
        spaceship = new Spaceship(400, 300, mainStage);
    }

    private void removeLifeLabel() {
        if (!lifeLabels.isEmpty()) {
            lifeLabels.pop().remove();
        }
    }

    private void gameOver() {
        if (saucer != null) {
            saucer.remove();

        }
        PewstroidsGame.finalScore = score;
        PewstroidsGame.setActiveScreen(new GameOverScreen());
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
