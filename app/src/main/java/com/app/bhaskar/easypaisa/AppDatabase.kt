package com.app.bhaskar.easypaisa

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pa.models.Result

import com.pa.models.dao.ResultDataDao

@Database(entities = arrayOf(Result::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase()
{
    abstract fun metOfficeDataDao() : ResultDataDao
}