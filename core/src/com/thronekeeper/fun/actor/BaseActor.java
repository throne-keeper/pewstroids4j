package com.thronekeeper.fun.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;
import java.util.List;


public abstract class BaseActor extends Group {

    private static Rectangle worldBounds;

    private Stage stage;
    private Animation<TextureRegion> animation;
    private boolean animationPaused;
    
    private Vector2 velocityVector;
    private Vector2 accelerationVector;
    
    private float maxSpeed;
    private float deceleration;
    private float acceleration;
    private float elapsedTime;
    
    private Polygon boundary;

    public BaseActor(float x, float y, Stage stage) {
        super();
        setPosition(x, y);
        stage.addActor(this);
        animation = null;
        elapsedTime = 0f;
        animationPaused = false;
        velocityVector = new Vector2(0, 0); 
        accelerationVector = new Vector2(0, 0);
        acceleration = 0f;
        maxSpeed = 1000;
        deceleration = 0f;
    }

    public static List<BaseActor> getActors(Stage stage, Class<? extends BaseActor> clazz) {
        List<BaseActor> list = new LinkedList<>();
        for (Actor a : stage.getActors()) {
            if (clazz.isInstance(a)) {
                list.add((BaseActor) a);
            }
        }
        return list;
    }

    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    public static void setWorldBounds(BaseActor actor) {
        setWorldBounds(actor.getWidth(), actor.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!animationPaused) {
            elapsedTime += delta;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (animation != null && isVisible()) {
            batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(),
                    getOriginX(), getOriginY(),
                    getWidth(), getHeight(),
                    getScaleX(), getScaleY(),
                    getRotation());
        }
        super.draw(batch, parentAlpha);
    }

    public Animation<TextureRegion> loadAnimation(String[] fileNames, float duration, boolean loop) {
        if (this.animation == null) {
            Animation.PlayMode playMode = loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL;
            Array<TextureRegion> textureRegions = new Array<>();
            for (String fileName : fileNames) {
                Texture texture = new Texture(Gdx.files.internal(fileName));
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                textureRegions.add(new TextureRegion(texture));
            }
            this.animation = new Animation<>(duration, textureRegions, playMode);
        }
        return animation;
    }

    public void loadAnimation(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        if (this.animation == null) {
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            int frameWidth = texture.getWidth() / cols;
            int frameHeight = texture.getHeight() / rows;
            TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
            Array<TextureRegion> textureRegions = new Array<>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    textureRegions.add(temp[r][c]);
                }
            }
            Animation.PlayMode playMode = loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL;
            Animation<TextureRegion> animation = new Animation<>(frameDuration, textureRegions, playMode);
            setAnimation(animation);
        }
    }

    public Animation<TextureRegion> loadTexture(String fileName) {
        return loadAnimation(new String[] { fileName }, 1, true);
    }

    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        TextureRegion textureRegion = animation.getKeyFrame(0f);
        float w = textureRegion.getRegionWidth();
        float h = textureRegion.getRegionHeight();
        setSize(w, h);
        setOrigin(w / 2, h / 2);
        if (boundary == null) {
            setBoundaryRectangle();
        }
    }

    public void setAnimationPaused(boolean pause) {
        this.animationPaused = pause;
    }

    public boolean isAnimationFinished() {
        return this.animation != null && this.animation.isAnimationFinished(elapsedTime);
    }

    public void applyPhysics(float deltaTime) {
        velocityVector.add(accelerationVector.x * deltaTime, accelerationVector.y * deltaTime);
        float speed = getSpeed();
        if (accelerationVector.len() == 0) {
            speed -= deceleration * deltaTime;
        }
        speed = MathUtils.clamp(speed, 0, maxSpeed);
        setSpeed(speed);
        moveBy(velocityVector.x * deltaTime, velocityVector.y * deltaTime);
        accelerationVector.set(0, 0);
    }

    public void setSpeed(float speed) {
        if (velocityVector.len() == 0) {
            velocityVector.set(speed, 0);
        } else {
            velocityVector.setLength(speed);
        }
    }

    public float getSpeed() {
        return velocityVector.len();
    }

    public float setMotionAngle() {
        return velocityVector.angleDeg();
    }

    public float getMotionAngle() {
        return velocityVector.angleDeg();
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void accelerateAtAngle(float angle) {
        accelerationVector.add(new Vector2(acceleration, 0)).setAngleDeg(angle);
    }

    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }

    public boolean isMoving() {
        return getSpeed() > 0;
    }

    private void setBoundaryRectangle() {
        float w = getWidth();
        float h = getWidth();
        float[] vertices = {0,0, w,0, w,h, 0, h};
        boundary = new Polygon(vertices);
    }

    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * MathUtils.PI2 / numSides;
            vertices[i*2] = w/2 * MathUtils.cos(angle) + w/2;
            vertices[(i*2)+1] = h/2 * MathUtils.sin(angle) + h/2;
        }
        boundary = new Polygon(vertices);
    }

    public Polygon getBoundaryPolygon() {
        boundary.setPosition(getX(), getY());
        boundary.setOrigin(getOriginX(), getOriginY());
        boundary.setRotation(getRotation());
        boundary.setScale(getScaleX(), getScaleY());
        return boundary;
    }

    public boolean overlaps(BaseActor other) {
        Polygon me = this.boundary;
        Polygon them = other.getBoundaryPolygon();
        if (!me.getBoundingRectangle().overlaps(them.getBoundingRectangle())) {
            return false;
        }
        return Intersector.overlapConvexPolygons(me, them);
    }

    public void centerAtPosition(float x, float y) {
        setPosition(x - getWidth()/2, y - getHeight()/2);
    }

    public void centerAtActor(BaseActor actor) {
        centerAtPosition(actor.getX() + actor.getWidth()/2, actor.getY() + actor.getHeight()/2);
    }

    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }

    public void wrapAroundWorld() {
        if (getX() + getWidth() < 0) {
            setX(worldBounds.width);
        }
        if (getX() > worldBounds.width) {
            setX(-getWidth());
        }
        if (getY() + getHeight() < 0) {
            setY(worldBounds.height);
        }
        if (getY() > worldBounds.height) {
            setY(-getHeight());
        }
    }
}
