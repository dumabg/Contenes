import 'package:flutter/cupertino.dart';

class TextQRCode extends Text {

  TextQRCode( {
    Key key,
    TextStyle style
  }) : super("\u{e938}", key: key, style: style == null ? TextStyle(fontFamily: 'icomoon') : style.copyWith(fontFamily: 'icomoon'));

  TextQRCode.withFontSize(double fontSize): super("\u{e938}", style: TextStyle(fontFamily: 'icomoon', fontSize: fontSize));
      
}