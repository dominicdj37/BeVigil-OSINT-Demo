package com.example.bevigilosintdemo.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Environment
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.GoogleApiClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.nio.channels.FileChannel
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

    @Throws(IOException::class)
    fun saveFile(sourceFile: File, name: String) {
        val destFile = File(contextRef?.get()?.filesDir?.path + File.separator + "apks" + File.separator + name.replace(" ", "_") + ".apk")
        try {
            if (destFile.parentFile != null && !destFile.parentFile!!.exists()) {
                destFile.parentFile!!.mkdirs()
            }
            if (!destFile.exists()) {
                destFile.createNewFile()
            }
        } catch (e: Exception) {
        }

        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }
    //endregion
}