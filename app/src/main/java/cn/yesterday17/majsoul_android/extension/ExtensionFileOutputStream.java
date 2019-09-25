package cn.yesterday17.majsoul_android.extension;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.yesterday17.majsoul_android.Global;

public class ExtensionFileOutputStream {
    private String path;
    private FileOutputStream stream;

    public ExtensionFileOutputStream(String path) throws FileNotFoundException {
        this.path = Global.filesDir + File.separator + path;
        File t = new File(this.path.replaceAll("[^/]+\\.[^/]+$", ""));
        if (!t.exists()) {
            t.mkdirs();
        }

        stream = new FileOutputStream(this.path);
    }

    public void write(int b) throws IOException {
        stream.write(b);
    }

    public void close() throws IOException {
        stream.close();
    }

}
