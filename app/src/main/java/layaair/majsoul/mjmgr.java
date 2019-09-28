package layaair.majsoul;

import androidx.annotation.Keep;

import cn.yesterday17.majsoul_android.utils.Application;
import cn.yesterday17.majsoul_android.utils.ClipBoard;
import layaair.game.browser.ExportJavaFunction;

@SuppressWarnings("unused")
@Keep
public class mjmgr {
    public void getAppConfig() {
        ExportJavaFunction.CallBackToJS(this, "getAppConfig", "{}");
    }

    public void getSocioCode() {
        ExportJavaFunction.CallBackToJS(this, "getSocioCode", "{\"type\":0,\"code\":\"0\"}");
    }

    public static void clearSocioCode() {
    }

    public static void onAlipay(String arg1) {
        ExportJavaFunction.alert("雀魂+精简了支付模块，如果需要氪金请使用官方客户端/网页！");
    }

    public static void onAlipay_Brower(String arg1) {
        ExportJavaFunction.alert("雀魂+精简了支付模块，如果需要氪金请使用官方客户端/网页！");
    }

    public static void qqLogin() {
        ExportJavaFunction.alert("渠道登录无法支持，敬请谅解！");
    }

    public static void restart() {
        Application.Restart();
    }

    public static void setSysClipboardText(String text) {
        ClipBoard.SetClipboardText(text);
    }

    public static void weiboLogin() {
        ExportJavaFunction.alert("渠道登录无法支持，敬请谅解！");
    }

    public static void wxLogin() {
        ExportJavaFunction.alert("渠道登录无法支持，敬请谅解！");
    }
}
