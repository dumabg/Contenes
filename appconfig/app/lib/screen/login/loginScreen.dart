import 'package:appconfig/api/loginApi.dart';
import 'package:appconfig/screen/home/homeScreen.dart';
import 'package:appconfig/util/asyncCallWithUI.dart';
import 'package:appconfig/util/toast.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}


class _LoginScreenState extends State<LoginScreen> {
  final _emailController = TextEditingController();
  final _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold (
      body: Center(
        child: Column (
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [            
            Container (
              width: 200,
              child: TextField(         
                controller: _emailController,
                decoration: InputDecoration(                
                  labelText: 'Email',
                ),
              ),              
            ),
            Container (
              width: 200,
              child: TextField(
                controller: _passwordController, 
                obscureText: true,     
                decoration: InputDecoration(                
                  labelText: 'Password',
                ),
              )
            ),
            Container (
              height: 30,
            ),
            RaisedButton(
              child: const Text('Login'),
              onPressed: () {
                _login(context);
              },              
            )
          ]
        )
      )
    );
  }

  @override
  void dispose() {
    _emailController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _login(BuildContext context) {
    asyncCallWithUI(context, () { return LoginApi().login(_emailController.text, _passwordController.text); })
      .then( (ok) {
        if (ok) {
          Get.off(HomeScreen());
        }
        else {
          Toast.showInfo("Invalid user or password");
        }
      });
  }
  
}