package cn.yesterday17.majsoul_android.extension;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.extension.metadata.ExtensionScript;
import cn.yesterday17.majsoul_android.extension.metadata.Metadata;
import cn.yesterday17.majsoul_android.extension.metadata.MetadataDeserializer;
import layaair.game.browser.ExportJavaFunction;

public class ExtensionManager {
    private static String TAG = "ExtensionManager";
    private Map<String, Metadata> allMaps = new HashMap<>();
    private Map<String, List<ExtensionScript>> beforeGame = new HashMap<>();
    private Map<String, List<ExtensionScript>> afterGame = new HashMap<>();

    private Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Metadata.class, new MetadataDeserializer())
            .create();

    private static ExtensionManager instance;
    private boolean loaded = false;

    public static ExtensionManager GetInstance() {
        if (instance == null) {
            instance = new ExtensionManager();
        }
        return instance;
    }

    private void init() {
        if (loaded) return;

        File[] extensions = new File(Global.filesDir).listFiles();
        for (File f : extensions) {
            if (f.isDirectory()) {
                try {
                    Metadata meta = loadMetadata(f.getAbsolutePath() + File.separator + "extension.json");
                    Log.d(TAG, meta.getName());
                    this.load(meta);
                } catch (FileNotFoundException e) {
                }
            }
        }
        loaded = true;
    }

    public void initAsync() {
        initAsync(null);
    }

    public void initAsync(@Nullable Callable callback) {
        new Thread(() -> {
            this.init();
            try {
                if (callback != null) {
                    callback.call();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }).run();
    }

    public Metadata loadMetadata(String file) throws FileNotFoundException, JsonParseException {
        return gson.fromJson(new FileReader(file), Metadata.class);
    }

    public void loadAsync(Metadata metadata) {
        loadAsync(metadata, null);
    }

    public void loadAsync(Metadata metadata, @Nullable Callable callback) {
        new Thread(() -> {
            this.load(metadata);
            try {
                if (callback != null) {
                    callback.call();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }).run();
    }

    private void load(Metadata metadata) {
        // we assert loaded == true here

        this.loaded = false;
        metadata.handleDefaults();
        allMaps.putIfAbsent(metadata.getID(), metadata);

        List<ExtensionScript> scripts = new ArrayList<>();
        metadata.getScripts().forEach((script) -> scripts.add(new ExtensionScript(metadata.getID(), script)));
        (metadata.getLoadBeforeGame() ? beforeGame : afterGame).putIfAbsent(metadata.getID(), scripts);
        this.loaded = true;
    }

    public Map<String, Metadata> getExtensions() {
        // TODO: Return clone
        return allMaps;
    }

    private static String getScripts(Map<String, List<ExtensionScript>> extMap) {
        // TODO: Sort
        StringBuilder builder = new StringBuilder();

        extMap.forEach((id, scripts) ->
                scripts.forEach(data ->
                        builder.append("((context, console, fetchSelf) => {\n")
                                .append(data.getScript())
                                .append("})(\n")
                                .append("  Majsoul_Plus.").append(id).append(",\n")
                                .append("  console,\n")
                                .append("  extensionFetch('").append(id).append("')\n")
                                .append(");")
                )
        );
        return builder.toString();
    }

    @SuppressWarnings("unused")
    public static void getBeforeGameScripts() {
        new Thread(() -> ExportJavaFunction.CallBackToJS(
                ExtensionManager.class,
                "getBeforeGameScripts",
                getScripts(instance.beforeGame)
        )).run();
    }

    @SuppressWarnings("unused")
    public static void getAfterGameScripts() {
        new Thread(() -> ExportJavaFunction.CallBackToJS(
                ExtensionManager.class,
                "getAfterGameScripts",
                getScripts(instance.afterGame)
        )).run();
    }
}
