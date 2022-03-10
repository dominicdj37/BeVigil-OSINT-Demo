package com.example.bevigilosintdemo.ui.activities

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.applyTopWindowInsetToMargin
import com.example.bevigilosintdemo.core.Constants.ASSET_TYPE_KEY
import com.example.bevigilosintdemo.databinding.ActivityHomeBinding
import com.example.bevigilosintdemo.hideKeyBoardAndRemoveFocus
import com.example.bevigilosintdemo.showKeyBoardAndFocus
import com.example.bevigilosintdemo.ui.adapters.AssetClickListener
import com.example.bevigilosintdemo.ui.adapters.AssetsAdapter
import com.example.bevigilosintdemo.ui.custom.RecentLabelItem
import com.example.bevigilosintdemo.ui.fragments.AssetDetailsBottomSheetFragment
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.example.bevigilosintdemo.viewmodels.HomeViewModel
import kotlinx.coroutines.*

import com.example.bevigilosintdemo.core.Repo
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource


@DelicateCoroutinesApi
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
        refreshRecentItems()
        initializeSearch()

        getDeviceApps()
    }

    private fun getDeviceApps() {
        binding.deviceAppsInfoView.setup(packageManager, onDeviceSearchClick =  {
            navigateToAppListActivity()
        }, onDomainSearchClick = {
            navigateToAppListActivity(isDomainSearch = true)
        })
    }




    private fun refreshRecentItems() {
        binding.recentFlexBoxLayout.removeAllViews()
        if(viewModel.assetsMap.isEmpty()) {
            binding.recentFlexBoxLayout.addView(RecentLabelItem(this).setRecentItem(getStringResource(R.string.no_recent_items_text), invertColor = true))
        } else {
            binding.recentFlexBoxLayout.addView(RecentLabelItem(this).setRecentItem(getStringResource(R.string.recent_items_text), invertColor = true))
            viewModel.assetsMap.keys.forEach { searchKeys ->
                binding.recentFlexBoxLayout.addView(
                    RecentLabelItem(this).setRecentItem(searchKeys, onClick = { recentString ->
                        onRecentItemClicked(recentString)
                    })
                )
            }
            binding.recentFlexBoxLayout.addView(
                RecentLabelItem(this).setRecentItem(getStringResource(R.string.clear_recent_items_text), invertColor = true, onClick = {
                    viewModel.clearAllRecentItems()
                    refreshRecentItems()
                })
            )
        }
    }

    private fun onRecentItemClicked(recentString: String) {
        binding.homeParent.transitionToEnd()
        isSearchLayoutExpanded = true
        binding.searchInputEditText.setText(recentString)
        getAllAssetsFor(recentString)
    }

    private val assetListListener = object :AssetClickListener {
        override fun viewMoreClicked(assetKey: String, packageID: String) {
            Repo.selectedAsset = viewModel.assetsMap[packageID]
            val detailsBottomSheet = AssetDetailsBottomSheetFragment()
            detailsBottomSheet.arguments = Bundle().apply {
                putString(ASSET_TYPE_KEY, assetKey)
            }
            detailsBottomSheet.show(supportFragmentManager, detailsBottomSheet.tag)
        }
    }

    private fun initializeAssetsList() {
        assetAdapter = AssetsAdapter(AssetModel(), assetListListener)
        binding.assetsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.assetsRecyclerView.adapter = assetAdapter
    }

    private fun initializeMotionLayout() {
        binding.homeParent.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if(p1 == R.id.collapsedSet) {
                    initializeAssetsList()
                    isSearchLayoutExpanded = false
                    binding.searchInputEditText.visibility = View.GONE
                    binding.searchInputEditText.hideKeyBoardAndRemoveFocus()
                } else if(p1 == R.id.expandedSet) {
                    isSearchLayoutExpanded = true
                    binding.searchInputEditText.visibility = View.VISIBLE
                }
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    private fun initializeToolbar() {
        binding.toolbarLayout.setup(getStringResource(R.string.home_page_title), leftClick = {
            //todo add side menu
        })
        binding.statusBarReference.applyTopWindowInsetToMargin()
    }

    private fun initializeSearch() {
        binding.searchButton.setOnClickListener {
            if (isSearchLayoutExpanded) {
                binding.searchInputEditText.hideKeyBoardAndRemoveFocus()
                binding.searchInputEditText.text?.trim()?.takeIf { it.isNotBlank() }?.let { inputString ->
                    getAllAssetsFor(inputString.toString())
                }
            } else {
                binding.homeParent.transitionToEnd()
                isSearchLayoutExpanded = true
                binding.searchInputEditText.showKeyBoardAndFocus()
            }
        }

        binding.searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    binding.searchButton.callOnClick()
                    true
                }
                else -> true
            }
        }
    }

    private fun getAllAssetsFor(inputString: String) {
        showLoading(binding.progressBarLayout)
        viewModel.assetsMap[inputString] = null
        viewModel.getAllAssets(inputString).observe(this, object : Observer<ApiResponse> {
            override fun onChanged(response: ApiResponse?) {
                hideLoading(binding.progressBarLayout)
                if (response?.data != null) {
                    viewModel.assetsMap[inputString] = response.data as AssetModel
                    assetAdapter?.assetModel = response.data as AssetModel
                    assetAdapter?.assetModel?.apiCalled = true
                    assetAdapter?.notifyDataSetChanged()
                } else {
                    initializeAssetsList()
                    handleError(response?.error)
                }
                refreshRecentItems()
            }
        })
    }
}