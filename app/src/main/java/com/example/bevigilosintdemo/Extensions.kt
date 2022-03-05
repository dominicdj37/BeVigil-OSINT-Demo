package com.example.bevigilosintdemo

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

fun View.applyTopWindowInsetToMargin() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
        windowInsets
    }
}