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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(code: CreatedCodes)

    @Update
    fun update(code: CreatedCodes)

    @Delete
    fun delete(code: CreatedCodes)

    @Query("SELECT * FROM created_codes WHERE owner = :userLogin")
    suspend fun get(userLogin: String) : List<CreatedCodes>
}