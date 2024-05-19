package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.daos.CreatedCodesDao
import com.example.qrcodeapp.database.entities.CreatedCodes
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreatedCodesViewModel(val dao: CreatedCodesDao): ViewModel() {

    fun addCode(userLogin: String, content:ByteArray){
        viewModelScope.launch {
            val code = CreatedCodes()

            code.owner = userLogin
            code.content = content
            dao.insert(code)
        }
    }


    suspend fun getCode(userLogin: String): List<CreatedCodes> {
        val res = viewModelScope.async {
            dao.get(userLogin)
        }

        return res.await()

    }
}