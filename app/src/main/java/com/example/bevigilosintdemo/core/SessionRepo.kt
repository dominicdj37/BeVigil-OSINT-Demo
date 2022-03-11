package com.example.bevigilosintdemo.core

import com.example.bevigilosintdemo.api.model.AssetModel
import com.firebase.ui.auth.IdpResponse

object SessionRepo {
    fun initialize(response: IdpResponse?) {
        firebaseSessionResponse = response

    }

    var firebaseSessionResponse: IdpResponse? = null
    var selectedAsset: AssetModel? = null
}