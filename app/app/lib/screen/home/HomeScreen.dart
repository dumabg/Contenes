import 'package:flutter/material.dart';
import 'mainWidget.dart';

class HomeScreen extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    var mainWidget = MainWidget();
    // Scaffold is a layout for the major Material Components.
    return Scaffold(
      appBar: AppBar(
        title: Text('Augdim'),
        actions: <Widget>[
          // IconButton(
          //   icon: Icon(Icons.memory),
          //   tooltip: 'Scan',
          //   onPressed: () => {},
          // ),
        ],
      ),
      // body is the majority of the screen.
      body: mainWidget
    );
  }
}
