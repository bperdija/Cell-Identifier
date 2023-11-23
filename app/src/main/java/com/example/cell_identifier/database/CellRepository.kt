package com.example.cell_identifier.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CellRepository(private val cellDBDao: CellDBDao) {
    val userData: Flow<List<CellEntity>> = cellDBDao.getAll()

    fun insert(cellEntity: CellEntity) {
        CoroutineScope(IO).launch {
            cellDBDao.insert(cellEntity)
        }
    }

    fun delete(id: Long) {
        CoroutineScope(IO).launch {
            cellDBDao.delete(id)
        }
    }
}