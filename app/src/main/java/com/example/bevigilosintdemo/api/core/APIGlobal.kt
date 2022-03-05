package com.example.bevigilosintdemo.api.core

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIGlobal {

    private fun getClient(): Retrofit {
        val gson = GsonBuilder().serializeNulls().setLenient().create()

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(getBaseURL())
            .client(
                getOkHttpClient(
                    getHttpLoggingInterceptor()
                )
            )
            .build()
    }

    fun create(): ApiInterface {
        return getClient()
            .create(ApiInterface::class.java)
    }

    private fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .followSslRedirects(true)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)

        //enable http logging for development environment
        client.addInterceptor(httpLoggingInterceptor)
        return client.build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    fun getBaseURL(): String {
        return "https://osint.bevigil.com/api/"
    }
}