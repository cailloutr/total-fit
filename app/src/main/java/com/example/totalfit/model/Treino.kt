package com.example.totalfit.model

import com.google.firebase.Timestamp

data class Treino(
    val id: String? = null,
    val nome: String,
    val descricao: String,
    val data: Timestamp,
    val exercicios: List<String>
) {
}