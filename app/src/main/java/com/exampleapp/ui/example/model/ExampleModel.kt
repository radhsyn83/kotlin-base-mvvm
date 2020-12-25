package com.exampleapp.ui.example.model


import com.google.gson.annotations.SerializedName

data class ExampleModel(
    @SerializedName("completed")
    val completed: Boolean?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("userId")
    val userId: Int?
) {
    override fun toString(): String {
        return "ExampleModel(completed=$completed, id=$id, title=$title, userId=$userId)"
    }
}