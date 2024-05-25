package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.CurrentDataHandler
import com.example.qrcodeapp.database.daos.ScannedCodesDao
import com.example.qrcodeapp.database.entities.ScannedCodes
import com.example.qrcodeapp.database.entities.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScannedCodesViewModel(val dao: ScannedCodesDao) : ViewModel() {

    suspend fun addCode(user: User, content: String, date: String) {
        return viewModelScope.async {
            if (user.premium ||
                dao.getAllCodes(user.userLogin).size < CurrentDataHandler.getNonPremiumMaxCodes()
            ) {
                val code = ScannedCodes()

                code.owner = user.userLogin
                code.content = content
                code.date = date
                dao.insert(code)
            }
        }.await()
    }

    suspend fun getCode(id: Int): ScannedCodes {
        return viewModelScope.async {
            dao.getCode(id)
        }.await()
    }

    suspend fun getAllCodes(userLogin: String): List<ScannedCodes> {
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