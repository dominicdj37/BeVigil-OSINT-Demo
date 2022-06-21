package com.example.bevigilosintdemo.utils

import com.firebase.ui.auth.AuthUI


object FirebaseAuthUtils {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()
}