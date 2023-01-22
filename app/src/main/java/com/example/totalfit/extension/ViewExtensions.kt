package com.example.totalfit.extension

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(
    message: String,
    length: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(
        this,
        message,
        length
    ).show()
}