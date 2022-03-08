package com.example.bevigilosintdemo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.databinding.EmptyAssetsItemBinding
import com.example.bevigilosintdemo.databinding.LayoutAssetItemBinding

class AssetsAdapter(private val assetModel: AssetModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.layout_asset_item ->
                AssetViewHolder(LayoutAssetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.empty_assets_item ->
                AssetsEmptyViewHolder(EmptyAssetsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else ->
                AssetsEmptyViewHolder(EmptyAssetsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (assetModel.isEmpty()) {
            R.layout.empty_assets_item
        } else {
            R.layout.layout_asset_item
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AssetViewHolder -> holder.bindTo(assetModel, position)
            is AssetsEmptyViewHolder -> holder.bindTo()
        }
    }

    override fun getItemCount(): Int {
        return if(assetModel.isEmpty()) {
            1
        } else {
            assetModel.getAssetsCount()
        }
    }
}