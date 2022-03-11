package com.example.bevigilosintdemo.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.core.Constants
import com.example.bevigilosintdemo.core.SessionRepo
import com.example.bevigilosintdemo.databinding.ActivityAssetDetailsBinding
import com.example.bevigilosintdemo.ui.adapters.AssetClickListener
import com.example.bevigilosintdemo.ui.adapters.AssetsAdapter
import com.example.bevigilosintdemo.ui.fragments.AssetDetailsBottomSheetFragment
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.example.bevigilosintdemo.viewmodels.AssetDetailsViewModel

class AssetDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityAssetDetailsBinding
    private val viewModel: AssetDetailsViewModel  by viewModels()
    var assetAdapter: AssetsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssetDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.packageID = intent.getStringExtra(Constants.PACKAGE_NAME_KEY) ?: ""

        initializeToolbar()
        initializeAssetList()
        getAllAssetsFor(viewModel.packageID)
    }

    private fun initializeAssetList() {
        viewModel.asset = AssetModel()
        viewModel.fetchAppDetailsIfAvailable(packageManager)
        assetAdapter = AssetsAdapter(viewModel.asset, assetListListener)
        binding.assetsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.assetsRecyclerView.adapter = assetAdapter
    }

    private val assetListListener = object : AssetClickListener {
        override fun viewMoreClicked(assetKey: String, packageID: String) {
            SessionRepo.selectedAsset = viewModel.asset
            val detailsBottomSheet = AssetDetailsBottomSheetFragment()
            detailsBottomSheet.arguments = Bundle().apply {
                putString(Constants.ASSET_TYPE_KEY, assetKey)
            }
            detailsBottomSheet.show(supportFragmentManager, detailsBottomSheet.tag)
        }
    }

    private fun getAllAssetsFor(inputString: String) {
        showLoading(binding.progressBarLayout)
        viewModel.getAllAssets(inputString).observe(this, object : Observer<ApiResponse> {
            override fun onChanged(response: ApiResponse?) {
                hideLoading(binding.progressBarLayout)
                if (response?.data != null) {
                    viewModel.asset = response.data as AssetModel
                    viewModel.asset.apiCalled = true
                    viewModel.fetchAppDetailsIfAvailable(packageManager)
                    assetAdapter?.assetModel = viewModel.asset
                    assetAdapter?.notifyDataSetChanged()
                } else {
                    initializeAssetList()
                    handleError(response?.error)
                }
            }
        })
    }

    private fun initializeToolbar() {
        binding.toolbarLayout.setup(getStringResource(R.string.all_assets), getDrawableResource(R.drawable.ic_left), leftClick = {
            onBackPressed()
        })
    }


}