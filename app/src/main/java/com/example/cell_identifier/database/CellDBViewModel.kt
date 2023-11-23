package com.example.cell_identifier.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData

class CellDBViewModel(private val cellRepository: CellRepository) : ViewModel() {
    val liveData: LiveData<List<CellEntity>> = cellRepository.userData.asLiveData()

    fun insert(cellEntity: CellEntity) {
        cellRepository.insert(cellEntity)
    }

    fun delete(id: Long) {
        cellRepository.delete(id)
    }
}

class CellVMFactory(private val cellRepository: CellRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CellDBViewModel::class.java))
            return CellDBViewModel(cellRepository) as T
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}