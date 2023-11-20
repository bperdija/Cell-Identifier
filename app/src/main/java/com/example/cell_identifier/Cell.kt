package com.example.cell_identifier

import android.net.Uri

data class Cell(
    val image: Uri,
    val name: String,
    val category: String,
    val number: String
)
