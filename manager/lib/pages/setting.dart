import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:manager/widgets/promot_dialog.dart';

class SettingPage extends StatefulWidget {
  @override
  _SettingPageState createState() => _SettingPageState();
}

class _SettingPageState extends State<SettingPage> {
  static const platform =
      const MethodChannel('cn.yesterday17.majsoul_android/setting');

  bool directGame = false;
  bool showAssistant = false;
  String gameUrl = '';

  Future<void> _getSettings() async {
    await Future.wait([
      platform
          .invokeMethod('get', {'key': 'gameUrl'}).then((url) => gameUrl = url),
      platform.invokeMethod(
          'get', {'key': 'directGame'}).then((direct) => directGame = direct),
      platform.invokeMethod(
          'get', {'key': 'showAssistant'}).then((show) => showAssistant = show)
    ]);
    try {
      setState(() {});
    } catch (e) {}
  }

  @override
  void initState() {
    super.initState();

    _getSettings();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('模组管理器'),
      ),
      body: Container(
        child: ListView(
          children: <Widget>[
            SwitchListTile(
              title: Text('直接进入游戏'),
              subtitle: Text('但会有一个悬浮窗。'),
              value: directGame,
              onChanged: (value) {
                setState(() {
                  directGame = value;
                  platform.invokeMethod(
                      'set', {'key': 'directGame', 'value': directGame});
                });
              },
            ),
            SwitchListTile(
              title: Text('强制显示悬浮窗'),
              subtitle: Text('就算是从管理器启动的游戏也显示悬浮窗。'),
              value: showAssistant,
              onChanged: (value) {
                setState(() {
                  showAssistant = value;
                  platform.invokeMethod(
                      'set', {'key': 'showAssistant', 'value': showAssistant});
                });
              },
            ),
            ListTile(
                title: Text('修改游戏地址'),
                subtitle: Text('不建议手动修改'),
                onTap: () {
                  showDialog(
                    builder: (context) => PromotDialog(
                      title: '修改游戏地址',
                      initialValue: gameUrl,
                      onSubmit: (text) {
                        gameUrl = text;
                        platform.invokeMethod(
                            'set', {'key': 'gameUrl', 'value': gameUrl});
                      },
                    ),
                    context: context,
                  );
                }),
          ],
        ),
      ),
    );
  }
}
