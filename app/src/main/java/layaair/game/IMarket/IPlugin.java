package layaair.game.IMarket;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

public interface IPlugin {	
	//	获取游戏引擎端的变量
	Object game_plugin_get_value(String key);
	//	获取要显示的view
	View game_plugin_get_view();
	//	游戏引擎初始化
	void game_plugin_init(int nDownloadThreadNum);
	//	拦截keycode
	boolean game_plugin_intercept_key(int keycode);
	//	调用游戏引擎的方法
	Object game_plugin_invoke_method(String method, Bundle param);
	//	进入后台时调用
	void game_plugin_onPause();
	//	恢复前台时调用
	void game_plugin_onResume();
	//	退出游戏时调用
	void game_plugin_onStop();
	//	退出游戏时 销毁
	void game_plugin_onDestory();
	//	向游戏引擎传递参数
	void game_plugin_set_option(String key, String value);
    //  设置代理对象
    void game_plugin_set_runtime_proxy(IPluginRuntimeProxy paramIGameEngineRuntimeProxy);
	//	游戏页面发生改变时触发(分辨率，横竖屏...)
	void game_plugin_configonChanged(Configuration newConfig);
}
