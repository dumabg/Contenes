import 'package:app/api/viewApi.dart';
import 'package:app/screen/noAugdimCode/noAugdimCodeScreen.dart';
import 'package:app/screen/qrscan/qrScanScreen.dart';
import 'package:app/screen/view/viewScreen.dart';
import 'package:app/util/asyncCallWithUI.dart';
import 'package:app/util/widgets/IconQRCode.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class MainWidget extends StatelessWidget {
//   final _state = _MainWidgetState();
//   MainWidget();

//   @override
//   _MainWidgetState createState() => _state;
// }

// class _MainWidgetState extends State<MainWidget> {  

  void _readQr(BuildContext context) async {
    var result = await Get.to(QRScanScreen());
    print(result);
    if (result != null) {
      if (result['isAugminCode']) {
        var userId = result['userId'];
        asyncCallWithUI(context, () => ViewApi().view(userId, result['accessPoint']))
        .then((view) {
          view.items.sort((item1, item2) => item1.position.compareTo(item2.position));
          Get.to(ViewScreen(userId, view));
        })
        .catchError((onError) {
           showDialog(
              context: context,
              barrierDismissible: false,              
              builder: (BuildContext context) {
                return AlertDialog(
                        content: Text("This QR isn't enabled"),
                        actions: <Widget>[
                          RaisedButton(
                            child: Text('Ok'),
                            onPressed: () => Navigator.of(context).pop()                            
                          )]
                        );
              }
            );
        });        
      }
      else {
        Get.to(NoAugdimCodeScreen(result));
      }
    }    
  }

  @override
  Widget build(BuildContext context) {
    return Center(
          child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
            DecoratedBox (              
              decoration: const ShapeDecoration(
                color: Colors.lightBlue,
                shape: const CircleBorder(),
              ),
              child: Padding(
                padding: const EdgeInsets.all(20.0),
                child:
              IconButton(
                icon: const IconQRCode(),
                iconSize: 70.0,
                color: Colors.white,
                onPressed: () => _readQr(context),
              )
              ),
            ),
            const SizedBox(height: 20),
            const Text('Scan QR')
          ],
        ));
  }
}
