package com.example.cell_identifier.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cell_table")
data class CellEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)