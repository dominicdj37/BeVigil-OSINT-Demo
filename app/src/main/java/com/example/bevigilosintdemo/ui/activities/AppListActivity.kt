package com.example.bevigilosintdemo.ui.activities

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.Apps
import com.example.bevigilosintdemo.core.Constants
import com.example.bevigilosintdemo.core.Constants.NAME_TAG
import com.example.bevigilosintdemo.databinding.ActivityDeviceAppListBinding
import com.example.bevigilosintdemo.hideKeyBoardAndRemoveFocus
import com.example.bevigilosintdemo.showKeyBoardAndFocus
import com.example.bevigilosintdemo.ui.adapters.AppClickListener
import com.example.bevigilosintdemo.ui.adapters.AppListAdapter
import com.example.bevigilosintdemo.services.MyNotificationListenerService
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.example.bevigilosintdemo.viewmodels.DeviceAppListViewModel
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class AppListActivity : BaseActivity() {

    lateinit var binding: ActivityDeviceAppListBinding
    private var adapter: AppListAdapter? = null
    private val viewModel: DeviceAppListViewModel by viewModels()

    //region lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceAppListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.isDomainSearchMode = intent.getBooleanExtra(Constants.SEARCH_DOMAIN, false)

        initializeUI()
        startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        MyNotificationListenerService.startService(this, "")
    }
    //endregion


    //region UI initialize
    private fun initializeUI() {
        binding.toolbarLayout.setup(getStringResource(R.string.my_apps), getDrawableResource(R.drawable.ic_left), leftClick = {
            onBackPressed()
        })

        adapter = AppListAdapter(viewModel.displayingAppList, onAppListClickListener)
        binding.appListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.appListRecyclerView.adapter = adapter

        if(viewModel.isDomainSearchMode) {
            initializeUIForDomainSearch()
        } else {
            initializeUIForDeviceAppList()
        }
    }

    private val onAppListClickListener = object: AppClickListener {
        override fun onAppClicked(packageID: String) {
            navigateToAssetDetailsActivity(packageID)
        }
    }

    private fun initializeUIForDeviceAppList() {
        binding.searchTextInputContainer.visibility = View.GONE
        binding.searchHintText.visibility = View.GONE
        loadDeviceAppsList()
    }

    private fun initializeUIForDomainSearch() {
        binding.searchTextInputContainer.visibility = View.VISIBLE
        binding.searchHintText.visibility = View.VISIBLE
        binding.appsFoundLabel.text = ""
        binding.hintTextView.text = ""
        binding.collapsedAppCountText.text = ""
        binding.searchInputEditText.showKeyBoardAndFocus()
        binding.searchButton.setOnClickListener {
            binding.searchInputEditText.text?.takeIf { it.isNotBlank() }?.let { domainName ->
                binding.searchInputEditText.hideKeyBoardAndRemoveFocus()
                searchAppByDomainName(domainName.toString())
            }
        }
        binding.searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        binding.searchButton.callOnClick()
                        true
                    }
                    else -> true
                }
            }
    }

    private fun loadSearchedAppList() {
        binding.appCountText.text = viewModel.displayingAppList.size.toString()
        binding.appTotalCountText.text = getStringResource(R.string.apps)
        binding.appsFoundLabel.text = getStringResource(R.string.apps_associated_with_text).replace(NAME_TAG, viewModel.apps?.domain ?: "")
        adapter?.notifyDataSetChanged()
    }

    private fun showSystemApps(packages: List<PackageInfo>) {
        showLoading(binding.progressBarLayout)
        viewModel.showingInstalledApps = false
        binding.showAppsLabel.text = getStringResource(R.string.show_installed_apps)
        binding.appsFoundLabel.text = getStringResource(R.string.system_apps_found)
        packages.filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 }
            .let { systemPackages ->
                binding.appCountText.text = systemPackages.size.toString()
                binding.collapsedAppCountText.text =
                    "${systemPackages.size} ${getStringResource(R.string.apps)}"
                viewModel.displayingAppList.clear()
                viewModel.getAppListFromPackages(systemPackages, packageManager, this) { returnList ->
                    viewModel.displayingAppList.addAll(returnList)
                    adapter?.notifyDataSetChanged()
                    hideLoading(binding.progressBarLayout)
                }
            }
    }

    private fun showInstalledApps(packages: List<PackageInfo>) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                showLoading(binding.progressBarLayout)
                viewModel.showingInstalledApps = true
                binding.showAppsLabel.text = getStringResource(R.string.show_system_apps)
                binding.appsFoundLabel.text = getStringResource(R.string.installed_apps_found)
                packages.filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }
                    .let { installedPackages ->
                        binding.collapsedAppCountText.text =
                            "${installedPackages.size} ${getStringResource(R.string.apps)}"
                        binding.appCountText.text = installedPackages.size.toString()
                        viewModel.displayingAppList.clear()
                        viewModel.getAppListFromPackages(installedPackages, packageManager, this@AppListActivity) { returnList ->
                            viewModel.displayingAppList.addAll(returnList)
                            adapter?.notifyDataSetChanged()
                            hideLoading(binding.progressBarLayout)
                        }
                    }
            }
        }
    }
    //endregion


    //region data population
    private fun searchAppByDomainName(domainName: String) {
        showLoading(binding.progressBarLayout)
        viewModel.getApps(domainName).observe(this, object : Observer<ApiResponse> {
            override fun onChanged(response: ApiResponse?) {
                if (response?.data != null) {
                    viewModel.apps = response.data as Apps
                    viewModel.updateAppList()
                    loadSearchedAppList()
                    hideLoading(binding.progressBarLayout)
                } else {
                    handleError(response?.error)
                }
            }
        })
    }

    private fun loadDeviceAppsList() {
        showLoading(binding.progressBarLayout)
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
                binding.hintTextView.text = getStringResource(R.string.click_app_hint)
                binding.appTotalCountText.text = "/${packages.size}"
                showInstalledApps(packages)
                binding.showAppsLabel.setOnClickListener {
                    if(viewModel.showingInstalledApps) {
                        showSystemApps(packages)
                    } else {
                        showInstalledApps(packages)
                    }
                }

                delay(3000)
                hideLoading(binding.progressBarLayout)
            }
        }
    }
    //endregion
}