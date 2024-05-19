package com.example.qrcodeapp.database.viewModels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.qrcodeapp.database.daos.CreatedCodesDao
import com.example.qrcodeapp.database.viewModels.CreatedCodesViewModel

class CreatedCodesViewModelFactory(private val dao: CreatedCodesDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CreatedCodesViewModel::class.java)){
            return CreatedCodesViewModel(dao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }

}