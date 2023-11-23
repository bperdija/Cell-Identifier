package com.example.cell_identifier.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CellDBDao {
    @Insert
    suspend fun insert(cellEntity: CellEntity)

    @Query("SELECT * FROM cell_table")
    fun getAll(): Flow<List<CellEntity>>

    @Query("DELETE FROM cell_table WHERE id=:key")
    suspend fun delete(key: Long)
}
