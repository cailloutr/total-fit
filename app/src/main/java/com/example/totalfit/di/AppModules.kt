package com.example.totalfit.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.example.totalfit.repository.ExerciciosRepository
import com.example.totalfit.repository.FirebaseAuthRepository
import com.example.totalfit.repository.TreinosRepository
import com.example.totalfit.ui.dialogs.ImagePickerBottomSheet
import com.example.totalfit.ui.dialogs.LoadImageUrlDialog
import com.example.totalfit.ui.exercicios.ExercisesAdapter
import com.example.totalfit.ui.exercicios.ExercisesFragment
import com.example.totalfit.ui.exercicios.NewExerciseFragment
import com.example.totalfit.ui.login.LoginFragment
import com.example.totalfit.ui.login.SignInFragment
import com.example.totalfit.ui.profile.ProfileFragment
import com.example.totalfit.ui.treinos.AddExercicioInTreinoAdapter
import com.example.totalfit.ui.treinos.NewTreinoFragment
import com.example.totalfit.ui.treinos.TreinosFragment
import com.example.totalfit.ui.viewmodel.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    factory<ImagePickerBottomSheet> { ImagePickerBottomSheet(get()) }
    factory<LoadImageUrlDialog> { LoadImageUrlDialog(get(), get()) }
    factory<ExercisesFragment> { ExercisesFragment() }
    factory<ExercisesAdapter> { ExercisesAdapter(get()) }
    factory<AddExercicioInTreinoAdapter> { AddExercicioInTreinoAdapter(get()) }
    factory<NewExerciseFragment> { NewExerciseFragment() }
    factory<LoginFragment> { LoginFragment() }
    factory<SignInFragment> { SignInFragment() }
    factory<ProfileFragment> { ProfileFragment() }
    factory<TreinosFragment> { TreinosFragment() }
    factory<NewTreinoFragment> { NewTreinoFragment() }
}

val daoModule = module {
    single<FirebaseAuthRepository> { FirebaseAuthRepository(get()) }
    single<ExerciciosRepository> { ExerciciosRepository(get(), get()) }
    single<TreinosRepository> { TreinosRepository(get()) }
    single<SharedPreferences> { getDefaultSharedPreferences(get()) }
}

val viewModelModule = module {
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<SignInViewModel> { SignInViewModel(get()) }
    viewModel<UiStateViewModel> { UiStateViewModel() }
    viewModel<ExerciciosViewModel> { ExerciciosViewModel(get()) }
    viewModel<NewExerciseViewModel> { NewExerciseViewModel(get()) }
    viewModel<NewTreinoViewModel> { NewTreinoViewModel(get(), get()) }
}

val firebaseModule = module {
    single<FirebaseAuth> { Firebase.auth }
    single<FirebaseFirestore> { Firebase.firestore }
    single<StorageReference> { Firebase.storage.reference }
}