package com.example.totalfit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.Exercicio
import com.example.totalfit.model.Treino
import com.example.totalfit.repository.ExerciciosRepository
import com.example.totalfit.repository.TreinosRepository
import com.google.firebase.Timestamp

class NewTreinoViewModel(
    exerciciosRepository: ExerciciosRepository,
    private val treinosRepository: TreinosRepository
): ViewModel() {

    val listExercicios: LiveData<List<Exercicio>> = exerciciosRepository.getAll()

    var listOfExercicioNoTreino: List<String> = listOf()

    var dateOfTreino: Timestamp = Timestamp.now()

    fun save(treino: Treino): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        treinosRepository.save(treino)

        value = true
    }
}