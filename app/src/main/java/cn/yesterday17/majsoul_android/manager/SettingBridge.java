package cn.yesterday17.majsoul_android.manager;

import android.content.SharedPreferences;

import cn.yesterday17.majsoul_android.Global;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

class SettingBridge {
    static String SETTING_CHANNEL = "cn.yesterday17.majsoul_android/setting";

    static void handleSetting(MethodCall call, MethodChannel.Result result, SharedPreferences preferences) {
        if (call.hasArgument("key")) {
            String key = call.argument("key");

            if (call.method.equals("get")) {
                Object ret = handleSettingGet(key);

                if (ret != null) {
                    result.success(ret);
                    return;
                }
            } else if (call.method.equals(("set"))) {
                result.success(handleSettingSet(key, call.argument("value"), preferences));
                return;
            }
        }

        result.notImplemented();
    }

    private static Object handleSettingGet(String key) {
        if (key.equals("gameUrl")) {
            return Global.gameUrl;
        } else if (key.equals("directGame")) {
            return Global.directGame;
        } else if (key.equals("showAssistant")) {
            return Global.showAssistant;
        } else {
            return null;
        }
    }

    private static boolean handleSettingSet(String key, Object value, SharedPreferences preferences) {
        if (key.equals("gameUrl")) {
            Global.gameUrl = (String) value;
            preferences.edit().putString("game_url", Global.gameUrl).apply();
            return true;
        } else if (key.equals("directGame")) {
            Global.directGame = (Boolean) value;
            preferences.edit().putBoolean("direct_game", Global.directGame).apply();
            return true;
        } else if (key.equals("showAssistant")) {
            Global.showAssistant = (Boolean) value;
            preferences.edit().putBoolean("show_assistant", Global.showAssistant).apply();
            return true;
        } else {
            return false;
        }
    }
}
