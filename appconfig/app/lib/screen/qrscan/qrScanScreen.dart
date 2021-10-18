import 'package:appconfig/api/accessPointApi.dart';
import 'package:appconfig/model/accessPoint.dart';
import 'package:appconfig/model/view.dart';
import 'package:appconfig/services/loginService.dart';
import 'package:appconfig/util/asyncCallWithUI.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:qr_code_scanner/qr_code_scanner.dart';
import 'package:qr_code_scanner/qr_scanner_overlay_shape.dart';

class QRScanScreen extends StatefulWidget {
  final View _view;

  const QRScanScreen(this._view, {
    Key key,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => _QRScanScreenState();
}

class _QRScanScreenState extends State<QRScanScreen> {  
  String _msg;
  bool _flashEnabled = false;
  bool _paused = false;
  QRViewController _controller;
  final GlobalKey _qrKey = GlobalKey(debugLabel: 'QR');

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: <Widget>[
          Expanded(
            child: QRView(
              key: _qrKey,
              onQRViewCreated: _onQRViewCreated,
              overlay: QrScannerOverlayShape(
                borderColor: Colors.red,
                borderRadius: 10,
                borderLength: 30,
                borderWidth: 10,
                cutOutSize: 300,
              ),
            ),
            flex: 4,
          ),
          Container (
            height: 40,
            padding: EdgeInsets.all(5.0), 
            color: _msg == null ? Colors.white : Colors.red,
            child: Center (
              child: Text("${_msg == null ? 'Scanning QR' : _msg}",
                          style: TextStyle(color: _msg == null ? Colors.black : Colors.white))
            )            
          ),
          Container(
            color: Colors.teal,
            child:
          Row( 
            mainAxisSize: MainAxisSize.max,
            children: <Widget>[
              IconButton(
                icon: Icon(_flashEnabled ? Icons.flash_off : Icons.flash_on),
                color: Colors.white,
                onPressed: () => _toggleFlash(),
              ),
              IconButton(
                icon: Icon(Icons.camera_alt),
                color: Colors.white,
                onPressed: () => _flipCamera(),
              ),
              IconButton(
                icon: Icon(_paused ? Icons.play_arrow : Icons.pause),
                color: Colors.white,
                onPressed: () => _togglePause(),
              ),
              Spacer(),
              IconButton(
                  icon: Icon(Icons.clear),
                  color: Colors.white,
                  onPressed: () => Get.back(),                  
                )
            ],
          ),
          )
         ],        
      ),
    );
  }

  void _flipCamera() {
    _controller?.flipCamera();    
    setState(() {
      _paused = false;
      _msg = null;
    });
  }

  void _toggleFlash() {
    if (_controller != null) {
      _controller.toggleFlash();
      setState(() {
        _flashEnabled = !_flashEnabled;
      });
    }
  }

  void _togglePause() {
    if (_controller != null) {
      if (_paused) {
        _controller?.resumeCamera();
      }
      else {
        _controller?.pauseCamera();
      }
      setState(() {
        _paused = !_paused;
        _msg = null;
      });
    }
  }

  void _onQRViewCreated(QRViewController controller) {
    this._controller = controller;
    controller.scannedStringStream.listen((scanData) {
      setState(() {
        _paused = true;
      });
      final qrText = scanData;
      bool error = true;
      print(qrText);
      // Example: https://m4rn3.app.goo.gl/YM0b?i=2#6517904929456128
      if (qrText.startsWith(AccessPoint.PREFIX_ID)) {
        // id = 2#6517904929456128        
        final values = qrText.substring(AccessPoint.PREFIX_ID.length).split('#');
        if (values.length == 2) {
          final userId = values[0];
          if (userId == LoginService().userId()) {
            final accessPointId = int.parse(values[1]);
            asyncCallWithUI(context, () => AccessPointApi().add(accessPointId, widget._view.id))
              .then((_) => Get.back(result: accessPointId));
          }
          else {            
            setState(() {
              _msg = """You aren't the owner of this Augdim QR code.
              You can't configure it.""";
              });
          }
          error = false;
        }
      }
      if (error) {
        setState(() {
          _msg = "This isn't a valid Augdim QR code";
        });
      }
    });    
  } 

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }
}