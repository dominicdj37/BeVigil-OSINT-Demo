package com.example.bevigilosintdemo.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


object HttpStatusCode {
    const val successOne = 200
    const val successTwo = 201
    const val successThree = 204

    const val badRequest = 400
    const val unauthorized = 401
    const val forbidden = 403
    const val notFound = 404
    const val unProcessableEntity = 422

    const val internalServerError = 500
    const val noInternet = 503
    const val timeout = 504

}

class ErrorBody {
    @SerializedName("detail") @Expose
    val detail: String? = null
}

class Error {

    @SerializedName("code") @Expose
    var mCode: Int? = null

    @SerializedName("message") @Expose
    var message: String? = null
}