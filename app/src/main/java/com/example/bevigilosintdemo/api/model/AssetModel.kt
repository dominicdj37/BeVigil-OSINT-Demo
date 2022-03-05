package com.example.bevigilosintdemo.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AssetModel {

    @SerializedName("package_id") @Expose
    var packageID: String? = null

    @SerializedName("assets") @Expose
    var assets: HashMap<String,ArrayList<String>> = hashMapOf()
}