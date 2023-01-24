package com.example.totalfit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.Exercicio
import com.example.totalfit.repository.ExerciciosRepository

class ExerciciosViewModel(
    exerciciosRepository: ExerciciosRepository
): ViewModel() {

    val listOfItems: LiveData<List<Exercicio>> = exerciciosRepository.getAll()
}