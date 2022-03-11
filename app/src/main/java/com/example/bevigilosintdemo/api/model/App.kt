package com.example.bevigilosintdemo.api.model

import android.graphics.drawable.Drawable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class App {
    @SerializedName("package_id")
    @Expose
    var packageID: String? = null

    @SerializedName("app_name")
    @Expose
    var name: String? = null

    @SerializedName("app_version")
    @Expose
    var version: String? = null

    var icon: Drawable? = null

}