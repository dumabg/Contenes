import 'package:appconfig/screen/init/initScreen.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

startApp() async {
   {
    runApp(MaterialApp(
      title: 'Augdim configurator',
      navigatorKey: Get.key,
      home: InitScreen()
    ));
  }
}
