package cn.yesterday17.majsoul_android;

import android.app.Application;

public class MajsoulApplication extends Application {
    private static MajsoulApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MajsoulApplication getInstance() {
        return instance;
    }
}
