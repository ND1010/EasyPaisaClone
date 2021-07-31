package com.app.bhaskar.easypaisa.request_model


import com.google.gson.annotations.SerializedName

data class UpdatePasswordRequest(
    @SerializedName("mobile")
    var mobile: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("token")
    var token: String = ""
)