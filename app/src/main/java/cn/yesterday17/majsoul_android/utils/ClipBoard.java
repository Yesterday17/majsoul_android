package cn.yesterday17.majsoul_android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import cn.yesterday17.majsoul_android.game.GameActivity;

public class ClipBoard {
    public static void SetClipboardText(String text) {
        ClipboardManager manager = (ClipboardManager) GameActivity.GetInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("text", text));
    }
}
