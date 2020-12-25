package com.exampleapp.utils

import com.google.gson.annotations.SerializedName

data class DefaultModel(
    @SerializedName("Message")
    var message: String?,
    @SerializedName("Status")
    var status: Int?
)