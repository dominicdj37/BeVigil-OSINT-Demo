package com.example.bevigilosintdemo.viewmodels

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.text.format.Formatter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.App
import com.example.bevigilosintdemo.api.model.Apps
import com.example.bevigilosintdemo.api.repository.OSINTRepository
import com.example.bevigilosintdemo.utils.ResourceUtils.saveFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DeviceAppListViewModel: ViewModel() {

    var apps: Apps? = null
    var isDomainSearchMode: Boolean = false
    val displayingAppList: ArrayList<App> = arrayListOf()
    var showingInstalledApps = true

    fun getAppListFromPackages(systemPackages: List<PackageInfo>, packageManager: PackageManager, context: Context, onReady: ( ArrayList<App>)-> Unit) {
        GlobalScope.launch {
            val returnAppList: ArrayList<App> = arrayListOf()
            withContext(Dispatchers.IO) {
                systemPackages.forEach { packageInfo ->
                    returnAppList.add(App().apply {
                        icon = packageInfo.applicationInfo.loadIcon(packageManager)
                        name = packageInfo.applicationInfo.loadLabel(packageManager).toString()

                        val size = File(packageInfo.applicationInfo.publicSourceDir).length()
                        appSize = Formatter.formatFileSize(context, size)

                        appFile = File(packageInfo.applicationInfo.publicSourceDir)

                        val simpleDateFormat =
                            SimpleDateFormat("HH:MM dd/MM/yyyy", Locale.getDefault())
                        val dateString = simpleDateFormat.format(packageInfo.firstInstallTime)
                        installedDate = dateString
                        try {

                            appSource = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                "${packageManager.getInstallSourceInfo(packageInfo.packageName).installingPackageName} \n" +
                                "${packageManager.getInstallSourceInfo(packageInfo.packageName).initiatingPackageName} \n" +
                                        "${packageManager.getInstallSourceInfo(packageInfo.packageName).originatingPackageName}"
                            } else {
                                packageManager.getInstallerPackageName(packageInfo.packageName)
                            }
                        } catch (e: PackageManager.NameNotFoundException) {
                            e.printStackTrace()
                        }

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