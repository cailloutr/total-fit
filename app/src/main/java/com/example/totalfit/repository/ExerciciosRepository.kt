package com.example.totalfit.repository

import androidx.lifecycle.MutableLiveData
import com.example.totalfit.model.Exercicio
import com.example.totalfit.model.ExercicioDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

//private const val TAG = "ExerciciosRepository"

private const val FIRESTORE_COLLECTION_PATH = "exercicios"

class ExerciciosRepository(
    private val firestore: FirebaseFirestore
) {

    fun getAll() = MutableLiveData<List<Exercicio>>().apply {
        firestore.collection(FIRESTORE_COLLECTION_PATH).orderBy("nome")
            .addSnapshotListener { snapShot, _ ->
                snapShot?.let {
                    val exerciciosList: List<Exercicio> = snapShot.documents.mapNotNull {
                        it.toObject<ExercicioDocument>()?.toExercicio()
                    }
                    value = exerciciosList
                }
            }
    }

    fun save(exercicio: Exercicio) = MutableLiveData<Boolean>().apply {
        val exercicioDocument = ExercicioDocument(
            nome = exercicio.nome,
            observacoes = exercicio.observacoes
        )

        firestore.collection(FIRESTORE_COLLECTION_PATH)
            .add(exercicioDocument)
            .addOnSuccessListener {
                value = true
            }
            .addOnFailureListener {
                value = false
            }
    }
}