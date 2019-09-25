package cn.yesterday17.majsoul_android.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import cn.yesterday17.majsoul_android.Global;

public class Network {
    public static boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager) Global.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null
                && connManager.getActiveNetworkInfo().isAvailable()
                && connManager.getActiveNetworkInfo().isConnected();
    }
}
