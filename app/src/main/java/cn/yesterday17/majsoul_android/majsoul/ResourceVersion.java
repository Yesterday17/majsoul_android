package cn.yesterday17.majsoul_android.majsoul;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.extension.ExtensionManager;
import cn.yesterday17.majsoul_android.extension.metadata.Metadata;
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
                    // 加载 resVersion 文件
                    String data = Network.getString(Global.gameUrl + "resversion" + GameVersion.gameVersion + ".json");
                    resVersion = gson.fromJson(data, ResVersionJson.class);

                    // 对不存在的键进行创建
                    Map<String, Metadata> extensions = ExtensionManager.GetInstance().getExtensions();
                    extensions.values().forEach((value) ->
                            value.getReplace().forEach((entry) ->
                                    entry.getFrom().forEach((key) ->
                                            resVersion.res.putIfAbsent(
                                                    key,
                                                    new ResVersionPrefix("v0.0.0.a")
                                            )
                                    )
                            )
                    );

                    // 对资源进行缓存
                    ResourceReplace.initReplaceCache(extensions);

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
        Map<String, ResVersionPrefix> res = new HashMap<>();
    }

    static class ResVersionPrefix {
        String prefix;

        ResVersionPrefix(String prefix) {
            this.prefix = prefix;
        }
    }
}
