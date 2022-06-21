package com.example.bevigilosintdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.*

fun View.applyTopWindowInsetToMargin() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
        windowInsets
    }
}

@DelicateCoroutinesApi
fun View.showKeyBoardAndFocus() {
    val view = this
    GlobalScope.launch {
        withContext(Dispatchers.Main) {
            delay(500)
            view.isFocusableInTouchMode = true
            if (view.requestFocus()) {
                val inputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

}

@DelicateCoroutinesApi
fun View.hideKeyBoardAndRemoveFocus() {
    val view = this
    GlobalScope.launch {
        withContext(Dispatchers.Main) {
            delay(500)
            val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}

@SuppressLint("CheckResult")
fun AppCompatImageView?.assignImageFromUrl(
    fileUrl: String?, fitCenter: Boolean = false, centerCrop: Boolean = false, centerInside: Boolean = false,
    placeHolder: Int? = null, placeHolderDrawable: Drawable? = null, override: Pair<Int, Int>? = null, isCircleCrop: Boolean = false, bgColor: Int? = null
) {
    val imageView = this
    if (this != null) {
        val requestManager = Glide.with(this.context).load(fileUrl)

        when {
            placeHolder != null -> requestManager.placeholder(placeHolder)
            placeHolderDrawable != null -> requestManager.placeholder(placeHolderDrawable)
        }

        when {
            fitCenter -> requestManager.fitCenter()
            centerCrop -> requestManager.centerCrop()
            centerInside -> requestManager.centerInside()
        }

        if (override != null) {
            requestManager.override(override.first, override.second)
        }

        if (isCircleCrop) {
            requestManager.apply(RequestOptions.circleCropTransform())
        }

        requestManager.into(this)

    }
}
