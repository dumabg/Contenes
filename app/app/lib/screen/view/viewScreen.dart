import 'package:app/api/viewApi.dart';
import 'package:app/model/view.dart';
import 'package:app/util/asyncCallWithUI.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class ViewScreen extends StatefulWidget {
  final int _userId;
  final View _view;

  ViewScreen(this._userId, this._view);

  @override
  _ViewScreenState createState() => _ViewScreenState();
}

class _ViewScreenState extends State<ViewScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          leading: FlatButton(
            child: const Icon(
              Icons.arrow_back,
              color: Colors.white,
            ),
            onPressed: () => Get.back(),
          ),
          title: Text(widget._view.title),
        ),
        body: ListView(
            children: widget._view.items.map<Widget>((item) {
          switch (item.type) {
            case 0:
              return buildItemText(item);
            case 1:
              return buildItemHttp(item);
          }
          return SizedBox(height: 0, width: 0);
        }).toList()));
  }

  Widget buildItemText(Item item) {
    return ListTile(title: Text(item.text));
  }

  Widget buildItemHttp(Item item) {
    return Card(
      child: ListTile(title: Text(item.text), onTap: () => execute(item)),
    );
  }

  execute(Item item) {
    asyncCallWithUI(context, () => ViewApi().execute(widget._userId, widget._view.templateId, item.id))
        .then((itemExecuteResult) {
          var msg = itemExecuteResult.msg;
          if (msg.isNotEmpty) {
            showDialog(
              context: context,
              barrierDismissible: false,              
              builder: (BuildContext context) {
                return AlertDialog(
                        content: Text(msg),
                        actions: <Widget>[
                          RaisedButton(
                            child: Text('Ok'),
                            onPressed: () => Navigator.of(context).pop()                            
                          )]
                        );
              }
            );
          }
        });
  }
}
