package com.aman.findjob.di

import com.aman.findjob.repo.FormRepo
import com.aman.findjob.repo.FormRepoI
import com.aman.findjob.room.database.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepoModule {

    @Provides
    @Singleton
    fun formRepoProvider(appDatabase: AppDatabase): FormRepoI {
        return FormRepo(appDatabase)
    }
}