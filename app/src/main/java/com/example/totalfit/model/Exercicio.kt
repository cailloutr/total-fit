package com.example.totalfit.model

import android.net.Uri

data class Exercicio(
    val id: Long = 0,
    val nome: String = "",
    val imageUrl: Uri? = null,
    val observacoes: String = ""
) {
}