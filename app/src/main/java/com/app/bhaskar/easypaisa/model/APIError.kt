package com.app.bhaskar.easypaisa.model


import com.google.gson.annotations.SerializedName

data class APIError(
    @SerializedName("httpErrorCode")
    var httpErrorCode: Int = 0,
    @SerializedName("message")
    var message: String = ""
)