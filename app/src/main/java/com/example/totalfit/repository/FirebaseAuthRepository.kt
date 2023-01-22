package com.example.totalfit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.totalfit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "FirebaseAuthRepository"

class FirebaseAuthRepository(private val firebaseAuth: FirebaseAuth) {

    fun login(user: User): LiveData<Resource<Boolean>> {
        val livedata = MutableLiveData<Resource<Boolean>>()
        try {
            firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    livedata.value = Resource(data = true)
                }
                .addOnFailureListener {
                    livedata.value = Resource(data = false, error = it)
                }
        } catch (e: IllegalArgumentException) {
            livedata.value = Resource(data = false, error = e)
        }
        return livedata
    }

    fun signIn(user: User): LiveData<Resource<Boolean>> {
        val livedata = MutableLiveData<Resource<Boolean>>()
        try {
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    livedata.value = Resource(data = true)
                    Log.i(TAG, "signIn: $it")
                }
                .addOnFailureListener {
                    livedata.value = Resource(data = false, error = it)
                    Log.i(TAG, "Error: $it")
                }
        } catch (e: IllegalArgumentException) {
            livedata.value = Resource(data = false, error = e)
        }
        return livedata
    }

    fun isUserLogged(): Boolean {
        val user: FirebaseUser? = Firebase.auth.currentUser
        if (user != null) {
            return true
        }
        return false
    }

    fun singOut() {
        firebaseAuth.signOut()
    }
}