package cn.yesterday17.majsoul_android.game;

import layaair.autoupdateversion.AutoUpdateAPK;
import layaair.game.IMarket.IPlugin;
import layaair.game.IMarket.IPluginRuntimeProxy;
import layaair.game.Market.GameEngine;
import layaair.game.browser.ExportJavaFunction;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class GameActivity extends Activity {
    private static GameActivity instance;

    public static GameActivity GetInstance() {
        return instance;
    }

    public static final int AR_CHECK_UPDATE = 1;
    private IPlugin mPlugin = null;
    private IPluginRuntimeProxy mProxy = null;
    boolean isEngineInitialized = false;
    boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 加载界面
        SplashDialog.GetInstance(this).showSplash();
        checkUpdate(this);

        // 单例
        this.instance = this;
    }

    public void initEngine() {
        this.mProxy = new RuntimeProxy(this);
        this.mPlugin = new GameEngine(this);
        this.mPlugin.game_plugin_set_runtime_proxy(mProxy);
        this.mPlugin.game_plugin_set_option("localize", "false");
        this.mPlugin.game_plugin_set_option("gameUrl", "https://majsoul.union-game.com/app/web/html/index.html");
        this.mPlugin.game_plugin_init(3);
        this.setContentView(mPlugin.game_plugin_get_view());
        isEngineInitialized = true;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null
                && connManager.getActiveNetworkInfo().isAvailable()
                && connManager.getActiveNetworkInfo().isConnected();
    }

    public void checkUpdate(Context context) {
        if (isOnline(context)) {
            // 自动版本更新
            Log.d("0", "[GameActivity] checkUpdate");
            new AutoUpdateAPK(context, (Integer integer) -> {
                Log.d("0", "[GameActivity] checkUpdate - onReceiveValue");

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
            mPlugin.game_plugin_onPause();

        }
    }

    // 从暂停恢复
    protected void onResume() {
        super.onResume();
        if (isEngineInitialized) {
            mPlugin.game_plugin_onResume();
        }
    }

    // 游戏结束
    protected void onDestroy() {
        super.onDestroy();
        if (isEngineInitialized) {
            mPlugin.game_plugin_onDestory();
        }
    }

    public void Restart() {
        // TODO: 提示用户重新启动
        Runtime.getRuntime().exit(0);
    }

    public void SetClipboardText(String text) {
        ClipboardManager manager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("text", text));
    }
}
