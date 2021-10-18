import 'package:flutter/material.dart';

class Loader extends StatelessWidget {
  static OverlayEntry currentLoader;
  static void show(BuildContext context) {
    currentLoader = new OverlayEntry(
        builder: (context) => Stack(
              children: <Widget>[
                Container(
                  color: Color(0x99ffffff),
                ),
                Center(
                  child: Loader(),
                ),
              ],
            ));
    Overlay.of(context).insert(currentLoader);
  }
  static void hide() {
    currentLoader?.remove();
  }
  @override
  Widget build(BuildContext context) {
    return Center(
      child: CircularProgressIndicator(),
    );
  }
}

void showLoading(Future future, BuildContext context) {  
  Loader.show(context);
  future.whenComplete(() {
    Loader.hide();
  });
}
