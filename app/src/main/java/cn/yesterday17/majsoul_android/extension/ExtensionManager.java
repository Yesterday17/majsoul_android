package cn.yesterday17.majsoul_android.extension;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

import cn.yesterday17.majsoul_android.Constants;
import cn.yesterday17.majsoul_android.Global;
import cn.yesterday17.majsoul_android.extension.metadata.ExtensionScript;
import cn.yesterday17.majsoul_android.extension.metadata.Metadata;
import cn.yesterday17.majsoul_android.extension.metadata.MetadataDeserializer;
import cn.yesterday17.majsoul_android.utils.FileSystem;
import cn.yesterday17.majsoul_android.utils.PlatformClassUtils;
import cn.yesterday17.majsoul_android.utils.StringUtils;
import io.flutter.plugin.common.MethodCall;
import layaair.game.browser.ExportJavaFunction;

public class ExtensionManager {
    private static String TAG = "ExtensionManager";
    private Map<String, Metadata> allMaps = new HashMap<>();
    private Map<String, List<ExtensionScript>> beforeGame = new HashMap<>();
    private Map<String, List<ExtensionScript>> afterGame = new HashMap<>();

    private BlockingQueue<File> loadQueue = new LinkedBlockingQueue<>();
    private List<Thread> loadThreadList = new LinkedList<>();

    private static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(Metadata.class, new MetadataDeserializer())
            .create();

    private static ExtensionManager instance;

    public static ExtensionManager GetInstance() {
        if (instance == null) {
            instance = new ExtensionManager();
        }
        return instance;
    }

    private ExtensionManager() {
        // 将现有的所有扩展加入加载队列
        File[] extensions = new File(Global.filesDir).listFiles();
        for (File f : extensions) {
            if (f.isDirectory()) {
                loadExtension(f);
            }
        }
        // 默认只有一个加载线程
        addLoadThread();
    }

    /**
     * 等待扩展管理器完成加载后调用回调函数
     *
     * @param callback 回调函数
     */
    public void waitForLoaded(@NonNull Callable<Void> callback) {
        new Thread(() -> {
            try {
                while (!loadQueue.isEmpty()) {
                    Thread.sleep(100);
                }
                callback.call();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }).start();
    }

    /**
     * 加载目录对应的扩展
     *
     * @param folder 扩展目录
     */
    public void loadExtension(File folder) {
        try {
            loadQueue.put(folder);
        } catch (InterruptedException e) {
            Log.e(TAG, folder.getAbsolutePath());
        }
    }

    /**
     * 通过 Metadata 进行脚本的加载
     * <b>必须</b>运行在新的线程中
     *
     * @param metadata 扩展的 Metadata
     */
    private void load(@NonNull Metadata metadata) {
        metadata.handleDefaults();
        allMaps.putIfAbsent(metadata.getID(), metadata);

        List<ExtensionScript> scripts = new ArrayList<>();
        metadata.getScripts().forEach((script) -> scripts.add(new ExtensionScript(metadata.getID(), script)));
        (metadata.getLoadBeforeGame() ? beforeGame : afterGame).putIfAbsent(metadata.getID(), scripts);
    }

    /**
     * 加载扩展的 Metadata
     * 只应该在加载线程中运行
     *
     * @param folder 目录名 也就是扩展名
     * @return 扩展解析完成的 Metadata
     * @throws FileNotFoundException 文件不存在
     * @throws JsonParseException    解析出错
     */
    public static Metadata loadMetadata(String folder) throws FileNotFoundException, JsonParseException {
        return gson.fromJson(new FileReader(folder + File.separator + Constants.EXTENSION_METADATA_FILENAME), Metadata.class);
    }

    public void removeExtension(String id) {
        if (id == null) return;
        if (!allMaps.containsKey(id)) return;

        allMaps.remove(id);
        beforeGame.remove(id);
        afterGame.remove(id);
        FileSystem.rmRF(Global.filesDir + File.separator + id);
    }

    /**
     * 获得当前加载的所有扩展
     * TODO: 返回当前 allMap 的 clone
     *
     * @return 当前加载的所有扩展
     */
    public Map<String, Metadata> getExtensions() {
        return allMaps;
    }

    /**
     * 新增加一个加载线程
     */
    private void addLoadThread() {
        Thread th = new Thread(() -> {
            File f;
            try {
                while ((f = loadQueue.take()) != null) {
                    Metadata metadata = loadMetadata(f.getAbsolutePath());
                    Log.d(TAG, metadata.getName());

                    load(metadata);
                }
            } catch (InterruptedException | FileNotFoundException | JsonParseException e) {
                Log.e(TAG, e.getMessage());
            }
        });
        th.setDaemon(true);
        th.start();
        loadThreadList.add(th);
    }

    /**
     * 获得对应 extMap 的所有脚本
     * <b>必须</b> 在新线程中运行
     *
     * @param extMap 对应 Map
     * @return 所有脚本构成的字符串
     */
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
                PlatformClassUtils.genPlatformResponse(null, getScripts(instance.beforeGame))
        )).start();
    }

    @SuppressWarnings("unused")
    public static void getAfterGameScripts() {
        new Thread(() -> ExportJavaFunction.CallBackToJS(
                ExtensionManager.class,
                "getAfterGameScripts",
                PlatformClassUtils.genPlatformResponse(null, getScripts(instance.afterGame))
        )).start();
    }

    public static class ExtensionBridge {
        public static String EXTENSION_CHANNEL = "cn.yesterday17.majsoul_android/extension";

        public static void handleExtension(MethodCall call, Function<Object, Void> callback) {
            if (call.method.equals("getList")) {
                ExtensionManager.GetInstance().waitForLoaded(() -> {
                    callback.apply(genExtensionDataMap());
                    return null;
                });
            } else if (call.method.equals("delete") && call.hasArgument("id")) {
                ExtensionManager.GetInstance().waitForLoaded(() -> {
                    ExtensionManager.GetInstance().removeExtension(call.argument("id"));
                    callback.apply(true);
                    return null;
                });
            } else {
                callback.apply(null);
            }
        }

        private static Map<String, Map<String, String>> genExtensionDataMap() {
            Map<String, Map<String, String>> result = new HashMap<>();
            ExtensionManager.GetInstance().getExtensions().values().forEach((metadata) -> {
                Map<String, String> map = new HashMap<>();
                map.put("id", metadata.getID());
                map.put("name", metadata.getName());
                map.put("desc", metadata.getDescription());
                map.put("version", metadata.getVersion());
                map.put("author", StringUtils.Join(metadata.getAuthors(), ","));
                result.put(metadata.getID(), map);
            });
            return result;
        }
    }
}
