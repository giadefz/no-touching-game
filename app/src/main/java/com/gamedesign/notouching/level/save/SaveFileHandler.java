package com.gamedesign.notouching.level.save;

import android.util.Log;

import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.util.GsonMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

public class SaveFileHandler {

    private static final SaveFile save = new SaveFile();
    private static final String saveFileName = "save.json";


    public static void save(Game game, int levelNumber, int totalPoints, long seed) {
        save.levelNumber = levelNumber;
        save.seed = seed;
        save.totalPoints = totalPoints;
        try (OutputStream outputStream = game.getFileIO().writeFile(saveFileName)) {
            outputStream.write(GsonMapper.toJsonByteArray(save));
        } catch (IOException e) {
            Log.println(Log.ASSERT, "SAVEFILE", "Failed to save: " + e);
        }

    }

    public static SaveFile readSave(Game game) {
        try (Reader saveFileReader = new InputStreamReader(game.getFileIO().readFile(saveFileName))) {
            return GsonMapper.fromJson(saveFileReader, SaveFile.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static void resetSave(Game game) {
        save(game, 0, 0, 0);
    }

}
