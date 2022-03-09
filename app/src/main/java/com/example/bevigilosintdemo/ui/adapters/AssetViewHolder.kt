package com.example.bevigilosintdemo.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.databinding.LayoutAssetItemBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource

class AssetViewHolder(
    private val binding: LayoutAssetItemBinding,
    private val assetListListener: AssetClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(assetModel: AssetModel, position: Int) {
        val assetKey = assetModel.getKeyAt(position)
        val assetTitle = assetModel.getTitleStringForAsset(assetKey)
        binding.titleText.text = assetTitle
        binding.contentText.text = assetModel.getAssetListString(assetKey)
        if (assetModel.getAssetCountAt(assetKey) > 5) {
            binding.showAllLabel.text = getStringResource(R.string.view_all_label).replace("{asset}", assetTitle)
            binding.bottomFadeView.visibility = View.VISIBLE
            binding.bottomFadeView.setOnClickListener {
                assetModel.packageID?.let { packageID ->
                    assetListListener.viewMoreClicked(assetKey, packageID)
                }
            }
        } else {
            binding.bottomFadeView.visibility = View.GONE
        }
    }
}