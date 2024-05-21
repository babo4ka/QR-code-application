package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.daos.ScannedCodesDao
import com.example.qrcodeapp.database.entities.ScannedCodes
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScannedCodesViewModel(val dao: ScannedCodesDao): ViewModel() {

    fun addCode(userLogin: String, qrCode:ByteArray, content:String){
        viewModelScope.launch {
            val code = ScannedCodes()

            code.owner = userLogin
            code.code = qrCode
            code.content = content
            dao.insert(code)
        }
    }

    suspend fun getCode(id:Int): ScannedCodes {
        val res = viewModelScope.async {
            dao.getCode(id)
        }

        return res.await()
    }

    suspend fun getAlLCodes(userLogin: String): List<ScannedCodes> {
        val res = viewModelScope.async {
            dao.getAllCodes(userLogin)
        }

        return res.await()
    }

    suspend fun deleteCode(id:Int){
        val res = viewModelScope.async {
            dao.delete(id)
        }

        res.await()
    }
}