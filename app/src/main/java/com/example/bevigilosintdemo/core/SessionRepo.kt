package com.example.bevigilosintdemo.core

import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.api.model.Settings
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser

object SessionRepo {
    var user: FirebaseUser? = null
    var settings: Settings? = null
    var selectedAsset: AssetModel? = null

    fun initialize(user: FirebaseUser?, settings: Settings) {
        this.user = user
        this.settings = settings
    }

    fun clearData() {
        user = null
        settings = null
        selectedAsset = null
    }

}