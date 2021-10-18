import 'dart:convert';
import 'package:appconfig/model/template.dart';
import 'backendApiBase.dart';
import 'unexpectedApiResponseException.dart';

class TemplateApi extends BackendApiBase {

  TemplateApi(): super(true);

  Future<List<Template>> templates() async {
    return Future<List<Template>>(() async {
      var result = List<Template>();
      var response = await httpGet('templates');
      dynamic jsonTemplates = json.decode(response.body);
      if (jsonTemplates is List) {
        jsonTemplates.forEach((item) {
          if (item is Map) {            
            var template = Template();
            template.id = item['id'];
            template.name = item['name'];
            result.add(template);
          }
          else {
            throw UnexceptedApiResponseException("Map", "${item.toString()}");
          }          
        });
      } 
      print(jsonTemplates.toString());
      return result;
    });
  }
}
