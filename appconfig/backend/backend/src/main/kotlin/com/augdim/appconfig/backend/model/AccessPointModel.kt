package com.augdim.appconfig.backend.model

import com.augdim.backend.NotFound
import com.augdim.backend.gcd.DatastoreService

// id = AccessPoint id
// idView = view id to serve with the AccessPoint id
// params = additional params to this AccessPoint
class AccessPoint(val id: Long, val idView: Long, val params: String?)

class AccessPointModel: DatastoreService() {

    fun add(accessPoint: AccessPoint) {
        val view = getView(accessPoint.idView)
        view.accessPoint = accessPoint.id
        updateEntity(view)
    }

    fun delete(idView: Long) {
        val view = getView(idView)
        view.accessPoint = null
        updateEntity(view)
    }

    private fun getView(idView: Long): View {
        return getEntity<View>(idView) ?: throw NotFound("View $idView not found")
    }
}
