package com.example.bevigilosintdemo.api.core

import com.example.bevigilosintdemo.api.model.Error
import com.example.bevigilosintdemo.api.model.ErrorBody
import com.example.bevigilosintdemo.api.model.HttpStatusCode
import com.google.gson.Gson
import retrofit2.HttpException
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiResponse() {

    var data: Any? = null
    var error: Error? = null
    var isSuccess = data != null && error == null

    constructor(data: Any) : this() {
        this.data = data
    }

    constructor(throwable: Throwable): this() {
        this.error = Error()
        this.error?.message = throwable.message
        when (throwable) {
            is HttpException -> {
                this.error?.mCode = throwable.code()
                try {
                    val errorBody: ErrorBody? = Gson().fromJson(throwable.response()?.errorBody()?.string(), ErrorBody::class.java)
                    this.error?.message = errorBody?.detail
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            is UnknownHostException -> {
                this.error?.mCode = HttpStatusCode.notFound
            }

            is SocketTimeoutException -> {
                this.error?.mCode = HttpStatusCode.timeout
            }

            is ConnectException -> {
                this.error?.mCode = HttpStatusCode.noInternet
            }
        }
    }
}