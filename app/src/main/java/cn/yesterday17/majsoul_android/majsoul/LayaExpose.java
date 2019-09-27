package cn.yesterday17.majsoul_android.majsoul;

import layaair.game.browser.ExportJavaFunction;

import static cn.yesterday17.majsoul_android.utils.PlatformClassUtils.genPlatformResponse;

@SuppressWarnings("unused")
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

    public static void code() {
        CodeJS.loadCodeJS(() -> {
            ExportJavaFunction.CallBackToJS(LayaExpose.class, "code",
                    genPlatformResponse(CodeJS.error, CodeJS.codeJS));
            return null;
        });
    }
}
