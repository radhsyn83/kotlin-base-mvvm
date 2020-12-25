package com.exampleapp.network

import com.exampleapp.BuildConfig.BASE_URL
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object NetworkInstance {
    fun api(token: String? = null): ApiServices {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(getInterceptor(token))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }

    private fun getInterceptor(token: String?): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)

        okHttp.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                token?.let { requestBuilder.header("token", it) }

                val request = requestBuilder.build()
                return chain.proceed(request)
            }
        })

        return okHttp.build()
    }
}