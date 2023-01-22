package com.example.totalfit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.User
import com.example.totalfit.repository.FirebaseAuthRepository
import com.example.totalfit.repository.Resource

class LoginViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
): ViewModel() {

    fun login(user: User): LiveData<Resource<Boolean>> = firebaseAuthRepository.login(user)

    fun logout() = firebaseAuthRepository.singOut()

    private fun isLogged(): Boolean = firebaseAuthRepository.isUserLogged()

    fun isNotLogged(): Boolean = !isLogged()
}