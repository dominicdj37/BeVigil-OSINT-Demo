package com.example.bevigilosintdemo.ui.activities

import android.content.pm.ApplicationInfo
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
                viewModel.deviceList.clear()
                viewModel.deviceList.addAll(packages)
                adapter?.notifyDataSetChanged()
                delay(3000)
                hideLoading(binding.progressBarLayout)
            }
        }
    }
}