package com.example.bevigilosintdemo.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.applyTopWindowInsetToMargin
import com.example.bevigilosintdemo.databinding.ActivityHomeBinding
import com.example.bevigilosintdemo.viewmodels.HomeViewModel

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding
    val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeToolbar()
        initializeSearch()
    }

    private fun initializeToolbar() {
        binding.statusBarReference.applyTopWindowInsetToMargin()
    }

    private fun initializeSearch() {
        binding.searchButton.setOnClickListener {
            binding.searchInputEditText.text?.trim()?.takeIf { it.isNotBlank() }?.let { inputString ->
                viewModel.getAllAssets(inputString.toString()).observe(this, object : Observer<ApiResponse> {
                    override fun onChanged(t: ApiResponse?) {
                        if(t?.isSuccess == true) {
                            viewModel.assetsMap[inputString.toString()] = t.data as AssetModel
                        } else {

                        }
                    }
                })
            }
        }
    }
}