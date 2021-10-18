package com.augdim.backend.gcd

class ClassNotRegisteredException(className: String):
        Exception("$className not registered")