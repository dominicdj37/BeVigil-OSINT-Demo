package com.example.bevigilosintdemo.ui.adapters

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.App
import com.example.bevigilosintdemo.databinding.LayoutDeviceAppItemBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource

class DeviceAppViewHolder(
    private val binding: LayoutDeviceAppItemBinding,
    private val assetListListener: AppClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(app: App) {
        binding.appImageView.setImageDrawable(app.icon ?: getDrawableResource(R.drawable.ic_android_app))
        binding.appTitleText.text = app.name
        binding.appPackageNameText.text = app.packageID
        binding.appVersion.text = app.version
        binding.root.setOnClickListener { assetListListener.onAppClicked(app.packageID ?: "") }
    }
}