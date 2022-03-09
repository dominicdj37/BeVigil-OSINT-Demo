package com.example.bevigilosintdemo

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
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
