package com.augdim.backend

class ServiceUnavailable: Throwable()
class BadRequest: Throwable()
class Unauthorized: Throwable()
class NotFound(message: String): Throwable(message)
class SecretInvalidError: Throwable()