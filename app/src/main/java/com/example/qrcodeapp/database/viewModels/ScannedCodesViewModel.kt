package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.daos.ScannedCodesDao
import com.example.qrcodeapp.database.entities.ScannedCodes
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScannedCodesViewModel(val dao: ScannedCodesDao): ViewModel() {

    fun addCode(userLogin: String, content:ByteArray){
        viewModelScope.launch {
            val code = ScannedCodes()

            code.owner = userLogin
            code.content = content
            dao.insert(code)
        }
    }


    suspend fun getCode(userLogin: String): List<ScannedCodes> {
        val res = viewModelScope.async {
            dao.get(userLogin)
        }

        return res.await()

    }
}