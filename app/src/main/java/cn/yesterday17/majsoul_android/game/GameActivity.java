package cn.yesterday17.majsoul_android.game;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.R;
import layaair.game.conch.ILayaEventListener;
import layaair.game.conch.LayaConch5;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {
    private static String TAG = "GameActivity";
    private static String TAG_ENGINE = "LayaConchEngine";

    // 面向接口，嗯，面向接口（逃
    private LayaConch5 GameEngine;

    // 判断 Engine 是否加载完成
    boolean isEngineInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Global.isGameRunning = true;

        // 设置全屏
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 加载界面
        new SplashDialog(this).showSplash();
        initEngine();
    }

    public void initEngine() {
        this.GameEngine = new LayaConch5(this);
        GameEngine.setIsPlugin(false);

        // 加载游戏地址
        String gameUrl = Global.gameUrl;
        GameEngine.setGameUrl(gameUrl);
        Log.d(TAG_ENGINE, "url = " + gameUrl);

        GameEngine.setAlertTitle(getString(R.string.alert_dialog_title));
        GameEngine.setStringOnBackPressed(getString(R.string.on_back_pressed));

        GameEngine.setAppCacheDir(getCacheDirString());

        GameEngine.setAssetInfo(getAssets());

        GameEngine.setLayaEventListener(new GameListener());
        GameEngine.setInterceptKey(true);
        GameEngine.onCreate();

        Log.d(TAG_ENGINE, "soPath = " + GameEngine.getSoPath());
        Log.d(TAG_ENGINE, "jarFile = " + GameEngine.getJarFile());
        Log.d(TAG_ENGINE, "appCacheDir = " + GameEngine.getAppCacheDir());

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
        Global.isGameRunning = false;

        if (isEngineInitialized) {
            GameEngine.onDestroy();
        }
    }

    class GameListener implements ILayaEventListener {
        private static final String TAG_LISTENER = "GameListener";

        @Override
        public void ExitGame() {
            Log.i(TAG_LISTENER, "ExitGame");
            GameActivity.this.finish();
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
