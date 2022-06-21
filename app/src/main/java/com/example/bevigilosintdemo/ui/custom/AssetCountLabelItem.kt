package com.example.bevigilosintdemo.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.core.AssetKeyType.getColorForAssetKey
import com.example.bevigilosintdemo.databinding.LayoutAssetCountLabelItemBinding
import com.example.bevigilosintdemo.databinding.LayoutRecentItemLabelBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getColorResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource

class AssetCountLabelItem : ConstraintLayout {

    lateinit var binding: LayoutAssetCountLabelItemBinding

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        binding = LayoutAssetCountLabelItemBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setCountAndColor(label: String, color: Int? = getColorResource(R.color.black), onClick: ((String)->Unit)? = null): AssetCountLabelItem {
        color?.let { binding.assetPieColor.setBackgroundColor(it) }
        binding.assetsCountText.text = label
        return this
    }

}