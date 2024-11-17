package com.thronekeeper.fun.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontFactory {

    public static BitmapFont createDefaultFont() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("anita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = 48;
        parameters.color = Color.WHITE;
        parameters.borderWidth = 2;
        parameters.borderStraight = true;
        parameters.minFilter = Texture.TextureFilter.Linear;
        parameters.magFilter = Texture.TextureFilter.Linear;
        return fontGenerator.generateFont(parameters);
    }

    public static BitmapFont createFont(Color color, Color borderColor, int size, int borderWidth) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("anita.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = size;
        parameters.color = color;
        parameters.borderColor = borderColor;
        parameters.borderWidth = borderWidth;
        parameters.borderStraight = true;
        parameters.minFilter = Texture.TextureFilter.Linear;
        parameters.magFilter = Texture.TextureFilter.Linear;
        return fontGenerator.generateFont(parameters);
    }
}
