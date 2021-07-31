package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    var message: String = "",
    @SerializedName("status")
    var status: String = ""
)