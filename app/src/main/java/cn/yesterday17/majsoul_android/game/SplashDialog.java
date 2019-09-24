package cn.yesterday17.majsoul_android.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import cn.yesterday17.majsoul_android.R;


public class SplashDialog extends Dialog {
    private long splashShowTime;
    private long minShowTime = 2;

    private String[] tips = {"初始化中", "初始化中·", "初始化中··", "初始化中···"};
    private int tipIndex = 0;
    private int loadPercent = 0;

    // Widgets
    private View layout;
    private TextView tipsView;

    // Instance
    private static SplashDialog instance;

    @NonNull
    static SplashDialog GetInstance(Context context) {
        if (instance == null) {
            instance = new SplashDialog(context);
        }
        return instance;
    }

    private static SplashDialog GetInstance() {
        return instance;
    }

    private Handler splashHandler = new Handler(Looper.getMainLooper()) {
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
                    instance = null;
                    break;
                default:
                    break;
            }
        }
    };

    private SplashDialog(Context context) {
        super(context, R.style.Theme_Splash);
    }

    private void setTips(String[] tips) {
        this.tips = tips;
    }

    private void setPercent(int percent) {
        if (percent == 100) {
            dismissSplash();
        }

        percent = Math.max(percent, 0);
        percent = Math.min(percent, 100);
        this.loadPercent = percent;

        if (tips.length > 0) {
            splashHandler.sendEmptyMessage(0);
        }
    }

    private void setFontColor(int color) {
        tipsView.setTextColor(color);
    }

    private void setBackgroundColor(int color) {
        layout.setBackgroundColor(color);
    }

    void showSplash() {
        splashShowTime = System.currentTimeMillis();
        splashHandler.sendEmptyMessage(0);
        this.show();
    }

    private void dismissSplash() {
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

        this.setBackgroundColor(Color.parseColor("#000000"));
        this.setFontColor(Color.parseColor("#E8AF71"));

        this.setCancelable(false);
    }

    // 暴露给 JS 层的函数
    public static void loading(final int percent) {
        SplashDialog.GetInstance().setPercent(percent);
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
