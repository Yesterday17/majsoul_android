import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:manager/pages/about.dart';
import 'package:manager/pages/extension.dart';
import 'package:manager/pages/setting.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '雀魂+ 模组管理器',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: ExtensionManager(),
    );
  }
}

class ExtensionManager extends StatefulWidget {
  ExtensionManager({Key key}) : super(key: key);

  @override
  _ExtensionManagerState createState() => _ExtensionManagerState();
}

class _ExtensionManagerState extends State<ExtensionManager> {
  int _index = 0;
  final widgets = <Widget>[ExtensionPage(), SettingPage(), AboutPage()];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('模组管理器'),
      ),
      body: widgets[_index],
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _index,
        onTap: (index) {
          setState(() {
            this._index = index;
          });
        },
        items: [
          BottomNavigationBarItem(
            icon: Icon(Icons.extension),
            title: Text('拓展'),
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.settings),
            title: Text('设置'),
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.info_outline),
            title: Text('关于'),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          MethodChannel('cn.yesterday17.majsoul_android/start_game')
              .invokeMethod('');
        },
        tooltip: '启动游戏',
        child: Icon(Icons.play_arrow),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
