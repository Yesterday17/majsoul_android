import 'package:flutter/material.dart';

class PromotDialog extends StatefulWidget {
  final String title;
  final String initialValue;
  final ValueChanged<String> onSubmit;

  PromotDialog({this.title, this.initialValue = '', this.onSubmit})
      : assert(title != '');

  @override
  _PromotDialogState createState() => _PromotDialogState();
}

class _PromotDialogState extends State<PromotDialog> {
  TextEditingController _controller;

  @override
  void initState() {
    super.initState();

    _controller = TextEditingController(text: widget.initialValue);
  }

  @override
  Widget build(BuildContext context) {
    return AlertDialog(
      title: Text(widget.title),
      content: TextField(
        controller: _controller,
        onSubmitted: (text) {
          widget.onSubmit(text);
          Navigator.of(context).pop();
        },
      ),
      actions: <Widget>[
        FlatButton(
          child: Text('取消'),
          onPressed: () {
            Navigator.of(context).pop();
          },
        ),
        FlatButton(
          child: Text('确定'),
          onPressed: () {
            widget.onSubmit(_controller.text);
            Navigator.of(context).pop();
          },
        ),
      ],
    );
  }
}
