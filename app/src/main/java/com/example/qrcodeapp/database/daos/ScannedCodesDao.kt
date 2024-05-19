package com.example.qrcodeapp.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.qrcodeapp.database.entities.ScannedCodes


@Dao
interface ScannedCodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(code: ScannedCodes)

    @Update
    fun update(code: ScannedCodes)

    @Delete
    fun delete(code: ScannedCodes)

    @Query("SELECT * FROM scanned_codes WHERE owner = :userLogin")
    suspend fun get(userLogin: String) : List<ScannedCodes>
}