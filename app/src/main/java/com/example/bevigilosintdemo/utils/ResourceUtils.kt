package com.example.bevigilosintdemo.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference
import java.util.*


object ResourceUtils {

    private var contextRef: WeakReference<Context>? = null

    //region initialize
    fun initialize(context: Context) {
        contextRef = WeakReference(context)
        contextRef = WeakReference(context)
    }
    //endregion


    //region resource values
    fun getStringResource(id: Int): String {
        return contextRef?.get()?.resources?.getString(id) ?: ""
    }

    fun getDrawableResource(id: Int): Drawable? {
        return contextRef?.get()?.resources?.getDrawable(id, null)
    }

    fun getColorResource(id: Int): Int? {
        val context = contextRef?.get()
        if (context != null) {
            return ContextCompat.getColor(context, id)
        }
        return null
    }
    //endregion
}