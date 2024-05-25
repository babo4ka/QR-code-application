package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.daos.CreatedCodesDao
import com.example.qrcodeapp.database.entities.CreatedCodes
import com.example.qrcodeapp.database.entities.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreatedCodesViewModel(val dao: CreatedCodesDao) : ViewModel() {

    suspend fun addCode(user: User, qrCode: ByteArray, content: String) {
        return viewModelScope.async {
            if (user.premium ||
                dao.getAllCodes(user.userLogin).size < CurrentDataHandler.getNonPremiumMaxCodes()
            ) {

                val code = CreatedCodes()

                code.owner = user.userLogin
                code.code = qrCode
                code.content = content
                dao.insert(code)
            }
        }.await()
    }

    suspend fun getCode(id: Int): CreatedCodes {
        return viewModelScope.async {
            dao.getCode(id)
        }.await()
    }

    suspend fun getAllCodes(userLogin: String): List<CreatedCodes> {
        return viewModelScope.async {
            dao.getAllCodes(userLogin)
        }.await()
    }

    suspend fun deleteCode(id: Int) {
        return viewModelScope.async {
            dao.delete(id)
        }.await()
    }
}