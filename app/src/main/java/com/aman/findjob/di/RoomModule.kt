package com.aman.findjob.di

import android.app.Application
import android.content.Context
import com.aman.findjob.room.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(applicationContext: Application): AppDatabase {
        return AppDatabase.getInstance(applicationContext)!!
    }
}