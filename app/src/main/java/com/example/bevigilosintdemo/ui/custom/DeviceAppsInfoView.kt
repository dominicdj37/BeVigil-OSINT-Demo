package com.example.bevigilosintdemo.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.databinding.LayoutDeviceAppsInfoBinding
import com.example.bevigilosintdemo.databinding.LayoutRecentItemLabelBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getColorResource
import com.example.bevigilosintdemo.utils.ResourceUtils.getDrawableResource
import kotlinx.coroutines.*

class DeviceAppsInfoView : ConstraintLayout {

    lateinit var binding: LayoutDeviceAppsInfoBinding

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
        binding = LayoutDeviceAppsInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setup(packageManager: PackageManager, onClick: ()->Unit = {} ){
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        animateNumber(binding.appCountText, packages.size)
        packages.filter { packageInfo -> (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 }.let { installedApps ->
            animateNumber(binding.nonSystemAppsText, installedApps.size)
            animateNumber(binding.systemAppsText, (packages.size - installedApps.size))
        }

        binding.checkNowLabel.setOnClickListener {
            onClick.invoke()
        }
    }

    fun animateNumber(view: AppCompatTextView, toNumber: Int) {
        GlobalScope.launch {
            delay(500)
            withContext(Dispatchers.Main) {
                val animator = ValueAnimator.ofInt(0, toNumber)
                animator.addUpdateListener {
                    view.text = (it.animatedValue as Int).toString()
                }
                animator.interpolator = DecelerateInterpolator()
                animator.duration = 2000
                animator.start()
            }
        }
    }
}