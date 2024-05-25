package com.example.qrcodeapp.database.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qrcodeapp.database.daos.UserDao
import com.example.qrcodeapp.database.entities.User
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel(val dao: UserDao): ViewModel() {

    suspend fun addUser(newUserLogin: String, newUserPassword: String, newUserName:String){
       return viewModelScope.async {
            val user = User()

            user.userLogin = newUserLogin
            user.password = newUserPassword
            user.name = newUserName
            dao.insert(user)
        }.await()
    }


    suspend fun getUser(userLogin: String): User {
        return viewModelScope.async {
            dao.get(userLogin)
        }.await()
    }

    suspend fun setPremium(user: User){
        return viewModelScope.async {
            user.premium = true
            dao.update(user)
        }.await()
    }
}