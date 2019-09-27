package cn.yesterday17.majsoul_android.majsoul;

import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.utils.Network;

import static cn.yesterday17.majsoul_android.Global.gson;

public class ResourceVersion {
    private static final String TAG = "ResourceVersion";
    static ResVersionJson resVersion = null;
    static String error = null;

    public static void loadResourceVersion(Callable<Void> callback) {
        new Thread(() -> {
            try {
                if (resVersion == null) {
                    String data = Network.getString(Global.gameUrl + "resversion" + GameVersion.gameVersion + ".json");
                    resVersion = gson.fromJson(data, ResVersionJson.class);
                    error = null;
                }
            } catch (IOException e) {
                resVersion = null;
                error = e.getMessage();
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

    static class ResVersionJson {
        Map<String, ResVersionPrefix> res;
    }

    static class ResVersionPrefix {
        String prefix;
    }
}
