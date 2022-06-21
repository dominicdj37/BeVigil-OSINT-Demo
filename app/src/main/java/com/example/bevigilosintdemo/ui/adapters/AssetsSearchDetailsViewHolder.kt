package com.example.bevigilosintdemo.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.databinding.LayoutAssetSearchDetailsItemBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource

class AssetsSearchDetailsViewHolder(private val binding: LayoutAssetSearchDetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(assetModel: AssetModel) {
        if(assetModel.apiCalled) {
            binding.titleText.text = getStringResource(R.string.whoops)
            binding.messageText.text = assetModel.detail ?: getStringResource(R.string.nothing_here_text)
        } else {
            binding.titleText.text = getStringResource(R.string.search_hint_title)
            binding.messageText.text = getStringResource(R.string.search_package_id_hint)
        }
    }
}