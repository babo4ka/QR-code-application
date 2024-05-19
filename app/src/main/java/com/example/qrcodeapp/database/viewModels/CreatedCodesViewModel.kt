package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.daos.CreatedCodesDao
import com.example.qrcodeapp.database.entities.CreatedCodes
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreatedCodesViewModel(val dao: CreatedCodesDao): ViewModel() {

    fun addCode(userLogin: String, qrCode:ByteArray, content:String){
        viewModelScope.launch {
            val code = CreatedCodes()

            code.owner = userLogin
            code.code = qrCode
            code.content = content
            dao.insert(code)
        }
    }

    suspend fun getCode(id:Int):CreatedCodes{
        val res = viewModelScope.async {
            dao.getCode(id)
        }

        return res.await()
    }

    suspend fun getAllCodes(userLogin: String): List<CreatedCodes> {
        val res = viewModelScope.async {
            dao.getAllCodes(userLogin)
        }

        return res.await()

    }
}