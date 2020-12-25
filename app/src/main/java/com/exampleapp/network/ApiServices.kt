package com.exampleapp.network

import com.exampleapp.ui.example.model.ExampleModel
import com.exampleapp.utils.DefaultModel
import io.reactivex.rxjava3.core.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {

    @GET("your/link/here")
    fun default(
        @Path("id") id: String
    ): Observable<DefaultModel>

    @GET("your/link/here")
    fun default2(
        @Path("id") id: String
    ): Observable<JSONObject>

    @GET("todos/{id}")
    fun example(
        @Path("id") id: String
    ): Observable<ExampleModel>


}