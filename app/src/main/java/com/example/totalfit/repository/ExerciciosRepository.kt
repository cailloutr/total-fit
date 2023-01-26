package com.example.totalfit.repository

import androidx.lifecycle.LiveData
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
                        it.toObject<ExercicioDocument>()?.toExercicio(it.id)
                    }
                    value = exerciciosList
                }
            }
    }

    fun getById(id: String): LiveData<Exercicio> = MutableLiveData<Exercicio>().apply {
        firestore.collection(FIRESTORE_COLLECTION_PATH).document(id)
            .addSnapshotListener { s, _ ->
                s?.let { document ->
                    document.toObject<ExercicioDocument>()?.toExercicio(document.id)
                        ?.let { exercicio ->
                            value = exercicio
                        }
                }
            }
    }

/*    fun getExercicioById(id: String): LiveData<Exercicio> = MutableLiveData<Exercicio>().apply {
        val docRef = firestore.collection(FIRESTORE_COLLECTION_PATH).document(id)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    value = document.toObject<ExercicioDocument>()?.toExercicio(document.id)
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }*/


    fun save(exercicio: Exercicio) = MutableLiveData<Boolean>().apply {
        val exercicioDocument = ExercicioDocument(
            nome = exercicio.nome,
            observacoes = exercicio.observacoes
        )

        val collection = firestore.collection(FIRESTORE_COLLECTION_PATH)
        val document = exercicio.id?.let { id ->
            collection.document(id)
        } ?: collection.document()

        document.set(exercicioDocument)

        value = true
    }

    fun remove(exercicioId: String) {
        firestore.collection(FIRESTORE_COLLECTION_PATH)
            .document(exercicioId)
            .delete()
    }
}