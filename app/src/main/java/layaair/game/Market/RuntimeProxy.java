package layaair.game.Market;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.webkit.ValueCallback;

import cn.yesterday17.majsoul_android.game.GameActivity;
import layaair.game.IMarket.IPluginRuntimeProxy;

public class RuntimeProxy implements IPluginRuntimeProxy {
    private String TAG = "RuntimeProxy";

    public String getCacheDir() {
        String sCache = GameActivity.GetInstance().getCacheDir().toString();
        String[] vString = sCache.split("/");
        String sNewCache = "";
        for (int i = 0; i < vString.length - 1; i++) {
            sNewCache += vString[i];
            sNewCache += "/";
        }
        return sNewCache;
    }


    @Override
    public Object laya_get_value(String key) {
        Log.d(TAG, "LayaGetValue: " + key);
        if (key.equalsIgnoreCase("CacheDir")) {
            return getCacheDir();
        }
        return "";
    }

    @Override
    public boolean laya_set_value(String key, Object value) {
        return false;
    }

    @Override
    public void laya_stop_game_engine() {
    }

    @Override
    public Object laya_invoke_Method(String method, Bundle param) {
        return null;
    }

    @Override
    public void Login(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void Logout(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void Pay(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void PushIcon(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void Share(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void OpenBBS(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void GetFriendsList(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

    @Override
    public void SendMessageToPlatform(JSONObject jsonObj, ValueCallback<JSONObject> callback) {
        callback.onReceiveValue(null);
    }

}
