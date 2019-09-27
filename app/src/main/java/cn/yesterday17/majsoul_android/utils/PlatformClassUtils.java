package cn.yesterday17.majsoul_android.utils;

import static cn.yesterday17.majsoul_android.Global.gson;

public class PlatformClassUtils {
    public static String genPlatformResponse(PlatformResponse response) {
        return gson.toJson(response);
    }

    public static String genPlatformResponse(String err, Object data) {
        return genPlatformResponse(new PlatformResponse(err, data));
    }

    static class PlatformResponse {
        private String err;
        private Object data;

        PlatformResponse(String err, Object data) {
            this.err = err;
            this.data = data;
        }
    }
}
