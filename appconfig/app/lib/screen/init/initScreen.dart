import 'package:appconfig/screen/home/homeScreen.dart';
import 'package:appconfig/screen/login/loginScreen.dart';
import 'package:appconfig/services/loginService.dart';
import 'package:appconfig/services/servicesManager.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class InitScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return InitScreenState();
  }
}

class InitScreenState extends State<InitScreen> {
  InitScreenState() {
    ServicesManager.loadServices().then((_) {
      Get.off(LoginService().isLogged() ? HomeScreen() : LoginScreen());
    });
  }
  @override
  Widget build(BuildContext context) {
    return Text("Loading");
  }
  
}