package com.exampleapp.ui.base

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.exampleapp.R
import com.exampleapp.utils.UserPreference
import org.jetbrains.anko.image
import org.jetbrains.anko.sdk27.coroutines.onClick

abstract class BaseActivity(layoutResource: Int) : AppCompatActivity(layoutResource) {

    lateinit var storage: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //UserPreference
        storage = UserPreference(
            PreferenceManager
                .getDefaultSharedPreferences(this)
        )

//        findViewById<ImageView>(R.id.iv_back)?.onClick {
//            finish()
//        }

        initComponents()
        onViewReady()
    }

    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
        }

    protected abstract fun initComponents()
    protected abstract fun onViewReady()

    protected fun showLoading(isShow: Boolean) {}

    protected fun hideError() {
        val content = findViewById<NestedScrollView>(R.id.content)
        val contentError = findViewById<LinearLayout>(R.id.error_content)

        content.visibility = View.VISIBLE
        contentError.visibility = View.GONE
    }

    protected fun showError(
        isShow: Boolean,
        errorMessage: String? = resources.getString(R.string.something_went_wrong),
        errorIcon: Int? = R.drawable.ic_wi_fi,
        action: () -> Unit
    ) {
        val content = findViewById<NestedScrollView>(R.id.content)
        val contentError = findViewById<LinearLayout>(R.id.error_content)
        val btTryAgain = findViewById<Button>(R.id.bt_try_again)
        val ivIcon = findViewById<ImageView>(R.id.iv_error_icon)
        val tvMessage = findViewById<TextView>(R.id.tv_error_msg)

        tvMessage.text = errorMessage
        ivIcon.image = errorIcon?.let { ContextCompat.getDrawable(this, it) }
        btTryAgain.onClick { action() }

        if (isShow) {
            content.visibility = View.GONE
            contentError.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            contentError.visibility = View.GONE
        }
    }
}