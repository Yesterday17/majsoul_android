package cn.yesterday17.majsoul_android.extension.install;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

import cn.yesterday17.majsoul_android.utils.Network;

public class InstallMajsoulXStoreActivity extends InstallActivity {
    private static final String TAG = "InstallMajsoulXStoreActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    boolean preInstall(Intent intent) {
        Uri data = intent.getData();
        return super.preInstall(intent)
                && data.getQueryParameter("url") != null
                && data.getQueryParameter("code") != null
                && data.getQueryParameter("accountId") != null
                && data.getQueryParameter("projectId") != null;
    }

    @Override
    InputStream genInputStream(Intent intent) {
        Uri data = intent.getData();
        String url = data.getQueryParameter("url")
                + "&code=" + data.getQueryParameter("code")
                + "&accountId=" + data.getQueryParameter("accountId")
                + "&projectId=" + data.getQueryParameter("projectId");
        Log.d(TAG, url);

        try {
            return Network.getInputStream(url);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
}
