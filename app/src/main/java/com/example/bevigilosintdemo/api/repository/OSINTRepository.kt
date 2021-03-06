package com.example.bevigilosintdemo.api.repository

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.example.bevigilosintdemo.api.core.APIGlobal
import com.example.bevigilosintdemo.api.core.ApiConstants
import com.example.bevigilosintdemo.api.core.ApiConstants.APPS
import com.example.bevigilosintdemo.api.core.ApiConstants.ASSETS
import com.example.bevigilosintdemo.api.core.ApiConstants.formatUrl
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OSINTRepository private constructor() {

    companion object {
        private var mInstance: OSINTRepository? = null
        fun getInstance(): OSINTRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = OSINTRepository()
                }
            }
            return mInstance!!
        }
    }

    @SuppressLint("CheckResult")
    fun getAllAssets(packageID: String): MutableLiveData<ApiResponse> {

        val url = ApiConstants.getRequestUrlFor(ASSETS).formatUrl(packageID)
        val liveData: MutableLiveData<ApiResponse> = MutableLiveData()
        APIGlobal.create().getAllAssets(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseModel ->
                liveData.postValue(ApiResponse(responseModel))
            }, { error ->
                liveData.postValue(ApiResponse(error))
            })
        return liveData
    }

    @SuppressLint("CheckResult")
    fun getApps(domainName: String): MutableLiveData<ApiResponse> {

        val url = ApiConstants.getRequestUrlFor(APPS).formatUrl(domainName)
        val liveData: MutableLiveData<ApiResponse> = MutableLiveData()
        APIGlobal.create().getApps(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseModel ->
                liveData.postValue(ApiResponse(responseModel))
            }, { error ->
                liveData.postValue(ApiResponse(error))
            })
        return liveData
    }

}