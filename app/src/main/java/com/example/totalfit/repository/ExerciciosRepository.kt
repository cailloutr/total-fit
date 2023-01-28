package com.example.totalfit.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.totalfit.model.Exercicio
import com.example.totalfit.model.ExercicioDocument
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference

private const val TAG = "ExerciciosRepository"
private const val FIRESTORE_COLLECTION_PATH = "exercicios"

class ExerciciosRepository(
    private val firestore: FirebaseFirestore,
    private val storageReference: StorageReference
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

    fun save(exercicio: Exercicio) = MutableLiveData<String>().apply {
        val exercicioDocument = ExercicioDocument(
            nome = exercicio.nome,
            observacoes = exercicio.observacoes
        )

        val collection = firestore.collection(FIRESTORE_COLLECTION_PATH)
        val document = exercicio.id?.let { id ->
            collection.document(id)
        } ?: collection.document()

        document.set(exercicioDocument)

        value = document.id
    }

    fun remove(exercicioId: String) {
        firestore.collection(FIRESTORE_COLLECTION_PATH)
            .document(exercicioId)
            .delete()

        storageReference.child("exercicios/$exercicioId.jpg")
            .delete()
    }

    fun imageUpload(id: String, image: ByteArray): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {
            // Create a reference to 'exercicios/id.jpg'
            val imagesRef = storageReference.child("exercicios/$id.jpg")

            val uploadTask = imagesRef.putBytes(image)
            uploadTask.addOnFailureListener {
                value = false
            }.addOnSuccessListener {
                value = true

                storageReference.child("exercicios/$id.jpg").downloadUrl.addOnSuccessListener { uri ->
                    Log.i(TAG, "imageUpload: $uri")
                    firestore.collection(FIRESTORE_COLLECTION_PATH)
                        .document(id)
                        .update(
                            mapOf("imageUrl" to uri?.toString())
                        )
                }.addOnFailureListener {
                    Log.i(TAG, "imageUpload: Fail")
                }
            }

        }

//    companion object {
//        private const val FIRESTORE_COLLECTION_PATH = "exercicios"
//    }
}