import 'package:app/application.dart';
import 'package:app/config.dart';

//In Visual Studio Code, configure launch.json to run main_dev:
// "program": "lib/main_dev.dart"
void main() {
  configureEnvironment(true);
  startApp();
}