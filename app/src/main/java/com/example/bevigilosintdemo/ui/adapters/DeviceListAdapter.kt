package com.example.bevigilosintdemo.ui.adapters

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.databinding.LayoutDeviceAppItemBinding

class DeviceListAdapter(var packageList: ArrayList<PackageInfo>, val packageManager: PackageManager, private val appClickListener: AppClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceAppViewHolder(LayoutDeviceAppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), appClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is DeviceAppViewHolder -> holder.bindTo(packageList[position], packageManager)
        }
    }

    override fun getItemCount(): Int {
        return packageList.size
    }
}

interface AppClickListener {
    fun onAppClicked(packageID: String)
}