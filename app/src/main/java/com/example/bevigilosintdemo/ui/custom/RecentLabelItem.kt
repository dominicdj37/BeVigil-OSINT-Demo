package com.example.bevigilosintdemo.ui.custom

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.databinding.LayoutRecentItemLabelBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getColorResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource

class RecentLabelItem : ConstraintLayout {

    lateinit var binding: LayoutRecentItemLabelBinding

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
        binding = LayoutRecentItemLabelBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setRecentItem(itemString: String, invertColor: Boolean = false, onClick: ((String)->Unit)? = null): RecentLabelItem {
        if(invertColor) {
            binding.backgroundView.background = getDrawableResource(R.drawable.bg_recent_text_fill)
            getColorResource(R.color.white)?.let { binding.itemText.setTextColor(it) }
        }
        binding.itemText.text = itemString
        binding.root.setOnClickListener {
            onClick?.invoke(itemString)
        }
        return this
    }

}