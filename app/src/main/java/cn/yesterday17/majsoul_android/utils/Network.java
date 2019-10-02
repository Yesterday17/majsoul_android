package cn.yesterday17.majsoul_android.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.io.InputStream;

import cn.yesterday17.majsoul_android.MajsoulApplication;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Network {
    private static OkHttpClient client = new OkHttpClient();

    public static boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager)
                MajsoulApplication.getInstance()
                        .getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null
                && connManager.getActiveNetworkInfo().isAvailable()
                && connManager.getActiveNetworkInfo().isConnected();
    }

    public static String getString(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static InputStream getInputStream(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().byteStream();
    }
}
