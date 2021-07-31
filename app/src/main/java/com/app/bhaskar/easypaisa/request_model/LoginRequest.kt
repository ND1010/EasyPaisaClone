package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("password")
    var password: String = ""
)