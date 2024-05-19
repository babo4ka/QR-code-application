package com.example.qrcodeapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey
    var userLogin: String = "",

    var password: String = "",

    var name: String = ""

)