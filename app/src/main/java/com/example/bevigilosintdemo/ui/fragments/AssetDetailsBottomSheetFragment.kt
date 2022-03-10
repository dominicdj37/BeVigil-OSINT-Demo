package com.example.bevigilosintdemo.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.activityViewModels
import com.example.bevigilosintdemo.core.Constants
import com.example.bevigilosintdemo.core.Repo
import com.example.bevigilosintdemo.databinding.LayoutAssetsDetailsBottomSheetBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getColorResource
import com.example.bevigilosintdemo.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AssetDetailsBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutAssetsDetailsBottomSheetBinding

    //region lifecycle callbacks
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = LayoutAssetsDetailsBottomSheetBinding.inflate(LayoutInflater.from(context), null, false)
        bottomSheet.setContentView(binding.root)
        setupBottomSheetLayout(binding)
        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        arguments?.get(Constants.ASSET_TYPE_KEY).takeIf { it is String }?.let { assetKey ->
            showDetailedAsset(assetKey as String)
        }
    }
    //endregion

    //region UI setup
    private fun setupBottomSheetLayout(binding: LayoutAssetsDetailsBottomSheetBinding) {
        val frameLayout = binding.root.parent as FrameLayout
        val params = frameLayout.layoutParams as CoordinatorLayout.LayoutParams
        params.height = CoordinatorLayout.LayoutParams.MATCH_PARENT
        getColorResource(android.R.color.transparent)?.let { frameLayout.setBackgroundColor(it) }
        binding.closeButton.setOnClickListener {
            this.dismiss()
        }
    }

    private fun showDetailedAsset(assetKey: String) {
        binding.titleText.text = Repo.selectedAsset?.getTitleStringForAsset(assetKey)
        binding.subtitleText.text = Repo.selectedAsset?.packageID
        Repo.selectedAsset?.getAssetListString(assetKey)?.let {
            binding.detailedText.text = it
        }
    }
    //endregion
}