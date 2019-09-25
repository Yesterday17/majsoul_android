package cn.yesterday17.majsoul_android.game;

import cn.yesterday17.majsoul_android.R;
import layaair.game.conch.ILayaEventListener;
import layaair.game.conch.LayaConch5;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class GameActivity extends AppCompatActivity {
    private static String TAG = "GameActivity";
    private static String TAG_ENGINE = "LayaConchEngine";

    // 单例
    private static GameActivity instance = null;

    public static GameActivity GetInstance() {
        return instance;
    }

    // 面向接口，嗯，面向接口（逃
    private LayaConch5 GameEngine;

    // 判断 Engine 是否加载完成
    boolean isEngineInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 单例
        this.instance = this;

        // 设置全屏
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 加载界面
        SplashDialog.GetInstance(this).showSplash();
        initEngine();
    }

    public void initEngine() {
        this.GameEngine = new LayaConch5(this);
        GameEngine.setIsPlugin(false);

        // 加载游戏地址
        String gameUrl = getString(R.string.gameUrl);
        GameEngine.setGameUrl(gameUrl);
        Log.d(TAG_ENGINE, "GamePluginInit, url = " + gameUrl);

        GameEngine.setAlertTitle(getString(R.string.alert_dialog_title));
        GameEngine.setStringOnBackPressed(getString(R.string.on_back_pressed));

        GameEngine.setAppCacheDir(getCacheDirString());

        GameEngine.setAssetInfo(getAssets());

        GameEngine.setLayaEventListener(new layaGameListener());
        GameEngine.setInterceptKey(true);
        GameEngine.onCreate();

        Log.d(TAG_ENGINE, "GamePluginInit, soPath = " + GameEngine.getSoPath());
        Log.d(TAG_ENGINE, "GamePluginInit, jarFile = " + GameEngine.getJarFile());
        Log.d(TAG_ENGINE, "GamePluginInit, appCacheDir = " + GameEngine.getAppCacheDir());

        this.setContentView(GameEngine.getAbsLayout());
        isEngineInitialized = true;
    }

    public String getCacheDirString() {
        String sCache = getCacheDir().toString();
        String[] vString = sCache.split("/");
        StringBuilder cache = new StringBuilder(50);
        for (int i = 0; i < vString.length - 1; i++) {
            cache.append(vString[i]).append("/");
        }
        return cache.toString();
    }

    // 处理暂停逻辑，对分屏进行特别优化
    protected void onPause() {
        super.onPause();
        if (isEngineInitialized && !this.isInMultiWindowMode()) {
            GameEngine.onPause();
        }
    }

    // 从暂停恢复
    protected void onResume() {
        super.onResume();
        if (isEngineInitialized) {
            GameEngine.onResume();
        }
    }

    // 游戏结束
    protected void onDestroy() {
        super.onDestroy();
        if (isEngineInitialized) {
            GameEngine.onDestroy();
        }
    }

    @Override
    public void finish() {
        super.finish();
        instance = null;
    }

    static class layaGameListener implements ILayaEventListener {
        @Override
        public void ExitGame() {
            Log.i("LayaGameListener", "ExitGame");
            GameActivity.GetInstance().finish();
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
