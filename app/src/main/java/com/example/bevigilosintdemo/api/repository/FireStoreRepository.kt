package com.example.bevigilosintdemo.api.repository

import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.Error
import com.example.bevigilosintdemo.api.model.Settings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class FireStoreRepository private constructor() {

    companion object {

        private var mInstance: FireStoreRepository? = null
        fun getInstance(): FireStoreRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = FireStoreRepository()
                }
            }
            return mInstance!!
        }
    }

    fun getSettings(onSuccess: (Settings) -> Unit, onError: (Error?) -> Unit) {
        val firebaseFirestore = Firebase.firestore
        val documentReference = firebaseFirestore.collection("settings").document("secrets")
        documentReference.get().addOnSuccessListener { document ->
                document?.toObject<Settings>()?.let { settings ->
                   onSuccess.invoke(settings)
                } ?: let { onError.invoke(ApiResponse().unknownError) }
            }
            .addOnFailureListener { exception ->
                onError.invoke(ApiResponse(exception).error)
            }
    }
}