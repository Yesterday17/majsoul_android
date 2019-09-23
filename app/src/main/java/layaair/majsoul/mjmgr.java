package layaair.majsoul;

import cn.yesterday17.majsoul_android.game.GameActivity;
import layaair.game.browser.ExportJavaFunction;

public class mjmgr {
    public static String sociocode = "0";
    public static int sociotype;

    public static void clearSocioCode() {
        mjmgr.sociotype = 0;
        mjmgr.sociocode = "0";
    }

    public void getAppConfig() {
        ExportJavaFunction.CallBackToJS(this, "getAppConfig", "{}");
    }

    public void getSocioCode() {
        ExportJavaFunction.CallBackToJS(this, "getSocioCode", "{\"type\":" + mjmgr.sociotype + ",\"code\":\"" + mjmgr.sociocode + "\"}");
    }

    public static void onAlipay(String arg1) {
        ExportJavaFunction.alert("雀魂-改精简了支付模块以确保作者没有任何可能性从用户处获取利益，如果需要氪金请使用官方客户端/网页！");
    }

    public static void onAlipay_Brower(String arg1) {
        ExportJavaFunction.alert("雀魂-改精简了支付模块以确保作者没有任何可能性从用户处获取利益，如果需要氪金请使用官方客户端/网页！");
    }

    public static void qqLogin() {
        ExportJavaFunction.alert("渠道登录无法支持，敬请谅解！");
    }

    public static void restart() {
        GameActivity.GetInstance().Restart();
    }

    public static void setSysClipboardText(String text) {
        GameActivity.GetInstance().SetClipboardText(text);
    }

    public static void weiboLogin() {
        ExportJavaFunction.alert("渠道登录无法支持，敬请谅解！");
    }

    public static void wxLogin() {
        ExportJavaFunction.alert("渠道登录无法支持，敬请谅解！");
    }
}
