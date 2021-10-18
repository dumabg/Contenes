class ServerStatusException implements Exception {  
  int status;
  bool isUnauthorized() => status == 401;
  ServerStatusException(this.status);
}
