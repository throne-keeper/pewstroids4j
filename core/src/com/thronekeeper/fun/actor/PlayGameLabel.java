package com.thronekeeper.fun.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.TimeUtils;

public class PlayGameLabel extends BaseActor {

    private static final String PLAY_GAME_TEXT = "1 COIN    1 PLAY";

    private Label label;
    private long startTime = 0;

    public PlayGameLabel(float x, float y, Stage stage) {
        super(x, y, stage);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("anita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = 48;
        parameters.color = Color.WHITE;
        parameters.borderWidth = 2;
        parameters.borderColor = Color.WHITE;
        parameters.borderStraight = true;
        parameters.minFilter = Texture.TextureFilter.Linear;
        parameters.magFilter = Texture.TextureFilter.Linear;
        BitmapFont font = fontGenerator.generateFont(parameters);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        this.label = new Label("1 COIN    1 PLAY", labelStyle);
        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        if (TimeUtils.timeSinceNanos(startTime) > 1_000_000_000) {
//            label.setVisible(!label.isVisible());
//        }
    }
}
