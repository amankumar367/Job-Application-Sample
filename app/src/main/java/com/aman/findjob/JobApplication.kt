package com.aman.findjob

import android.app.Application
import android.util.Log
import com.aman.findjob.di.AppComponent
import com.aman.findjob.di.DaggerAppComponent
import com.facebook.stetho.Stetho

class JobApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Log.d(TAG, " >>> Initializing Stetho")
        }

        appComponent = DaggerAppComponent.builder().application(this).build()
    }

    companion object {
        const val TAG = "JobApplication"

        private var appComponent: AppComponent? = null

        fun getAppComponent(): AppComponent? {
            return appComponent
        }
    }
}