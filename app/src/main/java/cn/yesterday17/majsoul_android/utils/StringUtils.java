package cn.yesterday17.majsoul_android.utils;

import java.util.List;

public class StringUtils {
    public static String Join(String[] arr, String sep) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length - 1; i++) {
            builder.append(arr[i]).append(sep);
        }
        return builder.append(arr[arr.length - 1]).toString();
    }

    public static String Join(String[] arr) {
        return Join(arr, "");
    }

    public static String Join(List<String> arr, String sep) {
        return Join(arr.toArray(new String[0]), sep);
    }

    public static String Join(List<String> arr) {
        return Join(arr, "");
    }


}
