package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.daos.UserDao
import com.example.qrcodeapp.database.entities.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel(val dao: UserDao): ViewModel() {

    fun addUser(newUserLogin: String, newUserPassword: String, newUserName:String){
        viewModelScope.launch {
            val user = User()

            user.userLogin = newUserLogin
            user.password = newUserPassword
            user.name = newUserName
            dao.insert(user)
        }
    }


    suspend fun getUser(userLogin: String): User {
        val res = viewModelScope.async {
            dao.get(userLogin)
        }

        return res.await()

    }
}