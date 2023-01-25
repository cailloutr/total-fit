package com.example.totalfit.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.Exercicio
import com.example.totalfit.repository.ExerciciosRepository

class NewExerciseViewModel(
    private val repository: ExerciciosRepository
) : ViewModel() {

    var imageUrl: Uri? = null

    fun save(exercicio: Exercicio): LiveData<Boolean> = repository.save(exercicio)

}