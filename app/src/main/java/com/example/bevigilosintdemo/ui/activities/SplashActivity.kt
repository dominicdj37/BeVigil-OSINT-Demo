package com.example.bevigilosintdemo.ui.activities

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.repository.FireStoreRepository
import com.example.bevigilosintdemo.core.SessionRepo
import com.example.bevigilosintdemo.databinding.ActivitySplashBinding
import com.example.bevigilosintdemo.utils.FirebaseAuthUtils
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.*

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tryAutoSignIn()
    }

    //region auto sign in
    private fun tryAutoSignIn() {
        binding.googleSignInButton.visibility = View.GONE
        binding.signInProgressbar.visibility = View.VISIBLE

        GlobalScope.launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                if(FirebaseAuth.getInstance().currentUser != null) {
                    onLoginSuccess(FirebaseAuth.getInstance().currentUser)
                } else {
                    initiateFirebaseSignIn()
                }
            }
        }
    }

    //region sign in UI
    private fun initiateFirebaseSignIn() {
        binding.signInProgressbar.visibility = View.GONE
        binding.googleSignInButton.visibility = View.VISIBLE
        binding.googleSignInButton.setOnClickListener {
            signInLauncher.launch(FirebaseAuthUtils.signInIntent)
        }
    }
    //endregion


    //region firebase auth
    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            onLoginSuccess(FirebaseAuth.getInstance().currentUser)
        } else {
            handleError(response?.error?.let { ApiResponse(it).error })
            initiateFirebaseSignIn()
        }
    }
    //endregion


    //region post login
    private fun onLoginSuccess(currentUser: FirebaseUser?) {
        FireStoreRepository.getInstance().getSettings({ settings ->
            SessionRepo.initialize(currentUser,settings)
            navigateToHomeActivity()
            finish()
        }) { error ->
            handleError(error)
            initiateFirebaseSignIn()
        }
    }
    //endregion
}