package com.example.bevigilosintdemo.api.repository

import com.example.bevigilosintdemo.api.core.ApiConstants.PACKAGE_NAMES
import com.example.bevigilosintdemo.api.core.ApiConstants.SEARCH_HISTORY
import com.example.bevigilosintdemo.api.core.ApiConstants.SECRETS
import com.example.bevigilosintdemo.api.core.ApiConstants.SETTINGS
import com.example.bevigilosintdemo.api.core.ApiResponse
import com.example.bevigilosintdemo.api.model.Error
import com.example.bevigilosintdemo.api.model.SearchHistory
import com.example.bevigilosintdemo.api.model.Settings
import com.example.bevigilosintdemo.core.SessionRepo
import com.google.firebase.firestore.FieldValue
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
        Firebase.firestore.collection(SETTINGS).document(SECRETS).get()
            .addOnSuccessListener { document ->
                document?.toObject<Settings>()?.let { settings ->
                    onSuccess.invoke(settings)
                } ?: let { onError.invoke(ApiResponse().unknownError) }
            }.addOnFailureListener { exception ->
                onError.invoke(ApiResponse(exception).error)
            }
    }

    fun addSearchString(inputString: String, onSuccess: () -> Unit, onError: (Error?) -> Unit) {
        Firebase.firestore.collection(SEARCH_HISTORY)
            .document(SessionRepo.user?.uid ?: "")
            .update(PACKAGE_NAMES, FieldValue.arrayUnion(inputString)).addOnSuccessListener {
                onSuccess.invoke()
            }.addOnFailureListener { exception ->
                onError.invoke(ApiResponse(exception).error)
            }
    }

    fun createSearchHistoryDataIfNotExist(onSuccess: () -> Unit, onError: (Error?) -> Unit) {
        val documentReference = Firebase.firestore.collection(SEARCH_HISTORY).document(SessionRepo.user?.uid ?: "")
        documentReference.get().addOnSuccessListener { snapShot ->
            if (!snapShot.exists()) {
                val hash = hashMapOf(PACKAGE_NAMES to arrayListOf<String>())
                documentReference.set(hash).addOnSuccessListener {
                    onSuccess.invoke()
                }.addOnFailureListener { exception ->
                    onError.invoke(ApiResponse(exception).error)
                }
            } else {
                onSuccess.invoke()
            }
        }
    }

    fun getSearchHistory(onSuccess: (SearchHistory) -> Unit, onError: (Error?) -> Unit) {
        val documentReference = Firebase.firestore.collection(SEARCH_HISTORY)
            .document(SessionRepo.user?.uid ?: "")
        documentReference.get().addOnSuccessListener { document ->
            document?.toObject<SearchHistory>()?.let { settings ->
                onSuccess.invoke(settings)
            }
        }
    }

    fun removePackagesFromSearchHistory(onSuccess: () -> Unit, onError: (Error?) -> Unit) {
        val deletePackageNames = hashMapOf<String, Any>(PACKAGE_NAMES to FieldValue.delete())
        val documentReference = Firebase.firestore.collection(SEARCH_HISTORY)
            .document(SessionRepo.user?.uid ?: "")
        documentReference.update(deletePackageNames).addOnSuccessListener { document ->
            onSuccess.invoke()
        }
    }
}