package com.example.bevigilosintdemo.viewmodels

import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.api.repository.OSINTRepository

class AssetDetailsViewModel: ViewModel() {
    var asset: AssetModel = AssetModel()
    var packageID = ""

    fun getAllAssets(packageID: String): MutableLiveData<ApiResponse> {
        return OSINTRepository.getInstance().getAllAssets(packageID)
    }

    fun fetchAppDetailsIfAvailable(packageManager: PackageManager) {
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        val appPackage = packages.find { packageInfo -> packageInfo.packageName == packageID }
        asset.appIcon = appPackage?.applicationInfo?.loadIcon(packageManager)
        asset.appName = appPackage?.applicationInfo?.loadLabel(packageManager).toString()
    }




}