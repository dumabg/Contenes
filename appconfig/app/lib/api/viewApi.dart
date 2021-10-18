import 'dart:convert';
import 'package:appconfig/model/view.dart';
import 'backendApiBase.dart';
import 'unexpectedApiResponseException.dart';

class ViewApi extends BackendApiBase {

  ViewApi(): super(true);
  
  Future<List<View>> views() async {
    return Future<List<View>>(() async {
      var result = List<View>();
      var response = await httpGet('views');
      dynamic jsonViews = json.decode(response.body);
      if (jsonViews is List) {
        jsonViews.forEach((item) {
          if (item is Map) {            
            var view = View();
            view.id = item['id'];
            view.name = item['name'];
            view.templateId = item['templateId'];
            view.accessPoint = item['accessPoint'];
            result.add(view);
          }
          else {
            throw UnexceptedApiResponseException("Map", "${item.toString()}");
          }          
        });
      } 
      print(jsonViews.toString());
      return result;
    });
  }
}