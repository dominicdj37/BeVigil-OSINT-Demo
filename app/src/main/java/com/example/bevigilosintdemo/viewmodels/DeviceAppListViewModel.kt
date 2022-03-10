package com.example.bevigilosintdemo.viewmodels

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.App
import com.example.bevigilosintdemo.api.model.Apps
import com.example.bevigilosintdemo.api.repository.OSINTRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeviceAppListViewModel: ViewModel() {

    var apps: Apps? = null
    var isDomainSearchMode: Boolean = false
    val displayingAppList: ArrayList<App> = arrayListOf()
    var showingInstalledApps = true


    fun getAppListFromPackages(systemPackages: List<PackageInfo>, packageManager: PackageManager, onReady: ( ArrayList<App>)-> Unit) {
        GlobalScope.launch {
            val returnAppList: ArrayList<App> = arrayListOf()
            withContext(Dispatchers.IO) {
                systemPackages.forEach { packageInfo ->
                    returnAppList.add(App().apply {
                        icon = packageInfo.applicationInfo.loadIcon(packageManager)
                        name = packageInfo.applicationInfo.loadLabel(packageManager).toString()
                        packageID = packageInfo.packageName
                        version = packageInfo.versionName
                    })
                }
            }
            withContext(Dispatchers.Main) {
                onReady.invoke(returnAppList)
            }
        }
    }

    fun getApps(domainName: String): MutableLiveData<ApiResponse> {
        return OSINTRepository.getInstance().getApps(domainName)
    }

    fun updateAppList() {
        displayingAppList.clear()
        apps?.packages?.let { displayingAppList.addAll(it) }
    }
}