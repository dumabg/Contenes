void configureEnvironment(bool isDev) {
  Config._instance = isDev ? 
    Config._internal("http://192.168.0.14:8080/v1") :
    Config._internal("https://www.augdim.com/v1");
}

class Config {
  final String apiBaseUrl;
  factory Config() {
    return _instance;
  }
  Config._internal(this.apiBaseUrl);
  static Config _instance;
}