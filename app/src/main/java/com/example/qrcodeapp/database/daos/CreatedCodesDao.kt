package com.example.qrcodeapp.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.qrcodeapp.database.entities.CreatedCodes

@Dao
interface CreatedCodesDao {
    @Insert
    suspend fun insert(code: CreatedCodes)

    @Update
    fun update(code: CreatedCodes)

    @Query("DELETE FROM created_codes WHERE id=:id")
    suspend fun delete(id:Int)

    @Query("SELECT * FROM created_codes WHERE id = :id")
    suspend fun getCode(id: Int):CreatedCodes

    @Query("SELECT * FROM created_codes WHERE owner = :userLogin")
    suspend fun getAllCodes(userLogin: String) : List<CreatedCodes>
}