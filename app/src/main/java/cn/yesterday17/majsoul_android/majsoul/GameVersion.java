package cn.yesterday17.majsoul_android.majsoul;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Callable;

import cn.yesterday17.majsoul_android.utils.Network;

import static cn.yesterday17.majsoul_android.Global.gson;

public class GameVersion {
    private static final String TAG = "GameVersion";
    static String gameVersion = null;
    static String error = null;

    public static void loadVersion(Callable<Void> callback) {
        new Thread(() -> {
            try {
                if (gameVersion == null || gameVersion.equals("")) {
                    String data = Network.getString("https://majsoul.union-game.com/app/web/html/version.json");
                    GameVersionJson response = gson.fromJson(data, GameVersionJson.class);
                    gameVersion = response.version;
                    error = null;
                }
            } catch (IOException e) {
                gameVersion = null;
                error = e.getMessage();
                Log.e(TAG, error);
            } finally {
                try {
                    if (callback != null)
                        callback.call();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }).start();
    }

    static class GameVersionJson {
        String code;
        String version;
    }
}