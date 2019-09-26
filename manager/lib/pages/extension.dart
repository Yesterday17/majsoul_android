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

  Map<String, Extension> extensions = new Map();

  @override
  void initState() {
    super.initState();

    platform.invokeMapMethod<String, Map>('getList').then((result) {
      print(result);
      setState(() {
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
    if (extensions == null) {
      return Center(
        child: Text('加载拓展列表中'),
      );
    }

    var exts = extensions.values.toList();

    return ListView.builder(
      itemCount: extensions.length,
      itemBuilder: (context, index) {
        var ext = exts[index];
        return InkWell(
          child: Card(
            child: Padding(
              padding: const EdgeInsets.only(top: 8.0, bottom: 8.0),
              child: Column(
                children: <Widget>[
                  Stack(
                    children: <Widget>[
                      ListTile(
                        leading: Checkbox(
                          value: true,
                          onChanged: (value) {},
                        ),
                        title: Text('${ext.name} - ${ext.author}'),
                        subtitle: Text(ext.description),
                      ),
                      Padding(
                        padding: const EdgeInsets.only(right: 16.0),
                        child: Align(
                          alignment: Alignment.topRight,
                          child: Text(exts[index].version),
                        ),
                      )
                    ],
                  )
                ],
              ),
            ),
          ),
          onTap: () {
            // TODO: Enable / Disable
          },
          onLongPress: () {
            // TODO: Detailed page / delete
          },
        );
      },
    );
  }
}
