package com.example.qrcodeapp.database.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.database.daos.ScannedCodesDao
import com.example.qrcodeapp.database.viewModels.ScannedCodesViewModel

class ScannedCodesViewModelFactory(private val dao: ScannedCodesDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScannedCodesViewModel::class.java)){
            return ScannedCodesViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}