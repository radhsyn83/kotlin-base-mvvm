package com.exampleapp.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.preference.PreferenceManager
import com.exampleapp.dagger.AppLifecycleObserver
import com.exampleapp.dagger.DaggerAppComponent
import javax.inject.Inject

class App : Application() {

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()

        //Init dagger
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

    }
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}