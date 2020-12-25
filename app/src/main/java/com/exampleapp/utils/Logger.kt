package com.exampleapp.utils

import android.util.Log
import com.exampleapp.BuildConfig.SHOW_DEBUG
import com.exampleapp.BuildConfig.TITLE_DEBUG

object Logger {
    fun print(message: String?) {
        print(TITLE_DEBUG, message)
    }

    fun print(tag: String?, message: String?) {
        if (SHOW_DEBUG == "1") {
            Log.d(TITLE_DEBUG, message!!)
        }
    }
}