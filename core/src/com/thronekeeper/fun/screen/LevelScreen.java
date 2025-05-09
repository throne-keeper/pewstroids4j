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
import com.thronekeeper.fun.config.Resource;

import javax.print.DocFlavor;
import java.util.logging.Logger;


public class LevelScreen extends BaseScreen {

    private static final Logger LOGGER = Logger.getLogger(LevelScreen.class.getName());
    private static final long INTERVAL = 5_000_000_000L; // 5 seconds I think??


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
    private long lastInvincibleInitiated;

    private Array<Asteroid> smallAsteroids;
    private Array<BaseActor> lifeLabels;

    @Override
    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.setSize(1200, 720);
        space.loadTexture(Resource.SPACE_BACKGROUND);
        BaseActor.setWorldBounds(space);
        spaceship = new Spaceship(400, 300, mainStage);
        new Asteroid(600, 500, mainStage, Resource.ASTEROID, AsteroidType.BIG);
        new Asteroid(600, 300, mainStage, Resource.ASTEROID, AsteroidType.BIG);
        new Asteroid(300, 300, mainStage, Resource.ASTEROID, AsteroidType.BIG);
        new Asteroid(300, 600, mainStage, Resource.ASTEROID, AsteroidType.BIG);
        gameOver = false;
        pewSound = Gdx.audio.newSound(Gdx.files.internal(Resource.PEW_SOUND));
        explode = Gdx.audio.newSound(Gdx.files.internal(Resource.EXPLODE_SOUND));
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
        Texture texture = new Texture(Gdx.files.internal(Resource.SPACESHIP));
        for (int i = 1; i <= lives; i++) {
            BaseActor life = new BaseActor(startingPos, uiStage.getHeight() - texture.getHeight(), uiStage);
            life.loadTexture(Resource.SPACESHIP);
            life.setRotation(90);
            lifeLabels.add(life);
            startingPos = texture.getWidth() * (float) i;
        }

        lifeLabels.forEach(uiStage::addActor);
    }

    @Override
    public void update(float delta) {
        for (BaseActor asteroid : BaseActor.getActors(mainStage, Asteroid.class)) {
            if (asteroid.overlaps(spaceship) && !spaceship.isInvincible()) {
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
                        b.remove();
                        explodeAndRemove(asteroidActor);
                        score++;
                        scoreLabel.setText("Score: " + String.format(scoreTemplate, score));
                        if (asteroidActor.getType().equals(AsteroidType.BIG)) {
                            sendInAsteroidBits(asteroidActor);
                        }
                    }
                } else if (b.overlaps(spaceship) && b.getOwner().equals(ActorType.COMPUTER) && !spaceship.isInvincible()) {
                    b.remove();
                    explodeAndRemove(spaceship);
                    removeLifeLabel();
                    initiateNewLife();
                    spaceship.setPosition(-1000, -1000);
                    if (lives <= 0) {
                        gameOver();
                    } else {
                        lives--;
                        initiateNewLife();
                    }
                } else if (b.overlaps(saucer) && b.getOwner().equals(ActorType.PLAYER)) {
                    b.remove();
                    explodeAndRemove(saucer);
                    score++;
                    scoreLabel.setText("Score: " + String.format(scoreTemplate, score));
                    lastSaucerSpawn = TimeUtils.nanoTime();
                    // TODO Add in custom animation animation
                }
            }
        }

        fireAtPlayer();
        checkGameOver();
        if (spaceship.isInvincible()) {
            /*
               Diane, add flashing animation for invincibility
             */
            updateInvincibility();
        }
    }

    private void initiateTemporaryInvincibility() {
        if (!spaceship.isInvincible()) {
            lastInvincibleInitiated = TimeUtils.nanoTime();
            LOGGER.info("Player now has 5 second invincibility");
            spaceship.setInvincible(true);
        }
    }

    private void updateInvincibility() {
        if (spaceship.isInvincible()) {
            LOGGER.info("Player is invincible!");
            if (TimeUtils.timeSinceNanos(lastInvincibleInitiated) > INTERVAL) {
                spaceship.setInvincible(false);
                LOGGER.info("Player is no longer invincible");
            } else {
                LOGGER.info("Player is still invincible!");
            }
        }
    }


    private void explodeAndRemove(BaseActor actor) {
        Explosion explosion = new Explosion(0, 0, mainStage);
        explosion.centerAtActor(actor);
        actor.remove();
        explode.play();
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
        initiateTemporaryInvincibility();
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
