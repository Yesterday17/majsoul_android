package cn.yesterday17.majsoul_android.majsoul;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.Callable;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.utils.Network;

public class CodeJS {
    private static final String TAG = "CodeJS";
    static String codeJS = null;
    static String error = null;

    public static void loadCodeJS(Callable<Void> callback) {
        new Thread(() -> {
            try {
                if (codeJS == null || codeJS.equals("")) {
                    Log.d(TAG, Global.gameUrl + "v" + GameVersion.codeAddress);
                    codeJS = Network.getString(Global.gameUrl + "v" + GameVersion.gameVersion + "/code.js");
                    error = null;
                }
            } catch (IOException e) {
                codeJS = null;
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
}
