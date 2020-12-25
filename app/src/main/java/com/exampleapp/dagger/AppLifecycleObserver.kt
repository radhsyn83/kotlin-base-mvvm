package com.exampleapp.dagger

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.exampleapp.utils.Logger
import javax.inject.Inject

class AppLifecycleObserver @Inject constructor(context: Context) : LifecycleObserver {

    /**
     * Shows foreground {@link android.widget.Toast} after attempting to cancel the background one.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        Logger.print("Enter Foreground")
    }

    /**
     * Shows background {@link android.widget.Toast} after attempting to cancel the foreground one.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        Logger.print("Enter Background")
    }

}
