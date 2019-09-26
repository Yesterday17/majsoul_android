package cn.yesterday17.majsoul_android.manager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.extension.ExtensionManager;
import cn.yesterday17.majsoul_android.extension.metadata.Metadata;
import cn.yesterday17.majsoul_android.game.GameActivity;
import cn.yesterday17.majsoul_android.extension.ExtensionFileOutputStream;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;

public class ManagerActivity extends FlutterActivity {
    private final String TAG = "ManagerActivity";

    private final int SETTINGS_PREF = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlutterMain.startInitialization(this);
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        initGlobal();

        // 在非打开文件的情况下启用直接进入游戏
        if (!isOpeningFile() && Global.directGame) {
            startGame();
        }

        ExtensionManager.GetInstance().init();
        prepareOpenInstall();

        // TODO: 在这个阶段就加载部分游戏内容 加快游戏启动
        // TODO: 弹出安装窗口

        // 注册与 Flutter 交互的通道
        new MethodChannel(getFlutterView(), Setting.SETTING_CHANNEL).setMethodCallHandler((MethodCall call, MethodChannel.Result result) ->
                Setting.handleSetting(call, result, preferences)
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        prepareOpenInstall();
    }

    void initGlobal() {
        Global.dataDir = getDataDir().toString();
        Global.filesDir = getFilesDir().toString();

        preferences = getPreferences(SETTINGS_PREF);
        Global.gameUrl = preferences.getString("game_url", "https://majsoul.union-game.com/app/web/html/index.html");
        Global.directGame = preferences.getBoolean("direct_game", false);
    }

    void startGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    void prepareOpenInstall() {
        Intent intent = getIntent();
        Log.d(TAG, intent.getAction());
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Log.d(TAG, intent.getDataString());
            String id = "";

            Toast.makeText(this, "导入拓展中...", Toast.LENGTH_SHORT).show();
            try {
                InputStream inputStream = getContentResolver().openInputStream(intent.getData());
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);

                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    String[] parts = entry.getName().split("/");

                    id = parts[0];
                    if (id.equals("!!!")) {
                        // TODO: ID 唯一性检查
                        break;
                    }

                    // 跳过目录创建，目录创建在每次试图建立文件时进行
                    if (entry.isDirectory()) continue;

                    Log.d(TAG, "Unzipping " + entry.getName());
                    ExtensionFileOutputStream out = new ExtensionFileOutputStream(entry.getName());
                    for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                        out.write(c);
                    }
                    zipInputStream.closeEntry();
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "拓展导入失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            String folder = Global.filesDir + File.separator + id;

            // TODO: Metadata 读取
            try {
                Metadata metadata = ExtensionManager.GetInstance().loadMetadata(folder + File.separator + "extension.json");
                Log.d(TAG, "Name: " + metadata.getName());
                Log.d(TAG, "Desc: " + metadata.getDescription());
                ExtensionManager.GetInstance().load(metadata);
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "拓展导入失败！未找到 extension.json！", Toast.LENGTH_SHORT).show();
                return;
            } catch (JsonParseException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "拓展导入失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "拓展" + id + "导入成功！", Toast.LENGTH_SHORT).show();
        }
    }

    boolean isOpeningFile() {
        return Intent.ACTION_VIEW.equals(getIntent().getAction());
    }

    private void handleMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("setting")) {
            Setting.handleSetting(call, result, preferences);
        } else if (call.method.equals("startGame")) {
            this.startGame();
        }

        result.notImplemented();
    }
}
