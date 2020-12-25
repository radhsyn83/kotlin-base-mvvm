package com.exampleapp.ui.example

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.exampleapp.network.NetworkInstance
import com.exampleapp.network.RetrofitHandler
import com.exampleapp.ui.example.model.ExampleModel
import com.exampleapp.utils.DefaultModel
import com.exampleapp.utils.Logger
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ExampleViewModel(private val context: Context) : ViewModel() {
    var data = MutableLiveData<ExampleModel>()
    var error = MutableLiveData<String>()
    var loading = MutableLiveData<Boolean>()

    //SINGLE CALL
    fun load(id: String) {
        var x = 0
        NetworkInstance.api().example(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ExampleModel> {
                override fun onSubscribe(d: Disposable?) {
                    x += 1
                    Logger.print(x.toString())
                    loading.postValue(true)
                }

                override fun onNext(t: ExampleModel?) {
                    data.postValue(t)
                }

                override fun onError(e: Throwable?) {
                    error.postValue(RetrofitHandler.failure(context, e!!))
                }

                override fun onComplete() {
                    loading.postValue(false)
                }
            })
    }

    //SINGLE CALL
    fun loadComposite(id: String) {
        val compositeDisposable = CompositeDisposable()
        val listOfCall = listOf(NetworkInstance.api().default(id), NetworkInstance.api().default2(id))
        compositeDisposable.add(
            Observable.merge(listOfCall)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loading.postValue(true) }
                .doFinally { loading.postValue(false) }
                .subscribe ({
                    when (it) {
                        is DefaultModel -> {
                            /*Update UI for API 1*/
                        }

                        is String -> {
                            /*Update UI for API 2*/
                        }
                    }
                }, {
                    /*onError*/
                    /*oneApiCalledFailed*/
                }, {
                    /*onComplete*/
                    /*onAllApiCalled*/
                })
        )
    }

}