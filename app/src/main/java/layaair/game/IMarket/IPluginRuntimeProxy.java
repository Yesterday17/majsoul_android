package layaair.game.IMarket;

import org.json.JSONObject;

import android.os.Bundle;
import android.webkit.ValueCallback;

public interface IPluginRuntimeProxy {
    //设置插件管理器key对应的值
    boolean laya_set_value(String key, Object value);

    //获取插件管理器key对应的值
    Object laya_get_value(String key);

    //游戏引擎请求终止游戏
    void laya_stop_game_engine();

    //调用方法
    Object laya_invoke_Method(String method, Bundle param);

    //登录
    void Login(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //注销登录
    void Logout(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //支付
    void Pay(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //发送桌面快捷方式
    void PushIcon(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //分享
    void Share(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //打开BBS
    void OpenBBS(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //获取好友列表
    void GetFriendsList(JSONObject jsonObj, ValueCallback<JSONObject> callback);

    //给host发送消息
    void SendMessageToPlatform(JSONObject jsonObj, ValueCallback<JSONObject> callback);
}
