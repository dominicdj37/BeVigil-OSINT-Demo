package com.example.bevigilosintdemo.core

import android.app.Application
import com.example.bevigilosintdemo.utils.ResourceUtils

class AppContext: Application() {

    override fun onCreate() {
        super.onCreate()
        ResourceUtils.initialize(this)
    }
}


