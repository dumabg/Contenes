import 'dart:async';
import 'dart:io';
import 'package:appconfig/api/serverStatusException.dart';
import 'package:appconfig/services/loginService.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import '../config.dart';

class BackendApiBase {
    static const String _BEARER = 'Bearer ';
    bool _loggedRequired;
    
    BackendApiBase(this._loggedRequired);

    String _url(String route) => '${Config().apiBaseUrl}/$route';

    Map<String, String> _authorizationHeader() {
      var jwtBearer = LoginService().jwtBearer();
      return {HttpHeaders.authorizationHeader : '$_BEARER$jwtBearer'};
    }

    @protected
    Future<http.Response> httpGet(String route) async {      
      var response = await http.get(_url(route),
        headers: _authorizationHeader()
      );
      _treatResponse(response);
      return response;
    }

    @protected
    Future<http.Response> httpDelete(String route) async {      
      var response = await http.delete(_url(route),
        headers: _authorizationHeader()
      );
      _treatResponse(response);
      return response;
    }

    @protected
    Future<http.Response> httpPost(String route, dynamic body) async {
      var response = await http.post(_url(route),
        body: body,
        headers: _authorizationHeader()
      );
      _treatResponse(response);
      return response;
    }

    void _treatResponse(http.Response response) {
      var statusCode = response.statusCode;
      switch (statusCode) {
        case 200:
          var authorization = response.headers[HttpHeaders.authorizationHeader];
          if ((authorization != null) && (authorization.startsWith(_BEARER))) {
            var jwtBearer = authorization.substring(_BEARER.length);
            LoginService().updateJwtBearer(jwtBearer);
          }
          break;
        case 401:
          LoginService().logout();
          if (_loggedRequired) {
            LoginService().redirectToLoginScreen();
          }
          else {
            throw ServerStatusException(statusCode);  
          }
          break;
        default:
          throw ServerStatusException(statusCode);
      }
    }
}

