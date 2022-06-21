package com.example.bevigilosintdemo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.databinding.LayoutAssetItemBinding
import com.example.bevigilosintdemo.databinding.LayoutAssetSearchDetailsItemBinding
import com.example.bevigilosintdemo.databinding.LayoutAssetsInfoLayoutBinding

class AssetsAdapter(var assetModel: AssetModel, private val assetListListener: AssetClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.layout_asset_item ->
                AssetViewHolder(LayoutAssetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), assetListListener)
            R.layout.layout_asset_search_details_item ->
                AssetsSearchDetailsViewHolder(LayoutAssetSearchDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.layout_assets_info_layout ->
                AssetsInfoViewHolder(LayoutAssetsInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else ->
                AssetsSearchDetailsViewHolder(LayoutAssetSearchDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (assetModel.isEmpty()) {
            R.layout.layout_asset_search_details_item
        } else {
            if(position == 0) {
                R.layout.layout_assets_info_layout
            } else {
                R.layout.layout_asset_item
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AssetViewHolder -> holder.bindTo(assetModel, position - 1)
            is AssetsInfoViewHolder -> holder.bindTo(assetModel)
            is AssetsSearchDetailsViewHolder -> holder.bindTo(assetModel)
        }
    }

    override fun getItemCount(): Int {
        return if(assetModel.isEmpty()) {
            1
        } else {
            assetModel.getAssetsCount() + 1
        }
    }
}


interface AssetClickListener {
    fun viewMoreClicked(assetKey: String, packageID: String)
}