package com.example.bevigilosintdemo.ui.adapters

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.databinding.LayoutDeviceAppItemBinding

class DeviceAppViewHolder(
    private val binding: LayoutDeviceAppItemBinding,
    private val assetListListener: AppClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(packageInfo: PackageInfo, packageManager: PackageManager) {
        binding.appImageView.setImageDrawable(packageInfo.applicationInfo.loadIcon(packageManager))
        binding.appTitleText.text = packageInfo.applicationInfo.loadLabel(packageManager)
        binding.appPackageNameText.text = packageInfo.packageName
        binding.root.setOnClickListener { assetListListener.onAppClicked(packageInfo.packageName) }
    }
}