package com.example.bevigilosintdemo.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowCompat
import com.example.bevigilosintdemo.api.repository.FireStoreRepository
import com.example.bevigilosintdemo.core.Constants.APP_TAG
import com.example.bevigilosintdemo.core.SessionRepo
import com.example.bevigilosintdemo.databinding.ActivitySplashBinding
import com.example.bevigilosintdemo.utils.FirebaseAuthUtils
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
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

    private fun tryAutoSignIn() {
        binding.googleSignInButton.visibility = View.GONE
        binding.signInProgressbar.visibility = View.VISIBLE

        GlobalScope.launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                if(FirebaseAuth.getInstance().currentUser != null) {
                    onLoginSuccess(null, FirebaseAuth.getInstance().currentUser)
                } else {
                    initiateFirebaseSignIn()
                }
            }
        }
    }

    private fun initiateFirebaseSignIn() {
        binding.signInProgressbar.visibility = View.GONE
        binding.googleSignInButton.visibility = View.VISIBLE
        binding.googleSignInButton.setOnClickListener {
            signInLauncher.launch(FirebaseAuthUtils.signInIntent)
        }
    }

    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            onLoginSuccess(response, FirebaseAuth.getInstance().currentUser)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            val error = response?.error?.errorCode
            initiateFirebaseSignIn()
        }
    }

    private fun onLoginSuccess(response: IdpResponse?, currentUser: FirebaseUser?) {
        Log.d(APP_TAG, "sign in response: $response")
        Log.d(APP_TAG, "sign in user: $currentUser")


        FireStoreRepository.getInstance().getSettings({ settings ->
            SessionRepo.initialize(response)
            navigateToHomeActivity()
            finish()
        }) { error ->
            handleError(error)
            initiateFirebaseSignIn()
        }


    }
}