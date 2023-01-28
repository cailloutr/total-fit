package com.example.totalfit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    fun getUser(): LiveData<FirebaseUser> = firebaseAuthRepository.getUser()
}