package com.pa.di.modules

import androidx.room.Room
import android.content.Context
import com.app.bhaskar.easypaisa.AppDatabase
import com.pa.models.dao.ResultDataDao

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

val DB_FILE_NAME = "easypaisa.db"
@Module
class DatabaseModule(val context: Context) {


    @Provides @Singleton
    fun provideContactDao(appDatabase: AppDatabase) : ResultDataDao = appDatabase.metOfficeDataDao()

    @Provides @Singleton
    fun privideAppDatabase() : AppDatabase =

            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_FILE_NAME)
                    .build()



}