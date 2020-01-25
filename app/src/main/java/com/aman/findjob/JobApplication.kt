package com.aman.findjob

import android.util.Log
import com.aman.findjob.di.AppComponent
import com.aman.findjob.di.DaggerAppComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class JobApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {

        Log.d(TAG, " >>> JobApplication Created")

        if (BuildConfig.DEBUG) {
            Log.d(TAG, " >>> Initializing Stetho")
            Stetho.initializeWithDefaults(this)
        }

        appComponent = DaggerAppComponent.builder().application(this).build()

        return appComponent
    }

    companion object {
        const val TAG = "JobApplication"

        private var appComponent: AppComponent? = null

        fun getAppComponent(): AppComponent? {
            return appComponent
        }
    }
}