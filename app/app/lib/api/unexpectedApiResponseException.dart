class UnexceptedApiResponseException implements Exception {
  final String _expected;
  final String _received;

  const UnexceptedApiResponseException(this._expected, this._received);

  @override
  String toString() {
    return '''UnexceptedApiResponseException: 
      Expected: $_expected
      Received: $_received
      ''';
  }
}
