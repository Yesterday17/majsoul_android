package cn.yesterday17.majsoul_android.extension;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExtensionFileOutputStream {
    private String path;
    private FileOutputStream _stream;
    private BufferedOutputStream stream;

    public ExtensionFileOutputStream(String dir, String path) throws FileNotFoundException {
        this.path = dir + File.separator + path;
        File t = new File(this.path.replaceAll("[^/]+\\.[^/]+$", ""));
        if (!t.exists()) {
            t.mkdirs();
        }

        _stream = new FileOutputStream(this.path);
        stream = new BufferedOutputStream(_stream);
    }

    public void write(byte[] buffer, int read) throws IOException {
        stream.write(buffer, 0, read);
    }

    public void close() throws IOException {
        stream.close();
        _stream.close();
    }

}
