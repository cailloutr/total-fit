package com.example.totalfit.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.totalfit.repository.ExerciciosRepository
import com.example.totalfit.repository.FirebaseAuthRepository
import com.example.totalfit.ui.viewmodel.ExerciciosViewModel
import com.example.totalfit.ui.viewmodel.LoginViewModel
import com.example.totalfit.ui.viewmodel.SignInViewModel
import com.example.totalfit.ui.viewmodel.UiStateViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val daoModule = module {
    single<FirebaseAuthRepository> { FirebaseAuthRepository(get()) }
    single<ExerciciosRepository> { ExerciciosRepository(get()) }
    single<SharedPreferences> { getDefaultSharedPreferences(get()) }
}

val viewModelModule = module {
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<SignInViewModel> { SignInViewModel(get()) }
    viewModel<UiStateViewModel> { UiStateViewModel() }
    viewModel<ExerciciosViewModel> { ExerciciosViewModel(get()) }
}

val firebaseModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
}