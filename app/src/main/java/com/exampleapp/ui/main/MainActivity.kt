package com.exampleapp.ui.main

import androidx.lifecycle.ViewModelProvider
import com.exampleapp.R
import com.exampleapp.ui.base.BaseActivity
import com.exampleapp.ui.example.ExampleViewModel
import com.exampleapp.utils.Logger

class MainActivity : BaseActivity(R.layout.activity_main) {

    lateinit var viewModel: ExampleViewModel

    override fun initComponents() {

    }

    override fun onViewReady() {
        viewModel = ViewModelProvider(this, viewModelFactory { ExampleViewModel(this) }).get(
            ExampleViewModel::class.java
        )
        viewModel.data.observe(this, {
            hideError()
            Logger.print(it.toString())
        })
        viewModel.loading.observe(this, {
            Logger.print(it.toString())
        })
        viewModel.error.observe(this, {
            showError(true, it) {
                viewModel.load("1")
            }
        })
        viewModel.load("1")
    }
}