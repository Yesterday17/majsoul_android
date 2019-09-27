package cn.yesterday17.majsoul_android.extension.metadata;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import cn.yesterday17.majsoul_android.Global;

public class ExtensionScript {
    private static final String TAG = "ExtensionScript";

    private String path;
    private boolean isRemote;

    private String content;
    private boolean loaded = false;

    public ExtensionScript(String id, String filePath) {
        this.path = filePath;
        this.isRemote = filePath.matches("^https?://");

        if (!isRemote)
            this.path = Global.filesDir + File.separator + id + File.separator + this.path;

        if (this.isRemote) {
            this.readFromRemote();
        } else {
            this.readFromFile();
        }
    }

    private void readFromFile() {
        new Thread(() -> {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(this.path))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    content.append(currentLine).append("\n");
                }
            } catch (IOException e) {
                Log.e("", e.getMessage());
            }
            this.content = content.toString();
            this.loaded = true;
        }).start();
    }

    private void readFromRemote() {
        // TODO:
        this.content = "";
        this.loaded = true;
    }

    public String getScript() {
        while (!this.loaded) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "  try {\n" + this.content + "\n" +
                "  } catch(e) {\n" +
                "    alert(e);\n" +
                "    console.error('Unresolved Error', e);\n" +
                "  }";
    }
}
