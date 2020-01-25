package com.aman.findjob.di

import com.aman.findjob.ui.MainActivity
import com.aman.findjob.ui.newForm.NewFormFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector()
    abstract fun mainActivityProvider() : MainActivity

    @ContributesAndroidInjector()
    abstract fun newFormFragmentProvider() : NewFormFragment

}
