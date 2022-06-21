package com.example.bevigilosintdemo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.api.model.Error
import com.example.bevigilosintdemo.core.Constants.PACKAGE_NAME_KEY
import com.example.bevigilosintdemo.core.Constants.SEARCH_DOMAIN
import com.example.bevigilosintdemo.databinding.LayoutLoadingProgressBinding
import com.example.bevigilosintdemo.utils.ResourceUtils.getStringResource
import kotlinx.coroutines.*

open class BaseActivity : AppCompatActivity() {

    //region lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    //endregion


    //region navigation
    fun navigateToHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    fun navigateToAppListActivity(isDomainSearch:Boolean = false) {
        startActivity(Intent(this, AppListActivity::class.java).apply {
            putExtra(SEARCH_DOMAIN, isDomainSearch)
        })
    }

    fun navigateToAssetDetailsActivity(packageID: String) {
        startActivity(Intent(this, AssetDetailsActivity::class.java).apply {
            putExtra(PACKAGE_NAME_KEY, packageID)
        })
    }

    fun navigateToSplashScreen() {
        startActivity(Intent(this, SplashActivity::class.java))
    }
    //endregion


    //region loading progress
    fun showLoading(binding: LayoutLoadingProgressBinding) {
        binding.root.visibility = View.VISIBLE
    }

    fun hideLoading(binding: LayoutLoadingProgressBinding) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                binding.root.visibility = View.GONE
            }
        }
    }
    //endregion


    //region error handling
    fun handleError(error: Error?) {
        showDismissiveAlertDialog(
            title = getStringResource(R.string.oops),
            message = error?.message ?: getStringResource(R.string.unknown_error)
        )
    }

    private fun showDismissiveAlertDialog(
        title: String,
        message: String,
        buttonText: String = getStringResource(R.string.ok),
        onDismiss: () -> Unit? = {})
    {
        val mAlertDialog = AlertDialog.Builder(this).create()
        mAlertDialog.setTitle(title)
        mAlertDialog.setMessage(message)
        mAlertDialog.setCancelable(false)
        mAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, Message())
        mAlertDialog.setOnShowListener {
            val positive = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positive?.transformationMethod = null

            positive?.setOnClickListener {
                mAlertDialog.dismiss()
                onDismiss.invoke()
            }
        }
        mAlertDialog.show()
    }
    //endregion
}