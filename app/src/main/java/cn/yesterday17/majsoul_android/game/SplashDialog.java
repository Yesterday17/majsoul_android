package cn.yesterday17.majsoul_android.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Keep;

import cn.yesterday17.majsoul_android.R;

@Keep
public class SplashDialog extends Dialog {
    private static String[] tips = new String[4];
    private static int tipIndex;
    private static int loadPercent;
    private static String format = "%1$s (%2$d)%%";

    // Widgets
    private View layout;
    private TextView tipsView;

    private static Handler splashHandler;

    SplashDialog(Context context) {
        super(context, R.style.Theme_Splash);

        tipIndex = 0;
        loadPercent = 0;
        splashHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                splashHandler.removeMessages(0);

                if (loadPercent == 100) {
                    dismissSplash();
                    return;
                }

                tipsView.setText(String.format(format, tips[tipIndex], loadPercent));
                tipIndex = (tipIndex + 1) % tips.length;
                splashHandler.sendEmptyMessageDelayed(0, 1000);
            }
        };
    }

    private void setFontColor(int color) {
        tipsView.setTextColor(color);
    }

    private void setBackgroundColor(int color) {
        layout.setBackgroundColor(color);
    }

    void showSplash() {
        splashHandler.sendEmptyMessage(0);
        this.show();
    }

    private void dismissSplash() {
        dismiss();
        splashHandler = null;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.splash_dialog);

        this.tipsView = findViewById(R.id.tipsView);
        this.layout = findViewById(R.id.layout);

        setBackgroundColor(Color.parseColor("#000000"));
        setFontColor(Color.parseColor("#E8AF71"));
        setTips("初始化中");

        this.setCancelable(false);
    }

    private static void setTips(final String tip) {
        tips[0] = tip;
        tips[1] = tip + "·";
        tips[2] = tip + "··";
        tips[3] = tip + "···";
    }

    // 暴露给 JS 层的函数
    @SuppressWarnings("unused")
    public static void loading(final int percent) {
        loadPercent = Math.min(Math.max(percent, 0), 100);

        // 设置提示文本
        switch (percent) {
            case 10:
                setTips("加载 HTML 中");
                break;
            case 20:
                setTips("加载 version.json 中");
                break;
            case 30:
                setTips("执行 load0 中");
                break;
            case 40:
                setTips("加载 resversion.json 中");
                break;
            case 44:
            case 48:
            case 52:
            case 56:
            case 60:
                setTips("加载字体中");
                break;
            case 70:
                setTips("加载 config.json 中");
                break;
            case 80:
                setTips("加载 liqi.json 中");
                break;
            case 85:
                setTips("执行 app.NetAgent.init 中");
                break;
            case 100:
            default:
                break;
        }
        splashHandler.sendEmptyMessage(0);
    }
}
