package com.example.bevigilosintdemo.api.core

import com.example.bevigilosintdemo.api.model.AssetModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface ApiInterface {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "X-Access-Token: Gz6jvzUG6Gei0GOu"
    )
    @GET()
    fun getAllAssets(@Url url: String): Observable<AssetModel>
}
