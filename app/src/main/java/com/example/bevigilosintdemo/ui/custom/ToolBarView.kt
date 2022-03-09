package com.example.bevigilosintdemo.ui.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bevigilosintdemo.databinding.ToolbarLayoutBinding

class ToolBarView : ConstraintLayout {

    lateinit var binding: ToolbarLayoutBinding

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
        binding = ToolbarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setup(title: String, leftIcon: Drawable? = null, rightIcon: Drawable? = null,
              leftClick: (()->Unit)? = null, rightClick: (()->Unit)? = null) {
        binding.titleText.text = title
        binding.leftIcon.setImageDrawable(leftIcon)
        binding.rightIcon.setImageDrawable(rightIcon)
        binding.leftIcon.setOnClickListener {
            leftClick?.invoke()
        }
        binding.rightIcon.setOnClickListener {
            rightClick?.invoke()
        }
    }
}
