package com.example.qrcodeapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "created_codes")
data class CreatedCodes(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var code: ByteArray = ByteArray(0),

    var content:String = "",

    var owner: String = ""
)
