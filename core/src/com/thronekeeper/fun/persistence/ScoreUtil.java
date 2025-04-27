package com.thronekeeper.fun.persistence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ScoreUtil {

    private static final String TAG = ScoreUtil.class.getSimpleName();
    private static final String SCORE_JSON = "score/scores.json";

    private ScoreUtil() {}

    public static ScoreContainer deserializeScores() {
        FileHandle fileHandle = Gdx.files.internal(SCORE_JSON);
        Json json = new Json();
        return json.fromJson(ScoreContainer.class, fileHandle);
    }

    public static void serializeScores(ScoreContainer scoreContainer) {
        FileHandle fileHandle = Gdx.files.internal(SCORE_JSON);
        try (Writer fw = new FileWriter(fileHandle.file())) {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            json.toJson(scoreContainer, fw);
        } catch (IOException e) {
            Gdx.app.error(TAG, "", e);
        }
    }

}
