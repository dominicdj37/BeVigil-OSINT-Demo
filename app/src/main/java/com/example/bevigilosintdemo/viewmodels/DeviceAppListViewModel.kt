package com.example.bevigilosintdemo.viewmodels

import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel

class DeviceAppListViewModel: ViewModel() {
    val deviceList: ArrayList<PackageInfo> = arrayListOf()
}