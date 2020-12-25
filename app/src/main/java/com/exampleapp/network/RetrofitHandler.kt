package com.exampleapp.network

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.exampleapp.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.ResponseBody
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.json.JSONObject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

//
// Created by Fathur Radhy 
// on May 2019-05-31.
//
object RetrofitHandler {

    fun checkResponse(
        mContext: Context,
        success: Boolean,
        code: Int?,
        msg: String?,
        onSuccess: () -> Unit
    ) {
        if (success) {
            when (code) {
                400 -> {
//                    val userPreference = UserPreference(PreferenceManager.getDefaultSharedPreferences(mContext))
//                    userPreference.logout(mContext)
                }
                200 -> {
                    onSuccess()
                }
                else -> {
                    val activity = mContext as Activity
//                    activity.finish()
//                    mContext.startActivity<ErrorActivity>("message" to msg, "code" to code)
                }
            }
        } else {
            val activity = mContext as Activity
//            activity.finish()
//            mContext.startActivity<ErrorActivity>("message" to msg)
        }
    }

    fun checkResponses(
        error: ResponseBody?,
        code: Int,
        onSuccess: (msg: String) -> Unit
    ) {
        Log.d("CODEREQUEST", code.toString())
        when (code) {
            200 -> {
                onSuccess("")
            }
            400 -> {
                try {
                    val jObjError = JSONObject(error!!.string())
                    onSuccess(jObjError.getString("Message"))
                } catch (e: Exception) {
                    onSuccess(e.message.toString())
                }
            }
            else -> {
                try {
                    val jObjError = JSONObject(error!!.string())
                    onSuccess(jObjError.getString("message"))
                } catch (e: Exception) {
                    onSuccess(e.message.toString())
                }
            }
        }
    }

    fun failure(mContext: Context, t: Throwable) : String {
        if (t is SocketTimeoutException) {
            return mContext.resources.getString(R.string.rto)
        } else if (t is ConnectException) {
            return mContext.resources.getString(R.string.unknown_server)
        } else if (t is UnknownHostException) {
            return mContext.resources.getString(R.string.no_internet)
        } else {
            if (t.message != null) {
                return t.message.toString()
            }
        }
        return mContext.resources.getString(R.string.something_went_wrong)
    }

}