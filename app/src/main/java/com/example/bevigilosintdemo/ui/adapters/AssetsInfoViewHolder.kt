package com.example.bevigilosintdemo.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.core.Constants.COUNT_TAG
import com.example.bevigilosintdemo.databinding.LayoutAssetsInfoLayoutBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource

class AssetsInfoViewHolder(private val binding: LayoutAssetsInfoLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(assetModel: AssetModel) {
        assetModel.appName?.let {
            binding.appName.text = it
            binding.appPackageName.text = assetModel.packageID
        } ?: let {
            binding.appName.text = assetModel.packageID
        }

        binding.appIconImage.setImageDrawable(assetModel.appIcon ?: getDrawableResource(R.drawable.ic_android_app))
        binding.assetsCountText.text = getStringResource(R.string.assets_found_count).replace(COUNT_TAG, assetModel.assets.size.toString())
        binding.pieChart.setupChart(assetModel)
    }

}