package com.example.totalfit.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.totalfit.repository.LoginRepository
import com.example.totalfit.ui.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val daoModule = module {
    single<LoginRepository> { LoginRepository(get()) }
    single<SharedPreferences> { getDefaultSharedPreferences(get()) }
}

val viewModelModule = module {
    viewModel<LoginViewModel> { LoginViewModel(get()) }
}