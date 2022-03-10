package com.example.bevigilosintdemo.ui.adapters

import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bevigilosintdemo.api.model.App
import com.example.bevigilosintdemo.databinding.LayoutDeviceAppItemBinding

class AppListAdapter(var appList: ArrayList<App>, private val appClickListener: AppClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DeviceAppViewHolder(LayoutDeviceAppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), appClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is DeviceAppViewHolder -> holder.bindTo(appList[position])
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }
}

interface AppClickListener {
    fun onAppClicked(packageID: String)
}