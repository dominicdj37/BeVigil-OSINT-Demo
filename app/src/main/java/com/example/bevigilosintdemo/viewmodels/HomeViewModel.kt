package com.example.bevigilosintdemo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.api.repository.OSINTRepository

class HomeViewModel : ViewModel() {

    var assetsMap: HashMap<String,AssetModel?> = hashMapOf()

    fun getAllAssets(packageID: String): MutableLiveData<ApiResponse> {
        return OSINTRepository.getInstance().getAllAssets(packageID)
    }
}