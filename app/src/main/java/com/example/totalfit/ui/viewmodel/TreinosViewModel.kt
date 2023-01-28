package com.example.totalfit.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.Treino
import com.example.totalfit.repository.TreinosRepository

class TreinosViewModel(
    private val treinosRepository: TreinosRepository
): ViewModel() {

    val listOfTreinos: LiveData<List<Treino>> = treinosRepository.getAllTreino()

    fun getById(id: String): LiveData<Treino> = treinosRepository.getById(id)

    fun remove(exercicioId: String): LiveData<Boolean> = treinosRepository.remove(exercicioId)
}