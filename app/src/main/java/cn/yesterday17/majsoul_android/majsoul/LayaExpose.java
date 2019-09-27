package cn.yesterday17.majsoul_android.majsoul;

import androidx.annotation.Keep;

import cn.yesterday17.majsoul_android.Global;
import layaair.game.browser.ExportJavaFunction;

import static cn.yesterday17.majsoul_android.utils.PlatformClassUtils.genPlatformResponse;

@SuppressWarnings("unused")
@Keep
public class LayaExpose {
    public static void version() {
        GameVersion.loadVersion(() -> {
            ExportJavaFunction.CallBackToJS(LayaExpose.class, "version",
                    genPlatformResponse(GameVersion.error, GameVersion.gameVersion));
            return null;
        });
    }

    public static void resVersion() {
        ResourceVersion.loadResourceVersion(() -> {
            ExportJavaFunction.CallBackToJS(LayaExpose.class, "resVersion",
                    genPlatformResponse(ResourceVersion.error, ResourceVersion.resVersion));
            return null;
        });
    }

    public static void waitForResVersion() {
        ResourceVersion.loadResourceVersion(() -> {
            ExportJavaFunction.CallBackToJS(LayaExpose.class, "resVersion",
                    genPlatformResponse(ResourceVersion.error, ""));
            return null;
        });
    }

    public static void code() {
        CodeJS.loadCodeJS(() -> {
            ExportJavaFunction.CallBackToJS(LayaExpose.class, "code",
                    genPlatformResponse(CodeJS.error, CodeJS.codeJS));
            return null;
        });
    }

    public static String resourceReplace(final String key) {
        String result = "";
        if (ResourceReplace.hasKey(key)) {
            result = "file://" + Global.filesDir + "/" + ResourceReplace.getValue(key);
        }
        return result;
        // ExportJavaFunction.CallBackToJS(LayaExpose.class, "resourceReplace", result);
    }
}
