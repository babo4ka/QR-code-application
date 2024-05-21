package com.example.qrcodeapp.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.qrcodeapp.database.entities.ScannedCodes


@Dao
interface ScannedCodesDao {
    @Insert
    suspend fun insert(code: ScannedCodes)

    @Update
    fun update(code: ScannedCodes)

    @Query("DELETE FROM scanned_codes WHERE id=:id")
    suspend fun delete(id:Int)

    @Query("SELECT * FROM scanned_codes WHERE id = :id")
    suspend fun getCode(id: Int): ScannedCodes

    @Query("SELECT * FROM scanned_codes WHERE owner = :userLogin")
    suspend fun getAllCodes(userLogin: String) : List<ScannedCodes>
}