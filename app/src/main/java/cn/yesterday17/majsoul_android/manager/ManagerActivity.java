package cn.yesterday17.majsoul_android.manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.extension.ExtensionManager;
import cn.yesterday17.majsoul_android.game.GameActivity;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;

public class ManagerActivity extends FlutterActivity {
    private final String TAG = "ManagerActivity";
    static String START_GAME_CHANNEL = "cn.yesterday17.majsoul_android/start_game";

    private final int SETTINGS_PREF = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlutterMain.startInitialization(this);
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        // 初始化全局变量
        initGlobal();

        // 加载扩展管理器
        ExtensionManager.GetInstance().initAsync();

        // TODO: 加载部分游戏内容 加快游戏启动

        // 判断是否直接进入游戏
        if (Global.directGame) {
            startGame();
        }

        // 注册与 Flutter 交互的通道
        initMethodChannels();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.isManagerRunning = false;
    }

    void initGlobal() {
        Global.isManagerRunning = true;

        Global.dataDir = getDataDir().toString();
        Global.filesDir = getFilesDir().toString();

        preferences = getPreferences(SETTINGS_PREF);
        Global.gameUrl = preferences.getString("game_url", "https://majsoul.union-game.com/app/web/html/index.html");
        Global.directGame = preferences.getBoolean("direct_game", false);
    }

    void initMethodChannels() {
        // 修改/读取设置
        new MethodChannel(getFlutterView(), SettingBridge.SETTING_CHANNEL).setMethodCallHandler(
                (MethodCall call, MethodChannel.Result result) ->
                        SettingBridge.handleSetting(call, result, preferences)
        );
        // 管理扩展
        new MethodChannel(getFlutterView(), ExtensionManager.ExtensionBridge.EXTENSION_CHANNEL).setMethodCallHandler(
                (MethodCall call, MethodChannel.Result result) ->
                        ExtensionManager.ExtensionBridge.handleExtension(call, result)
        );
        // 启动游戏
        new MethodChannel(getFlutterView(), START_GAME_CHANNEL).setMethodCallHandler(
                (MethodCall call, MethodChannel.Result result) -> startGame()
        );
    }

    void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
