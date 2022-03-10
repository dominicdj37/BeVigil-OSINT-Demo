package com.example.bevigilosintdemo.ui.activities

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.databinding.ActivityDeviceAppListBinding
import com.example.bevigilosintdemo.ui.adapters.AppClickListener
import com.example.bevigilosintdemo.ui.adapters.DeviceListAdapter
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.example.bevigilosintdemo.viewmodels.DeviceAppListViewModel
import kotlinx.coroutines.*

class DeviceAppListActivity : BaseActivity() {
    lateinit var binding: ActivityDeviceAppListBinding
    private var adapter: DeviceListAdapter? = null
    private val viewModel: DeviceAppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeUI()
        loadAppsList()
    }

    private val onAppClickListener = object: AppClickListener {
        override fun onAppClicked(packageID: String) {
            //todo open assets
        }
    }

    private fun initializeUI() {
        binding.toolbarLayout.setup(getStringResource(R.string.my_apps), getDrawableResource(R.drawable.ic_left), leftClick = {
            onBackPressed()
        })

        adapter = DeviceListAdapter(viewModel.deviceList, packageManager, onAppClickListener)
        binding.appListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.appListRecyclerView.adapter = adapter
    }

    private fun loadAppsList() {
        showLoading(binding.progressBarLayout)
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
                populateData(packages)
                delay(3000)
                hideLoading(binding.progressBarLayout)
            }
        }
    }

    private fun populateData(packages: List<PackageInfo>) {
        binding.appTotalCountText.text = "/${packages.size}"
        showInstalledApps(packages)
        binding.showAppsLabel.setOnClickListener {
            if(viewModel.showingInstalledApps) {
                showSystemApps(packages)
            } else {
                showInstalledApps(packages)
            }
        }
    }

    private fun showSystemApps(packages: List<PackageInfo>) {
        viewModel.showingInstalledApps = false
        binding.showAppsLabel.text = getStringResource(R.string.show_installed_apps)
        binding.appsFoundLabel.text = getStringResource(R.string.system_apps_found)
        packages.filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 }.let { systemPackages ->
            binding.appCountText.text = systemPackages.size.toString()
            binding.collapsedAppCountText.text = "${systemPackages.size} ${getStringResource(R.string.apps)}"
            viewModel.deviceList.clear()
            viewModel.deviceList.addAll(systemPackages)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun showInstalledApps(packages: List<PackageInfo>) {
        viewModel.showingInstalledApps = true
        binding.showAppsLabel.text = getStringResource(R.string.show_system_apps)
        binding.appsFoundLabel.text = getStringResource(R.string.installed_apps_found)
        packages.filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.let { installedPackages ->
            binding.collapsedAppCountText.text = "${installedPackages.size} ${getStringResource(R.string.apps)}"
            binding.appCountText.text = installedPackages.size.toString()
            viewModel.deviceList.clear()
            viewModel.deviceList.addAll(installedPackages)
            adapter?.notifyDataSetChanged()
        }
    }
}