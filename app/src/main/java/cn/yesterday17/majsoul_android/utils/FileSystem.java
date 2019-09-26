package cn.yesterday17.majsoul_android.utils;

import java.io.File;

public class FileSystem {
    public static boolean isIDUnique(String folder, String id) {
        File fo = new File(folder);
        for (File f : fo.listFiles()) {
            if (f.isDirectory() && f.getName().equals(id)) {
                return false;
            }
        }
        return true;
    }
}
