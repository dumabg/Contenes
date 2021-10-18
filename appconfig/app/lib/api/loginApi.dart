import 'package:appconfig/api/backendApiBase.dart';
import 'package:appconfig/services/loginService.dart';
import 'serverStatusException.dart';

class LoginApi extends BackendApiBase {

  LoginApi(): super(false);

  Future<bool> login(String user, String password) async {
    return Future<bool>(() async {
      try {
        var response = await httpPost('login', {'u': user, 'p': password});
        LoginService().login(response.body);
        return true;
      }
      on ServerStatusException catch(e) {
        if (e.isUnauthorized()) {
          return false;
        }
        else {
          rethrow;
        }
      }
    });
  }
}