package com.example.bevigilosintdemo.ui.activities

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bevigilosintdemo.*
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.AssetModel
import com.example.bevigilosintdemo.api.model.SearchHistory
import com.example.bevigilosintdemo.api.repository.FireStoreRepository
import com.example.bevigilosintdemo.core.Constants.ASSET_TYPE_KEY
import com.example.bevigilosintdemo.ui.adapters.AssetClickListener
import com.example.bevigilosintdemo.ui.adapters.AssetsAdapter
import com.example.bevigilosintdemo.ui.custom.RecentLabelItem
import com.example.bevigilosintdemo.ui.fragments.AssetDetailsBottomSheetFragment
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import com.example.bevigilosintdemo.viewmodels.HomeViewModel
import kotlinx.coroutines.*

import com.example.bevigilosintdemo.core.SessionRepo
import com.example.bevigilosintdemo.databinding.ActivityHomeDrawerBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource
import com.firebase.ui.auth.AuthUI


@DelicateCoroutinesApi
class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeDrawerBinding
    var assetAdapter: AssetsAdapter? = null
    val viewModel: HomeViewModel by viewModels()
    var isSearchLayoutExpanded = false

    //region lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityHomeDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeToolbarAndNavigation()
        initializeMotionLayout()
        initializeAssetsList()
        refreshRecentItems()
        initializeSearch()

        getDeviceApps()
    }
    //endregion


    //region UI init
    private fun initializeAssetsList() {
        assetAdapter = AssetsAdapter(AssetModel(), assetListListener)
        binding.content.assetsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.content.assetsRecyclerView.adapter = assetAdapter
    }

    private val assetListListener = object :AssetClickListener {
        override fun viewMoreClicked(assetKey: String, packageID: String) {
            SessionRepo.selectedAsset = viewModel.assetsMap[packageID]
            val detailsBottomSheet = AssetDetailsBottomSheetFragment()
            detailsBottomSheet.arguments = Bundle().apply {
                putString(ASSET_TYPE_KEY, assetKey)
            }
            detailsBottomSheet.show(supportFragmentManager, detailsBottomSheet.tag)
        }
    }

    private fun initializeMotionLayout() {
        binding.content.homeParent.setTransitionListener(object: MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                if(p1 == R.id.collapsedSet) {
                    initializeAssetsList()
                    setCollapsedLayoutState()
                } else if(p1 == R.id.expandedSet) {
                    setExpandedLayoutState()
                }
            }
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
        })
    }

    private fun setExpandedLayoutState() {
        isSearchLayoutExpanded = true
        binding.content.searchInputEditText.visibility = View.VISIBLE
    }

    private fun setCollapsedLayoutState() {
        isSearchLayoutExpanded = false
        binding.content.searchInputEditText.visibility = View.GONE
        binding.content.searchInputEditText.hideKeyBoardAndRemoveFocus()
    }

    private fun initializeToolbarAndNavigation() {
        binding.navContent.userImageView.assignImageFromUrl(SessionRepo.user?.photoUrl.toString(),true, isCircleCrop = true)
        binding.navContent.userNameTextView.text = SessionRepo.user?.displayName
        binding.navContent.userEmailTextView.text = SessionRepo.user?.email
        binding.content.toolbarLayout.setup(getStringResource(R.string.home_page_title), leftIcon = getDrawableResource(R.drawable.ic_nav), leftClick = {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        })

        binding.navContent.signOutButton.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            signOut()
        }
        binding.content.statusBarReference.applyTopWindowInsetToMargin()
    }
    //endregion


    //region recent items
    private fun refreshRecentItems() {
        FireStoreRepository.getInstance().getSearchHistory({ searchHistory ->
            updateRecentItemsView(searchHistory)
        }) {}
    }

    private fun updateRecentItemsView(searchHistory: SearchHistory) {
        binding.content.recentFlexBoxLayout.removeAllViews()
        if(searchHistory.packageNames.isEmpty()) {
            binding.content.recentFlexBoxLayout.addView(RecentLabelItem(this).setRecentItem(getStringResource(R.string.no_recent_items_text), invertColor = true))
        } else {
            binding.content.recentFlexBoxLayout.addView(RecentLabelItem(this).setRecentItem(getStringResource(R.string.recent_items_text), invertColor = true))
            searchHistory.packageNames.forEach { searchKeys ->
                binding.content.recentFlexBoxLayout.addView(
                    RecentLabelItem(this).setRecentItem(searchKeys, onClick = { recentString ->
                        onRecentItemClicked(recentString)
                    })
                )
            }
            addClearRecentItems()
        }
    }

    private fun addClearRecentItems() {
        binding.content.recentFlexBoxLayout.addView(
            RecentLabelItem(this).setRecentItem(getStringResource(R.string.clear_recent_items_text), invertColor = true, onClick = {
                FireStoreRepository.getInstance().removePackagesFromSearchHistory({
                    refreshRecentItems()
                }) {}
            })
        )
    }

    private fun onRecentItemClicked(recentString: String) {
        binding.content.homeParent.transitionToEnd()
        isSearchLayoutExpanded = true
        binding.content.searchInputEditText.setText(recentString)
        getAllAssetsFor(recentString)
    }

    private fun addToSearchHistory(inputString: String) {
        FireStoreRepository.getInstance().createSearchHistoryDataIfNotExist({
            FireStoreRepository.getInstance().addSearchString(inputString, {}) {}
        }) {}
    }
    //endregion


    //region device apps info
    private fun getDeviceApps() {
        binding.content.deviceAppsInfoView.setup(packageManager, onDeviceSearchClick =  {
            navigateToAppListActivity()
        }, onDomainSearchClick = {
            navigateToAppListActivity(isDomainSearch = true)
        })
    }
    //endregion


    //region search
    private fun initializeSearch() {
        binding.content.searchButton.setOnClickListener {
            if (isSearchLayoutExpanded) {
                binding.content.searchInputEditText.hideKeyBoardAndRemoveFocus()
                binding.content.searchInputEditText.text?.trim()?.takeIf { it.isNotBlank() }?.let { inputString ->
                    getAllAssetsFor(inputString.toString())
                }
            } else {
                binding.content.homeParent.transitionToEnd()
                isSearchLayoutExpanded = true
                binding.content.searchInputEditText.showKeyBoardAndFocus()
            }
        }

        binding.content.searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    binding.content.searchButton.callOnClick()
                    true
                }
                else -> true
            }
        }
    }

    private fun getAllAssetsFor(inputString: String) {
        showLoading(binding.content.progressBarLayout)
        addToSearchHistory(inputString)
        viewModel.assetsMap[inputString] = null
        viewModel.getAllAssets(inputString).observe(this, object : Observer<ApiResponse> {
            override fun onChanged(response: ApiResponse?) {
                hideLoading(binding.content.progressBarLayout)
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
    //endregion

    //region sign out
    private fun signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener {
            SessionRepo.clearData()
            navigateToSplashScreen()
        }
    }
    //endregion
}