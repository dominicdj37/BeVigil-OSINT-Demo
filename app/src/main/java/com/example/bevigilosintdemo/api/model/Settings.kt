package com.example.bevigilosintdemo.api.model

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
@Keep
data class Settings(
    var btocken: String? = null
)
