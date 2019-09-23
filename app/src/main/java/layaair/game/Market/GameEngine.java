package layaair.game.Market;

import cn.yesterday17.majsoul_android.game.GameActivity;
import layaair.game.IMarket.IPlugin;
import layaair.game.IMarket.IPluginRuntimeProxy;
import layaair.game.conch.ILayaEventListener;
import layaair.game.conch.ILayaGameEgine;
import layaair.game.conch.LayaConch5;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.yesterday17.majsoul_android.R;

public class GameEngine implements IPlugin {
    private static final String TAG = "LayaGameEngine";

    public ILayaGameEgine layaGameEngine = null;
    private Context mContext = null;
    private IPluginRuntimeProxy runtimeProxy = null;

    public GameEngine() {
        this.runtimeProxy = new RuntimeProxy();
        this.mContext = GameActivity.GetInstance();
        this.layaGameEngine = new LayaConch5(mContext);
        this.layaGameEngine.setLocalizable(false);
    }

    @Override
    public void game_plugin_init(int nDownloadThreadNum) {
        // 加载游戏地址
        String gameUrl = mContext.getString(R.string.gameUrl);
        layaGameEngine.setIsPlugin(false);
        layaGameEngine.setGameUrl(gameUrl);
        Log.d(TAG, "GamePluginInit, url = " + gameUrl);

        layaGameEngine.setAlertTitle(mContext.getString(R.string.alert_dialog_title));
        layaGameEngine.setStringOnBackPressed(mContext.getString(R.string.on_back_pressed));
        layaGameEngine.setDownloadThreadNum(nDownloadThreadNum);

        String _path = (String) runtimeProxy.laya_get_value("CacheDir");
        layaGameEngine.setAppCacheDir(_path);
        layaGameEngine.setExpansionZipDir("", "");

        AssetManager am = mContext.getAssets();
        layaGameEngine.setAssetInfo(am);

        layaGameEngine.setLayaEventListener(new layaGameListener((Activity) mContext));
        layaGameEngine.setInterceptKey(true);
        layaGameEngine.onCreate();

        LayaConch5 tmp = (LayaConch5) layaGameEngine;
        Log.d(TAG, "GamePluginInit, soPath = " + tmp.getSoPath());
        Log.d(TAG, "GamePluginInit, jarfile = " + tmp.getJarFile());
        Log.d(TAG, "GamePluginInit, appcache = " + tmp.getAppCacheDir());
    }

    @Override
    public View game_plugin_get_view() {
        return layaGameEngine.getAbsLayout();
    }

    // 以下方法均未在本程序内调用过
    @Override
    public void game_plugin_set_option(String key, String value) {
    }

    @Override
    public void game_plugin_set_runtime_proxy(IPluginRuntimeProxy paramIGameEngineRuntimeProxy) {
    }

    @Override
    public Object game_plugin_get_value(String key) {
        return null;
    }

    @Override
    public boolean game_plugin_intercept_key(int keycode) {
        return false;
    }

    @Override
    public Object game_plugin_invoke_method(String method, Bundle param) {
        return null;
    }

    @Override
    public void game_plugin_onPause() {
        layaGameEngine.onPause();
    }

    @Override
    public void game_plugin_onResume() {
        layaGameEngine.onResume();
    }

    @Override
    public void game_plugin_onStop() {
        layaGameEngine.onStop();
    }

    @Override
    public void game_plugin_onDestory() {
        layaGameEngine.onDestroy();
    }

    @Override
    public void game_plugin_configonChanged(Configuration newConfig) {

    }

    static class layaGameListener implements ILayaEventListener {
        private Activity activity;

        layaGameListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void ExitGame() {
            Log.i("LayaGameListener", "ExitGame");
            activity.finish();
            activity = null;
            System.exit(0);
        }

        @Override
        public void showAssistantTouch(boolean b) {
        }

        @Override
        public void destory() {
        }
    }
}
