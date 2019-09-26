import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class ExtensionPage extends StatefulWidget {
  @override
  _ExtensionPageState createState() => _ExtensionPageState();
}

class _ExtensionPageState extends State<ExtensionPage> {
  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemBuilder: (context, index) {
        return ListTile(
          title: Text(index.toString()),
        );
      },
    );
  }
}
