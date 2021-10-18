import 'package:appconfig/application.dart';
import 'package:appconfig/config.dart';

//In Visual Studio Code, configure launch.json to run main_dev:
// "program": "lib/main_dev.dart"
void main() {
  configureEnvironment(true);
  startApp();
}