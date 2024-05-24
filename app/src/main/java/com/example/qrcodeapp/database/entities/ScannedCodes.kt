package com.example.qrcodeapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scanned_codes")
data class ScannedCodes(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var content:String = "",

    var owner: String = "",
    var date: String = ""
)

