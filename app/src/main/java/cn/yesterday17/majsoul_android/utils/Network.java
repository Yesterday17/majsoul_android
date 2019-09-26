package cn.yesterday17.majsoul_android.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.MajsoulApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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

    public static void get(String url) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
                }
            }
        });
    }
}
