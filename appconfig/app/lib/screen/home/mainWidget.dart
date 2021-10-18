import 'package:after_layout/after_layout.dart';
import 'package:appconfig/api/accessPointApi.dart';
import 'package:appconfig/api/templateApi.dart';
import 'package:appconfig/api/viewApi.dart';
import 'package:appconfig/model/template.dart';
import 'package:appconfig/model/view.dart';
import 'package:appconfig/screen/qrscan/qrScanScreen.dart';
import 'package:appconfig/util/asyncCallWithUI.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class MainWidget extends StatefulWidget {
  final _state = _MainWidgetState();
  final BuildContext _screenContext;

  MainWidget(this._screenContext);

  @override
  _MainWidgetState createState() => _state;

  refreshViews() {
    _state.refreshViews();
  }
}

class _MainWidgetState extends State<MainWidget> with AfterLayoutMixin<MainWidget>{
  List<Template> _templates = List<Template>();
  List<View> _views = List<View>();

  refreshViews() {
    asyncCallWithUI(widget._screenContext, () => TemplateApi().templates())
      .then((templates) {
        asyncCallWithUI(widget._screenContext, () => ViewApi().views())
        .then((views) {
          views.sort((view1, view2) => view1.name.compareTo(view2.name));
          setState(() {
            _views = views;
            _templates = templates;
          });
       });
    });
  }

  String _getTemplateName(View view) {
    return _templates.firstWhere((template) => template.id == view.templateId)?.name ?? "";
  }

  void _readQr(View view) async {
    int accessPoint = await Get.to(QRScanScreen(view));
    if (accessPoint != null) {
      setState(() => view.accessPoint = accessPoint);
    }
  }

  void _deleteAccessPoint(View view) {
    // bool result = Get.defaultDialog(
    //   title: "",
    //   content: Text('''This view has already assigned a QR code.
        
    //     Do you want to unassign it?'''),
    //   confirm: RaisedButton(child: Text("Yes"), onPressed: () => Get.back(result: true)),
    //   cancel: RaisedButton(child: Text("No"), onPressed: () => Get.back(result: false))
    // );
    // if (result) {
    //       asyncCallWithUI(widget._screenContext, () => AccessPointApi().delete(view.id))
    //         .then((_) => setState( () => view.accessPoint = null));
    // }

    showDialog<bool>(
      context: context,
      builder: (_) => AlertDialog(
        content: Text('''This view has already assigned a QR code.
        
        Do you want to unassign it?'''),
        actions: <Widget>[
          RaisedButton(child: Text("Yes"), onPressed: () => Navigator.pop(context, true)),
          RaisedButton(child: Text("No"), onPressed: () => Navigator.pop(context, false)),
        ],
      )
    ).then((result) {
        if (result) {
          asyncCallWithUI(widget._screenContext, () => AccessPointApi().delete(view.id))
            .then((_) => setState( () => view.accessPoint = null));
        }
      });
  }

  @override
  void afterFirstLayout(BuildContext context) {
    refreshViews();
  }

  @override
  Widget build(BuildContext context) {
    return ListView(
        children: _views
            .map((view) => Card(
                  child: Row(
                    children: [
                      Container(height: 60.0, width: 10.0, color: view.accessPoint == null ? Colors.red : Colors.greenAccent),
                      Expanded(
                        child: Padding(
                          padding: const EdgeInsets.only(left: 8.0, right: 8.0),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(view.name, textScaleFactor: 1.5),
                              Text(_getTemplateName(view), textScaleFactor: 1.0, style: TextStyle(color: Colors.grey),)
                            ]
                          )
                        )
                      ),
                      IconButton(
                        icon: Icon(view.accessPoint == null ? Icons.link : Icons.link_off), 
                        onPressed: () => view.accessPoint == null ? _readQr(view) : _deleteAccessPoint(view)
                      )
                    ]),
                  )
                ).toList()
        );
  }
}
