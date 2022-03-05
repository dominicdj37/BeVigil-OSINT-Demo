package com.example.bevigilosintdemo.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.Guideline
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding

open class BaseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun openSoftKeyboard(view: View) {
        view.requestFocus()
        // open the soft keyboard
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}