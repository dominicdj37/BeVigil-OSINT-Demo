package com.example.bevigilosintdemo.api.core

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("result") @Expose
    var result: Any? = null,

    @SerializedName("message") @Expose
    var message: String? = null
)