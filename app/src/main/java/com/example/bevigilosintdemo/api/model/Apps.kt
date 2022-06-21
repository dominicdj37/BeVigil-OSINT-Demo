package com.example.bevigilosintdemo.api.model

import android.graphics.drawable.Drawable
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.core.AssetKeyType
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Apps {
    @SerializedName("domain") @Expose
    var domain: String? = null

    @SerializedName("packages") @Expose
    var packages: ArrayList<App> = arrayListOf()
}