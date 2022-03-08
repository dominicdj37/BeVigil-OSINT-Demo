package com.example.bevigilosintdemo.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.applyTopWindowInsetToMargin
import com.example.bevigilosintdemo.databinding.ActivityHomeBinding
import com.example.bevigilosintdemo.ui.adapters.AssetsAdapter
import com.example.bevigilosintdemo.viewmodels.HomeViewModel

class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding
    var assetAdapter: AssetsAdapter? = null
    val viewModel: HomeViewModel by viewModels()
    var isSearchLayoutExpanded = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeToolbar()
        initializeMotionLayout()
        initializeAssetsList()
        initializeSearch()
    }

    private fun initializeAssetsList() {

    }

    private fun initializeMotionLayout() {
        binding.homeParent.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if(p1 == R.id.collapsedSet) {
                    isSearchLayoutExpanded = false
                    binding.searchInputEditText.isEnabled = false
                } else if(p1 == R.id.expandedSet) {
                    isSearchLayoutExpanded = true
                    binding.searchInputEditText.isEnabled = true
                }
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    private fun initializeToolbar() {
        binding.statusBarReference.applyTopWindowInsetToMargin()
    }

    private fun initializeSearch() {
        binding.searchButton.setOnClickListener {
            if (isSearchLayoutExpanded) {
                getAllAssets()
            } else {
                binding.homeParent.transitionToEnd()
                isSearchLayoutExpanded = true
                binding.searchInputEditText.requestFocus()
                openSoftKeyboard(binding.searchInputEditText)
            }
        }
    }

    private fun getAllAssets() {
        binding.searchInputEditText.text?.trim()?.takeIf { it.isNotBlank() }?.let { inputString ->
            viewModel.getAllAssets(inputString.toString()).observe(this, object : Observer<ApiResponse> {
                override fun onChanged(t: ApiResponse?) {
                    if(t?.data != null) {
                        viewModel.assetsMap[inputString.toString()] = t.data as AssetModel
                        assetAdapter = AssetsAdapter(t.data as AssetModel)
                        binding.assetsRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
                        binding.assetsRecyclerView.adapter = assetAdapter
                    } else {

                    }
                }
            })
        }
    }
}