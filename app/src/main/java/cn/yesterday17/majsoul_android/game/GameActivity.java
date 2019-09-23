package cn.yesterday17.majsoul_android.game;

import cn.yesterday17.majsoul_android.utils.Network;
import layaair.autoupdateversion.AutoUpdateAPK;
import layaair.game.IMarket.IPlugin;
import layaair.game.Market.GameEngine;
import layaair.game.browser.ExportJavaFunction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends Activity {
    private static String TAG = "GameActivity";
    private static GameActivity instance = null;

    @Nullable
    public static GameActivity GetInstance() {
        return instance;
    }

    public static final int AR_CHECK_UPDATE = 1;
    private IPlugin gameEngine = null;
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
        checkUpdate(this);
    }

    public void initEngine() {
        this.gameEngine = new GameEngine();
        this.gameEngine.game_plugin_init(3);
        this.setContentView(gameEngine.game_plugin_get_view());
        isEngineInitialized = true;
    }

    public void checkUpdate(Context context) {
        if (Network.isOnline()) {
            // 自动版本更新
            Log.d(TAG, "checkUpdate");
            new AutoUpdateAPK(context, (Integer integer) -> {
                Log.d(TAG, "checkUpdate - onReceiveValue");

                // 不论结果如何都启动游戏
                // TODO: 根据紧急等级决定是否阻止进入游戏
                initEngine();
            });
        } else {
            ExportJavaFunction.alert("网络连接失败，请稍后再试！");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == AR_CHECK_UPDATE) {
            checkUpdate(this);
        }
    }

    // 处理暂停逻辑，对分屏进行特别优化
    protected void onPause() {
        super.onPause();
        if (isEngineInitialized && !this.isInMultiWindowMode()) {
            gameEngine.game_plugin_onPause();

        }
    }

    // 从暂停恢复
    protected void onResume() {
        super.onResume();
        if (isEngineInitialized) {
            gameEngine.game_plugin_onResume();
        }
    }

    // 游戏结束
    protected void onDestroy() {
        super.onDestroy();
        if (isEngineInitialized) {
            gameEngine.game_plugin_onDestory();
        }
    }
}
