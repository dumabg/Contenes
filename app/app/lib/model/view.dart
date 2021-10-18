class Item {
  int id;
  int position;
  int type;
  String text;
}

class View {
  int id;
  int templateId;
  String title;
  List<Item> items = List<Item>();
}

class ItemExecuteResult {
  String msg;
}