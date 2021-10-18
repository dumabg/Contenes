
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';


class NoAugdimCodeScreen extends StatefulWidget {
  final dynamic _scannedData;

  NoAugdimCodeScreen(this._scannedData);

  @override
  _NoAugdimCodeScreenState createState() => _NoAugdimCodeScreenState();
}

class _NoAugdimCodeScreenState extends State<NoAugdimCodeScreen> {

  @override
  Widget build(BuildContext context) {
    // Scaffold is a layout for the major Material Components.
    return Scaffold(
      appBar: AppBar(
        leading: FlatButton(          
          child: const Icon(Icons.arrow_back, color: Colors.white,),
          onPressed: () => Get.back(),
        ),
        title: const Text('QR scanned'),
      ),
      body: Center(      
        child: Padding(
          padding: const EdgeInsets.all(35.0),
          child: Column(
            children: <Widget>[
            const Text("No Augdim QR", style: const TextStyle(fontSize: 20.0, color: Colors.red, fontWeight: FontWeight.bold)),
            const SizedBox(height: 20),
            const Text("Augdim QRs shows actions to do with the object associated with the QR. This is a normal QR and contains this text:"),
            const SizedBox(height: 10),
            DecoratedBox(
              decoration: BoxDecoration(
                    color: Colors.yellow.shade100,
                    borderRadius: const BorderRadius.all(const Radius.circular(10.0))),                      
              child: Padding(
                padding: const EdgeInsets.all(15.0),
                child: _scannedText()
              ) 
            ,)          
          ],)
        )
      )
    );
  }

  Widget _scannedText() {
    final scannedData = widget._scannedData;    
    if (scannedData == null) {
      return Text('');
    }
    final text = scannedData['text'] as String ?? '';
    return scannedData['isUrl'] ?  
            GestureDetector(
              child: Text(text, style: TextStyle(decoration: TextDecoration.underline, color: Colors.blue)),
              onTap: () => launch(text)
            )
            :
            Text(text);
    }
}