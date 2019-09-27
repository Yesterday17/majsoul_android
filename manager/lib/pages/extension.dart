import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class Extension {
  final String id;
  final String name;
  final String description;
  final String version;
  final String author;

  Extension({this.id, this.name, this.description, this.version, this.author});
}

class ExtensionPage extends StatefulWidget {
  @override
  _ExtensionPageState createState() => _ExtensionPageState();
}

class _ExtensionPageState extends State<ExtensionPage> {
  static const platform =
      const MethodChannel('cn.yesterday17.majsoul_android/extension');

  Map<String, Extension> extensions;

  @override
  void initState() {
    super.initState();

    refreshExtensionMap();
  }

  void refreshExtensionMap() {
    setState(() {
      extensions = null;
    });

    platform.invokeMapMethod<String, Map>('getList').then((result) {
      setState(() {
        extensions = new Map();
        result.forEach((key, value) {
          var map = value.cast<String, String>();
          extensions[key] = Extension(
            id: map['id'],
            name: map['name'],
            description: map['desc'],
            author: map['author'],
            version: map['version'],
          );
        });
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    var exts;
    return Scaffold(
      appBar: AppBar(
        title: Text('模组管理器'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.refresh),
            onPressed: refreshExtensionMap,
          )
        ],
      ),
      body: extensions == null
          ? Center(
              child: Text('加载扩展列表中'),
            )
          : ListView.builder(
              itemCount: extensions.length,
              itemBuilder: (context, index) {
                if (exts == null) exts = extensions.values.toList();
                var ext = exts[index];
                return Card(
                  child: Column(
                    children: <Widget>[
                      Stack(
                        children: <Widget>[
                          ListTile(
                            contentPadding:
                                EdgeInsets.only(top: 8.0, bottom: 8.0),
                            onTap: () {},
                            leading: Checkbox(
                              value: true,
                              onChanged: (value) {},
                            ),
                            title: Text('${ext.name} - ${ext.author}'),
                            subtitle: Text(ext.description),
                          ),
                          Padding(
                            padding:
                                const EdgeInsets.only(top: 8.0, right: 12.0),
                            child: Align(
                              alignment: Alignment.topRight,
                              child: Text(ext.version),
                            ),
                          )
                        ],
                      )
                    ],
                  ),
                );
              },
            ),
    );
  }
}
