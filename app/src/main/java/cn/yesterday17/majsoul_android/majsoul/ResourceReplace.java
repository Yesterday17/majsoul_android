package cn.yesterday17.majsoul_android.majsoul;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import cn.yesterday17.majsoul_android.extension.metadata.Metadata;

public class ResourceReplace {
    private static Map<String, String> resReplaceMap = new HashMap<>();

    public static void initReplaceCache(Map<String, Metadata> extensions) {
        resReplaceMap.clear();
        extensions.values().forEach(metadata ->
                metadata.getReplace().forEach(entry ->
                        entry.getFrom().forEach(
                                from -> resReplaceMap.put(
                                        from,
                                        metadata.getID() + "/assets/" + entry.getTo()
                                )
                        )
                )
        );
    }

    static boolean hasKey(String key) {
        return resReplaceMap.containsKey(key);
    }

    @Nullable
    static String getValue(String key) {
        return resReplaceMap.getOrDefault(key, null);
    }
}
