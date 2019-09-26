package cn.yesterday17.majsoul_android.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.MajsoulApplication;

public class ClipBoard {
    public static void SetClipboardText(String text) {
        ClipboardManager manager =
                (ClipboardManager) MajsoulApplication.getInstance()
                        .getApplicationContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("text", text));
    }
}
