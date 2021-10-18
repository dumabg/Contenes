import 'package:appconfig/services/loginService.dart';
import 'package:flutter/material.dart';
import 'mainWidget.dart';

class HomeScreen extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    var mainWidget = MainWidget(context);
    // Scaffold is a layout for the major Material Components.
    return Scaffold(
      appBar: AppBar(
        title: Text('Augdim configurator'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.refresh),
            tooltip: 'Refresh',
            onPressed: () => mainWidget.refreshViews(),
          ),
        ],
      ),
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            ListTile(
            ),
            ListTile(
              leading: Icon(Icons.recent_actors),
              title: Text('Logout'),
              onTap: () { 
                LoginService().logout();
                LoginService().redirectToLoginScreen();
              }
            ),
          ],
        ),
      ),
      // body is the majority of the screen.
      body: Flex(
        direction: Axis.horizontal,
        children: [
          Expanded (            
          child: mainWidget,
        )])
    );
  }
}
