package com.example.qrcodeapp.database.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qrcodeapp.database.daos.UserDao
import com.example.qrcodeapp.database.entities.CreatedCodes
import com.example.qrcodeapp.database.entities.ScannedCodes
import com.example.qrcodeapp.database.entities.User

@Database(entities = [User::class, CreatedCodes::class, ScannedCodes::class],
    version = 1, exportSchema = false)
abstract class QrDatabase: RoomDatabase() {

    abstract val userDao: UserDao

    companion object{
        @Volatile
        private var INSTANCE: QrDatabase? = null

        fun getInstance(context: Context): QrDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        QrDatabase::class.java,
                        "qr-db"
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}