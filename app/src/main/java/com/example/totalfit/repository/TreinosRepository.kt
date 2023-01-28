package com.example.totalfit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.totalfit.model.Treino
import com.example.totalfit.model.TreinoDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "TreinosRepository"
private const val FIRESTORE_COLLECTION_PATH = "treino"

class TreinosRepository(
    private val firestore: FirebaseFirestore
) {

    fun getById(id: String): LiveData<Treino> =
        MutableLiveData<Treino>().apply {
            firestore.collection(FIRESTORE_COLLECTION_PATH).document(id)
                .addSnapshotListener { s, _ ->
                    s?.let { document ->
                        document.toObject<TreinoDocument>()?.toTreino(document.id)
                            ?.let { treino ->
                                Log.i(TAG, "Firestore: ${treino.exercicios}")
                                value = treino
                            }
                    }
                }
        }


    fun getAllTreino(): LiveData<List<Treino>> = MutableLiveData<List<Treino>>().apply {
        firestore.collection("treino")
            .addSnapshotListener { snapShot, _ ->
                snapShot?.let {
                    val treinoList: List<Treino> = snapShot.documents.mapNotNull {
                        it.toObject<TreinoDocument>()?.toTreino(it.id)
                    }
                    value = treinoList
                }
            }
    }

    fun save(treino: Treino) = MutableLiveData<String>().apply {
        val treinoDocument = TreinoDocument(
            nome = treino.nome,
            descricao = treino.descricao,
            data = treino.data,
            exercicios = treino.exercicios
        )

        val collection = firestore.collection(FIRESTORE_COLLECTION_PATH)
        val document = treino.id?.let { id ->
            collection.document(id)
        } ?: collection.document()

        document.set(treinoDocument)

        value = document.id
    }
}