package com.aman.findjob.room.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aman.findjob.room.dao.FormDao
import com.aman.findjob.room.entity.Form

@Database(entities = [Form::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun formDao(): FormDao

    companion object {
        private const val TAG = "AppDatabase"
        private const val DATABASE_NAME = "job_application.db"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    Log.d(TAG, " >>> Creating new database instance")
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            Log.d(TAG, " >>> Getting the database instance")
            return INSTANCE
        }

        fun destroyInstance() {
            Log.d(TAG, " >>> Destroying app database instance")
            INSTANCE = null
        }
    }
}