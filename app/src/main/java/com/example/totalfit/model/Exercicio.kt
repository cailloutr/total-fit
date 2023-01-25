package com.example.totalfit.model

import android.net.Uri

data class Exercicio(
    val id: String? = null,
    val nome: String = "",
    val imageUrl: Uri? = null,
    val observacoes: String = ""
) {
}