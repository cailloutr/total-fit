package com.example.totalfit.model

import android.net.Uri

data class ExercicioDocument(
    val imageUrl: String = "",
    val nome: String = "",
    val observacoes: String = ""
) {
    fun toExercicio(id: String): Exercicio {
        val uri = try {
            Uri.parse(imageUrl)
        } catch (e: NullPointerException) {
            null
        }

        return Exercicio(
            id = id,
            imageUrl = uri,
            nome = nome,
            observacoes = observacoes
        )
    }
}
