import 'package:flutter/material.dart';

class AboutPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('模组管理器'),
      ),
      body: Container(
        child: Center(
          child: Text('关于'),
        ),
      ),
    );
  }
}
