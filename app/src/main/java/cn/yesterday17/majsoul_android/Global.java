package cn.yesterday17.majsoul_android;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Global {

    public static Gson gson = new GsonBuilder().create();

    public static String dataDir;

    public static String filesDir;
    
    public static String gameUrl;

    public static boolean directGame;

    public static boolean isManagerRunning = false;

    public static boolean isGameRunning = false;
}
