package com.augdim.backend.gcd

import com.google.cloud.datastore.BaseEntity
import com.google.cloud.datastore.Key

//import com.google.appengine.api.datastore.Entity

class CantConstructClassFromEntityException(entity: BaseEntity<Key>, className: String) :
        Exception("Can't construct ${className} with an Entity with names ${entity.names.toString()}")