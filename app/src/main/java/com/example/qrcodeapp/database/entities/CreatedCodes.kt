package com.example.qrcodeapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "created_codes")
data class CreatedCodes(
    @PrimaryKey
    var id: Int = 0,

    var content: ByteArray = ByteArray(0),

    var owner: String = ""
)
