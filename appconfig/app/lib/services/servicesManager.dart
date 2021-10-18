import 'package:appconfig/services/loginService.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ServicesManager {
    static Future<void> loadServices() async {
        await SharedPreferences.getInstance().then((preferences) {
          LoginService.start(preferences);
        });
    }
}