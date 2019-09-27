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

class _ExtensionManagerState extends State<ExtensionManager>
    with SingleTickerProviderStateMixin {
  int index = 0;
  TabController _controller;

  @override
  void initState() {
    super.initState();

    _controller =
        new TabController(length: 3, initialIndex: index, vsync: this);
    _controller.addListener(() {
      if (!_controller.indexIsChanging) {
        setState(() {
          index = _controller.index;
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: TabBarView(
        controller: _controller,
        children: <Widget>[ExtensionPage(), SettingPage(), AboutPage()],
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: index,
        onTap: (index) {
          setState(() {
            _controller.animateTo(index);
          });
        },
        items: [
          BottomNavigationBarItem(
            icon: Icon(Icons.extension),
            title: Text('扩展'),
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
