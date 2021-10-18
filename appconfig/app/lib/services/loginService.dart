import 'package:appconfig/screen/login/loginScreen.dart';
import 'package:get/get.dart';
import 'package:shared_preferences/shared_preferences.dart';

class LoginService {
  static LoginService _instance;
  static const PREFIX = "service.login.";
  static const PREFERENCE_ID = "$PREFIX.id";
  static const PREFERENCE_JWTBEARER = "$PREFIX.jwtBearer";
  String _jwtBearer;
  String _userId;

  factory LoginService() => _instance;
  LoginService._internal();

  static void start(SharedPreferences preferences) {      
      _instance = LoginService._internal()
        .._userId = preferences.get(PREFERENCE_ID)
        .._jwtBearer = preferences.get(PREFERENCE_JWTBEARER) ?? "";
  }

  void login(String userId) {
    _userId = userId;
    SharedPreferences.getInstance().then((preferences) {
      preferences.setString(PREFERENCE_ID, _userId);
      preferences.setString(PREFERENCE_JWTBEARER, _jwtBearer);
    });
  }

  void updateJwtBearer(String jwtBearer) {
    _jwtBearer = jwtBearer;
    SharedPreferences.getInstance().then((preferences) =>
      preferences.setString(PREFERENCE_JWTBEARER, _jwtBearer));
  }

  String userId() => _userId;

  String jwtBearer() => _jwtBearer;

  bool isLogged() => _userId != null;

  void logout() {
    SharedPreferences.getInstance().then((preferences) {
      preferences.remove(PREFERENCE_ID);
      preferences.remove(PREFERENCE_JWTBEARER);
    });
    _userId = null;
  }

  void redirectToLoginScreen() {
    Get.off(LoginScreen());
  }
}