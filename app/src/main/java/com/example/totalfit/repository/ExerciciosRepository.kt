package com.example.totalfit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.totalfit.model.Exercicio
import com.example.totalfit.model.ExercicioDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ExerciciosRepository(
    private val firestore: FirebaseFirestore
) {
    fun getAll(): LiveData<List<Exercicio>> {
        val liveData: MutableLiveData<List<Exercicio>> = MutableLiveData<List<Exercicio>>()
        firestore.collection("exercicios")
            .get()
            .addOnSuccessListener {
                it?.let { querySnapshot ->
                    val exerciciosList = mutableListOf<Exercicio>()
                    for (document in querySnapshot.documents) {
                        val exercicioFirebase = document.toObject<ExercicioDocument>()
                        exercicioFirebase?.let { exercicioFirebaseNotNull ->
                            exerciciosList.add(exercicioFirebaseNotNull.toExercicio())
                        }
                    }
                    liveData.value = exerciciosList
                }
            }
        return liveData
    }

//    firestore.collection("exercicios")
//    .get()
//    .addOnSuccessListener
//    {
//        it?.let { querySnapshot ->
//            for (document in querySnapshot.documents) {
//                Log.i(com.example.totalfit.ui.TAG, "Firestore: ${document.data}")
//            }
//        }
//    }
//
//    firestore.collection("exercicios")
//    .add(exercicio)
//    .addOnSuccessListener
//    {
//        Log.i(com.example.totalfit.ui.TAG, "produto salvo: ${it.id}")
//    }
}