package com.example.totalfit.model

import com.google.firebase.Timestamp

data class TreinoDocument(
    val nome: String = "",
    val descricao: String = "",
    val data: Timestamp = Timestamp.now(),
    val exercicios: List<String> = listOf()
) {
    fun toTreino(id: String): Treino {
        return Treino(
            id = id,
            nome = nome,
            descricao = descricao,
            data = data,
            exercicios = exercicios
        )
    }
}