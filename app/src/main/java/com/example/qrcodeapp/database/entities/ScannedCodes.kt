package com.example.qrcodeapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_codes")
data class ScannedCodes(
    @PrimaryKey
    var id: Int = 0,

    var code: ByteArray = ByteArray(0),

    var content:String = "",

    var owner: String = ""
)

