import 'backendApiBase.dart';

class AccessPointApi extends BackendApiBase {

  AccessPointApi(): super(true);

  Future<void> add(int id, int viewId) async {
    return Future<void>(() async {
      await httpPost('ap', {'i': id.toString(), 'v': viewId.toString()});
    });
  }


  Future<void> delete(int id) async {
    return httpDelete("ap/$id");
  }  
}
