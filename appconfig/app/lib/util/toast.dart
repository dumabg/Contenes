import 'package:appconfig/api/serverStatusException.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart' as ft;

class Toast {
  static void show(Exception e) {
    String msg;
    if (e is ServerStatusException) {
      msg = '''Server returns an unexpected error: ${e.status}.
        Try it again later.
        ''';
    }
    else {
      msg = "Unexpected error: ${e.toString()}";
    }
    showError(msg);
  }

  static void showError(String msg) {
    ft.Fluttertoast.showToast(
        msg: msg,
        toastLength: ft.Toast.LENGTH_SHORT,
        gravity: ft.ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.red,
        textColor: Colors.white,
        fontSize: 16.0
    );
  }

  static void showInfo(String msg) {
    ft.Fluttertoast.showToast(
        msg: msg,
        toastLength: ft.Toast.LENGTH_SHORT,
        gravity: ft.ToastGravity.BOTTOM,
        timeInSecForIos: 1,
        backgroundColor: Colors.blueAccent,
        textColor: Colors.white,
        fontSize: 16.0
    );
  }
}
