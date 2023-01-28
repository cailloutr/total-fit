package com.example.totalfit.model

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class Treino(
    val id: String? = null,
    val nome: String,
    val descricao: String,
    val data: Timestamp,
    val exercicios: List<String>
) {
    fun getFormattedDate(pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale("pt", "br"))
        return dateFormat.format(data.toDate())
    }
}