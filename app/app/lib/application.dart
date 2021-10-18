import 'package:app/screen/home/HomeScreen.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

startApp() async {
   {
    runApp(MaterialApp(
      title: 'Augdim configurator',
      navigatorKey: Get.key,
      home: HomeScreen()
    ));
  }
}
