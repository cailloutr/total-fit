package com.example.totalfit.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.totalfit.model.Exercicio
import com.example.totalfit.repository.ExerciciosRepository

class NewExerciseViewModel(
    private val repository: ExerciciosRepository
) : ViewModel() {

    var imageUrl: Uri? = null

    fun getById(id: String): LiveData<Exercicio> = repository.getById(id)

    fun save(exercicio: Exercicio, image: ByteArray): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {
            val id = repository.save(exercicio)
            id.value?.let { repository.imageUpload(it, image) }

            value = true
        }

    fun saveWithoutImage(exercicio: Exercicio): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {
            repository.save(exercicio)

            value = true
        }
}