package com.example.totalfit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.User
import com.example.totalfit.repository.FirebaseAuthRepository
import com.example.totalfit.repository.Resource

class SignInViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {

    fun signIn(user: User): LiveData<Resource<Boolean>> {
        return firebaseAuthRepository.signIn(user)
    }
}