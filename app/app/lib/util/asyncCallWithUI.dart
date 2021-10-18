import 'package:app/screen/loading/loading.dart';
import 'package:flutter/widgets.dart';
import 'toast.dart';

Future<T> asyncCallWithUI<T>(BuildContext context, Future<T> f()) async {
  var future = f();
  showLoading(future, context);
  future.catchError((e) {
    Toast.show(e);
  });
  return future;
}