import 'dart:convert';
import 'package:app/model/view.dart';
import 'backendApiBase.dart';

class ViewApi extends BackendApiBase {

  ViewApi(): super();
  
  Future<View> view(int userId, int accessPoint) async {
    return Future<View>(() async {      
      var response = await httpGet('view/$userId/$accessPoint');
      dynamic jsonData = json.decode(response.body);
      var view = View();
      view.id = jsonData['id'];
      view.templateId = jsonData['templateId'];
      view.title = jsonData['title'];
      List jsonItems = jsonData['items'] as List;
      jsonItems.forEach((jsonItem) {
        var item = Item();
        item.id = jsonItem['id'];
        item.type = jsonItem['type'];
        item.position = jsonItem['position'];
        item.text = jsonItem['text'];
        view.items.add(item);   
      });
      return view;  
    });
  }

  Future<ItemExecuteResult> execute(int userId, int templateId, int itemId) async {
    return Future<ItemExecuteResult>(() async {
      var response = await httpPost('item', 
        {"userId": userId.toString(), "templateId": templateId.toString(), "itemId": itemId.toString()});
      dynamic jsonData = json.decode(response.body);
      var result = ItemExecuteResult();
      result.msg = jsonData['msg'];
      return result;
    });
  }
}