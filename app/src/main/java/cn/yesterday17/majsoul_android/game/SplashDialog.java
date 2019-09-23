package cn.yesterday17.majsoul_android.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import cn.yesterday17.majsoul_android.R;


public class SplashDialog extends Dialog {
    private long splashShowTime;
    private long minShowTime = 2;

    private String[] tips = {};
    private int tipIndex = 0;
    private int loadPercent = 0;

    // Widgets
    private View layout;
    private TextView tipsView;

    // Instance
    private static SplashDialog instance;

    @NonNull
    public static SplashDialog GetInstance(Context context) {
        if (instance == null) {
            instance = new SplashDialog(context);
        }
        return instance;
    }

    @Nullable
    public static SplashDialog GetInstance() {
        return instance;
    }

    Handler splashHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    splashHandler.removeMessages(0);
                    if (tips.length > 0) {
                        tipsView.setText(tips[tipIndex] + "(" + loadPercent + "%)");
                        tipIndex = (tipIndex + 1) % tips.length;
                    }
                    splashHandler.sendEmptyMessageDelayed(0, 1000);
                    break;
                case 1:
                    splashHandler.removeMessages(0);
                    splashHandler.removeMessages(1);
                    SplashDialog.this.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private SplashDialog(Context context) {
        super(context, R.style.Splash);
    }

    public void setTips(String[] tips) {
        this.tips = tips;
    }

    public void setPercent(int percent) {
        if (percent == 100) {
            dismissSplash();
        }

        percent = Math.max(percent, 0);
        percent = Math.min(percent, 100);
        this.loadPercent = percent;

        if (tips.length > 0) {
            if (tipIndex >= tips.length) {
                tipIndex = 0;
            }
            tipsView.setText(tips[tipIndex] + "(" + this.loadPercent + "%)");
        }
    }

    public void setFontColor(int color) {
        tipsView.setTextColor(color);
    }

    public void setBackgroundColor(int color) {
        layout.setBackgroundColor(color);
    }

    public void showSplash() {
        this.show();
        splashShowTime = System.currentTimeMillis();
        splashHandler.sendEmptyMessage(0);
    }

    public void dismissSplash() {
        long showTime = System.currentTimeMillis() - splashShowTime;
        if (showTime >= minShowTime * 1000) {
            splashHandler.sendEmptyMessage(1);
        } else {
            splashHandler.sendEmptyMessageDelayed(1, this.minShowTime * 1000 - showTime);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.splash_dialog);

        this.tipsView = findViewById(R.id.tipsView);
        this.layout = findViewById(R.id.layout);

        this.setCancelable(false);
    }

    // 暴露给 JS 层的函数
    public static void loading(final int percent) {
        SplashDialog.GetInstance().setPercent(percent);
    }

    public static void showTextInfo(boolean show) {
        SplashDialog.GetInstance().tipsView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public static void bgColor(final String color) {
        SplashDialog.GetInstance().setBackgroundColor(Color.parseColor(color));
    }

    public static void hideSplash() {
        SplashDialog.GetInstance().dismissSplash();
    }

    public static void setFontColor(final String color) {
        SplashDialog.GetInstance().setFontColor(Color.parseColor(color));
    }

    public static void setTips(final JSONArray tips) {
        try {
            String[] tipsArray = new String[tips.length()];
            for (int i = 0; i < tips.length(); i++) {
                tipsArray[i] = tips.getString(i);
            }
            SplashDialog.GetInstance().setTips(tipsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
